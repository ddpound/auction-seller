package com.example.auctionseller.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionseller.model.BoardCategory;
import com.example.auctionseller.model.CommonModel;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.BoardCategoryRepository;
import com.example.auctionseller.repository.CommonModelRepository;
import com.example.auctionseller.repository.ProductModelRepository;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.auctionseller.sellercommon.ReturnTokenUsername;
import com.example.auctionseller.userinterface.AuctionUserInterFace;
import com.example.modulecommon.enums.AuthNames;
import com.example.modulecommon.frontModel.UserModelFront;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final ReturnTokenUsername returnTokenUsername;

    private final ProductModelRepository productModelRepository;

    private final AuctionUserInterFace auctionUserInterFace;

    private final CommonModelRepository commonModelRepository;

    private final BoardCategoryRepository boardCategoryRepository;

    // 카테고리를 만들었을때 이름이 똑같으면, 알아서 만들어지도록할지..아니면...음..
    /**
     * 판매자의 글을 작성할 수 있다, 수정도 같이 생각해서 넣어야 할 꺼같다.
     * 수정일 때는 카테고리와 썸네일이 비어서 도착할 수도있음
     * @param modify 참일때 수정, 거짓이면 처음 저장함
     *
     * */
    @Transactional
    public int saveBoard(String title,
                         String content,
                         MultipartFile thumbnail,
                         int categoryId,
                         HttpServletRequest request,
                         boolean modify,
                         Integer boardId){

        Map<Integer, Object> returnMapUserData = returnTokenUsername.tokenGetUsername(request);


        // 어디 쇼핑몰의 글을 저장할지 찾아냄
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUsername(returnMapUserData.get(1).toString());

        // 카테고리를 받아와 찾아냄, 보드 카테고리는 반드시 있어야함
        Optional<BoardCategory> findBoardCategory = boardCategoryRepository.findById(categoryId);


        // 썸네일 사진
        Map<Integer, String> returnPathNams = null;

        // 주의 반드시 옮기고 난다음에 content를 수정할것
        Map<Integer, String> makeFileResult = null;

        // 수정이 아니라면
        if(!modify){

            //썸네일
            returnPathNams = makeFile.makeFileImage((Integer)returnMapUserData.get(2),thumbnail,request);

            // 바꾸기전에 먼저이동, 검사를 통해 안쓰는 파일들을 삭제시킬 예정
            makeFileResult = makeFile.saveMoveImageFiles((Integer)returnMapUserData.get(2),content,AuthNames.Seller,"");
        }

        // 사진을 옮긴후 content 내용의 url경로도 변경
        String changecontent = "";


        // 수정작업 들어감
        if(boardId != null && modify){

            // 영속화
            Optional<CommonModel> commonModel = commonModelRepository.findById(boardId);

            //수정인데 썸네일이 있다면
            if(thumbnail != null){
                returnPathNams = makeFile.makeFileImage((Integer)returnMapUserData.get(2),thumbnail,request);

                commonModel.get().setPictureUrlPath(returnPathNams.get(1));
                commonModel.get().setPictureFilePath(returnPathNams.get(2));
            }


            makeFileResult = makeFile
                    .saveMoveImageFiles((Integer)returnMapUserData.get(2),
                            content,
                            AuthNames.Seller,
                            commonModel.get().getFilefolderPath());

            changecontent = makeFile.changeContentImageUrlPath((Integer)returnMapUserData.get(2),
                    content,makeFileResult.get(2),request);

            commonModel.get().setContent(changecontent);
            commonModel.get().setTitle(title);

            // 즉 수정된 카테고리가 없을때는 전에 있던 카테고리를 받아와 다시 재설정해주자
            commonModel.get().setBoardCategory(findBoardCategory.orElse(commonModel.get().getBoardCategory()));



            //수정 진행후 사용하지 않는 이미지 삭제
            makeFile.modifyImageFile(commonModel.get().getFilefolderPath(),changecontent);
            // 임시파일 삭제
            makeFile.deleteTemporary((Integer)returnMapUserData.get(2));
            return 1;
        }


        changecontent = makeFile.changeContentImageUrlPath((Integer)returnMapUserData.get(2),
                content,makeFileResult.get(2),request);


        commonModelRepository.save(CommonModel.builder()
                .boardCategory(findBoardCategory.get())
                .shoppingMall(shoppingMallModel)
                .pictureUrlPath(returnPathNams.get(1))
                .pictureFilePath(returnPathNams.get(2))
                .filefolderPath(makeFileResult.get(2))
                .title(title)
                .Content(changecontent)
                .build());


        // 다 저장했으면 삭제
        makeFile.deleteTemporary((Integer)returnMapUserData.get(2));
        return 1;
    }

    @Transactional
    public int deleteSellerBoard(int boardId, HttpServletRequest request){

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtRHeader = request.getHeader("RefreshToken");

        String token = jwtHeader.replace("Bearer ", "");

        Map<Integer, Object> returnMapUserData = returnTokenUsername.tokenGetUsername(request);

        // 유저의 권한을 찾아야함
        ResponseEntity<UserModelFront> userModelFront =
                auctionUserInterFace.findUserModelFront(jwtHeader,jwtRHeader, (Integer) returnMapUserData.get(2));

        // 수정이니 이미 제품이 있으니 검사를 시도
        // 동시에 영속화
        Optional<CommonModel> commonModel = commonModelRepository.findById(boardId);

        if(commonModel.isPresent()){
            // 삭제하려는 유저와 제품의 유저가 달라 -2를 반환
            // 리스트의 길이가 3이 아닐때 즉 어드민이 아니면서 해당 사용자의 제품이 아니라면
            if(!commonModel.get().getShoppingMall().getUsername().equals((String)returnMapUserData.get(1))
                    && userModelFront.getBody().getRole().equals("ADMIN")){
                return -2;
            }
            // 썸네일이 있을 때
            if(commonModel.get().getPictureFilePath().length() >0){
                // 썸네일 파일 삭제
                String filepathString = commonModel.get().getPictureFilePath();

                makeFile.filePathImageDelete(filepathString);

            }
            if(commonModel.get().getFilefolderPath().length() >0){
                makeFile.folderPathImageDelete(commonModel.get().getFilefolderPath());
            }

            //마지막 단계에 삭제

            // 사진을 담아놓는 폴더 경로를 통해 파일 삭제
            commonModelRepository.delete(commonModel.get());
            return 1;


        }

        return -1;
    }

    @Transactional(readOnly = true)
    public List<BoardCategory> getBoardCategoryList(HttpServletRequest request){

        Map<Integer, Object> returnMapUserData = returnTokenUsername.tokenGetUsername(request);

        // 현재 접속중인 유저의 쇼핑몰을 검색
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUsername((String) returnMapUserData.get(1));


        return boardCategoryRepository.findAllByShoppingMall(shoppingMallModel);
    }

    @Transactional
    public int saveBoardCategory(HttpServletRequest request, String categoryName){

        Map<Integer, Object> returnMapUserData = returnTokenUsername.tokenGetUsername(request);

        if(categoryName.contains(" ")){
            return -2; // 공백검사
        }

        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUsername((String) returnMapUserData.get(1));

        //만약에 이미 있는거라면 세이브 없이 그냥 올바른 값만 보내기
        if(boardCategoryRepository.findByCategoryName(categoryName) != null){
            return 1;
        }


        try {
            boardCategoryRepository.save(BoardCategory
                    .builder()
                    .categoryName(categoryName)
                    .shoppingMall(shoppingMallModel)
                    .build());

        }catch (Exception e){
            log.info("boardCategory saveFail, fail shoppingMall"+ shoppingMallModel.getShoppingMallName());
            return -1; // 특정에러
        }


        return 1;
    }

    @Transactional
    public int modifyBoardCategory(HttpServletRequest request, String categoryName){

        Map<Integer, Object> returnMapUserData = returnTokenUsername.tokenGetUsername(request);
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUsername((String) returnMapUserData.get(1));

        // 영속화
        BoardCategory boardCategory = boardCategoryRepository.findByShoppingMall(shoppingMallModel);

        try {
            // 더티체킹
            boardCategory.setCategoryName(categoryName);

        }catch (Exception e){
            log.info("boardCategory modify Fail, fail shoppingMall :"+ shoppingMallModel.getShoppingMallName());
            return -1; // 특정에러
        }


        return 1;
    }

}
