package com.example.auctionseller.service;

import com.example.auctionseller.model.*;
import com.example.auctionseller.repository.*;
import com.example.auctionseller.sellercommon.SellerReturnTokenUsername;
import com.example.auctionseller.userinterface.AuctionUserInterFace;

import com.example.modulecommon.allstatic.MyTokenProperties;
import com.example.modulecommon.enums.AuthNames;
import com.example.modulecommon.frontModel.UserModelFront;
import com.example.modulecommon.jwtutil.CookieJWTUtil;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final SellerReturnTokenUsername sellerReturnTokenUsername;

    private final AuctionUserInterFace auctionUserInterFace;

    private final CommonModelRepository commonModelRepository;

    private final BoardCategoryRepository boardCategoryRepository;

    private final CommonReplyRepsitory commonReplyRepsitory;

    private final ReplyofReplyRepository replyofReplyRepository;

    private final CookieJWTUtil cookieJWTUtil;

    private final MyTokenProperties myTokenProperties;

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

        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);


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

            if(commonModel.isEmpty()){
                log.info("not found board , Modifer : " + returnMapUserData.get(1));
                return -2; // 수정하려는 게시판이 없음
            }

            // 수정하려는 자와 게시판 작성자의 이름 비교
            if(!returnMapUserData.get(1).equals(commonModel.get().getShoppingMall().getUsername())){
                log.info("Author and Modifier are not the same");
                log.info("Board Author : " + commonModel.get().getShoppingMall().getUsername());
                log.info("Modifier : " + returnMapUserData.get(1));
                // 수정자와 작성된 작성자의 아이디가 같지않음
                return -3;
            }

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

    /**
     * 삭제 시 주의사항
     * 1. 대댓글 삭제 댓글삭제
     * 2. 게시판 삭제
     * 3. 이미지 삭제
     *
     * */
    @Transactional
    public int deleteSellerBoard(int boardId, HttpServletRequest request){

        String myCookie = cookieJWTUtil
                .requestListCookieGetString(myTokenProperties.getJWT_COOKIE_NAME(),request);

        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

        // 유저의 권한을 찾아야함
        ResponseEntity<UserModelFront> userModelFront =
                auctionUserInterFace
                        .findUserModelFront(myTokenProperties.getJWT_COOKIE_NAME()+"="+myCookie, (Integer) returnMapUserData.get(2));

        // 수정이니 이미 제품이 있으니 검사를 시도
        // 동시에 영속화
        Optional<CommonModel> commonModel = commonModelRepository.findById(boardId);

        try {
            if(commonModel.isPresent()){
                // 삭제하려는 유저와 제품의 유저가 달라 -2를 반환
                // 리스트의 길이가 3이 아닐때 즉 어드민이 아니면서 해당 사용자의 제품이 아니라면 -2 반환
                if(!commonModel.get().getShoppingMall().getUsername().equals((String)returnMapUserData.get(1))
                        && !userModelFront.getBody().getRole().equals("ADMIN")){
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

                // 마지막 단계에 삭제

                // 대댓글 삭제
                replyofReplyRepository.deleteAllByCommonModelId(commonModel.get().getId());

                // 보드와 관련된 댓글들을 모두 삭제
                commonReplyRepsitory.deleteAllByCommonModelId(commonModel.get().getId());

                commonModelRepository.delete(commonModel.get());
                return 1;


            }

        }catch (Exception e){
            log.error(e);
            return -1;
        }


        return -1;
    }

    @Transactional(readOnly = true)
    public List<BoardCategory> getBoardCategoryList(HttpServletRequest request){

        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

        // 현재 접속중인 유저의 쇼핑몰을 검색
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUsername((String) returnMapUserData.get(1));


        return boardCategoryRepository.findAllByShoppingMall(shoppingMallModel);
    }

    @Transactional
    public int saveBoardCategory(HttpServletRequest request, String categoryName){

        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

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

        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);
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

    /**
     * 댓글 저장 메소드
     * */
    @Transactional
    public int saveReply(String content,
                         int userId,
                         String nickName,
                         String userPicture,
                         int commonModelId,
                         HttpServletRequest request){


        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

        Optional<CommonModel> findCommonModel =  commonModelRepository.findById(commonModelId);

        if(findCommonModel.isPresent()){
            CommonReplyModel commonReplyModel
                    = CommonReplyModel.builder()
                    .content(content)
                    .userId(userId)
                    .nickName(nickName)
                    .userPicture(userPicture)
                    .commonModelId(findCommonModel.get().getId())
                    .build();

            commonReplyRepsitory.save(commonReplyModel);
        }


        System.out.println("여기가지 도달하나?");
        return 1;
    }

    @Transactional
    public int modifyReply(String content,
                         int userId,
                         String nickName,
                         String userPicture,
                         int commonModelId,
                         HttpServletRequest request){
        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

        Optional<CommonModel> findCommonModel =  commonModelRepository.findById(commonModelId);

        if(findCommonModel.isPresent()){
            CommonReplyModel commonReplyModel
                    = CommonReplyModel.builder()
                    .content(content)
                    .userId(userId)
                    .nickName(nickName)
                    .userPicture(userPicture)
                    .commonModelId(findCommonModel.get().getId())
                    .build();

            commonReplyRepsitory.save(commonReplyModel);
        }
        log.info("success modify reply");
        return 1;
    }

    /**
    * @param replyId 댓글 아이디 가져오기
    * 1. 꼭 삭제시 아이디 비교, 어드민인지를 비교 한후 삭제하도록
    *
    * */
    @Transactional
    public int deleteReply(int replyId, HttpServletRequest request){

        String myCookie = cookieJWTUtil
                .requestListCookieGetString(myTokenProperties.getJWT_COOKIE_NAME(),request);

        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

        Optional<CommonReplyModel> findReplyModel = commonReplyRepsitory.findById(replyId);


        // 관리자인지 유저인지를 파악하기위해
        ResponseEntity<UserModelFront> responseFindUserModelFront = auctionUserInterFace
                .findUserModelFront(myTokenProperties.getJWT_COOKIE_NAME()+"="+myCookie,(Integer)returnMapUserData.get(2));


        if(Objects.requireNonNull(responseFindUserModelFront.getBody()).getUserName() == null){
            return -3; // 유저가 없습니다.
        }


        //댓글이 있다면
        if(findReplyModel.isPresent()){

            // 작성자인지를 비교, 혹은 관리자라면
            if((Integer)returnMapUserData.get(2) == findReplyModel.get().getUserId() ||
                    responseFindUserModelFront.getBody().getRole().equals("ADMIN")){
                // 먼저 해당 대댓글 모두 삭제
                replyofReplyRepository.deleteAllByCommonReplyId(findReplyModel.get().getId());

                // 댓글 삭제 진행
                commonReplyRepsitory.delete(findReplyModel.get());
                return 1;
            }

        }

        return -1;
    }

    @Transactional
    public int saveReplyOfReply(String content,
                           int userId,
                           String nickName,
                           String userPicture,
                           int commonModelId,
                           int replyId,
                           HttpServletRequest request){
        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

        // 게시판 찾기
        Optional<CommonModel> findCommonModel =  commonModelRepository.findById(commonModelId);

        // 대댓글을 달아줄 댓글을 검색
        Optional<CommonReplyModel> findCommonReplyModel = commonReplyRepsitory.findById(replyId);

        // 둘다 있어야함
        if(findCommonModel.isPresent() && findCommonReplyModel.isPresent()){
            CommonReplyOfReplyModel commonReplyOfReplyModel
                    = CommonReplyOfReplyModel.builder()
                    .content(content)
                    .userId(userId)
                    .nickName(nickName)
                    .userPicture(userPicture)
                    .commonModelId(findCommonModel.get().getId())
                    .commonReplyId(findCommonReplyModel.get().getId())
                    .build();

            replyofReplyRepository.save(commonReplyOfReplyModel);
            log.info("success save reply of reply");
            return 1;
        }

        log.info("fail save reply of reply");
        return -1;
    }

    @Transactional
    public int deleteReplyOfReply(int replyOfReplyId, HttpServletRequest request){

        Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

        String token = cookieJWTUtil
                .requestListCookieGetString(myTokenProperties.getJWT_COOKIE_NAME(),request);

        Optional<CommonReplyOfReplyModel> findReplyofReplyModel = replyofReplyRepository.findById(replyOfReplyId);


        // 관리자인지 유저인지를 파악하기위해
        ResponseEntity<UserModelFront> responseFindUserModelFront = auctionUserInterFace
                .findUserModelFront(myTokenProperties.getJWT_COOKIE_NAME()+"="+token,(Integer)returnMapUserData.get(2));


        if(Objects.requireNonNull(responseFindUserModelFront.getBody()).getUserName() == null){
            return -3; // 유저가 없습니다.
        }


        //대댓글이 있다면
        if(findReplyofReplyModel.isPresent()){

            // 작성자인지를 비교, 혹은 관리자라면
            if((Integer)returnMapUserData.get(2) == findReplyofReplyModel.get().getUserId() ||
                    responseFindUserModelFront.getBody().getRole().equals("ADMIN")){

                // 대댓글 삭제 진행
                replyofReplyRepository.delete(findReplyofReplyModel.get());
                return 1;
            }

        }

        return -1;
    }

}
