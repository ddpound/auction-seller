package com.example.auctionseller.service;

import com.auth0.jwt.JWT;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.ProductModelRepository;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.auctionseller.sellercommon.SellerReturnTokenUsername;
import com.example.modulecommon.jwtutil.JWTUtil;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShoppingMallService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final JWTUtil jwtUtil;

    private final SellerReturnTokenUsername sellerReturnTokenUsername;

    private final ProductModelRepository productModelRepository;

    /**
     * 새로운 쇼핑몰을 만드는 서비스단
     * @param shoppingmallName 쇼핑몰 이름
     * @param multipartFile MultipartFile 자료형의 파라미터
     * @param shoppingMallExplanation 쇼핑몰 설명
     * @param request 꼭 넣어줘야함, 토큰값 긁어와야함
     * */
    public int SaveNewShoppingMall(MultipartFile multipartFile,
                                   String shoppingmallName,
                                   String shoppingMallExplanation,
                                   HttpServletRequest request) {

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = jwtHeader.replace("Bearer ", "");

        // 따옴표로 저장됨
        String username = JWT.decode(token).getClaim("username").toString().replaceAll("\"", "");;
        int userId = JWT.decode(token).getClaim("userId").asInt();
        // 12길이보다 길면
        if(shoppingmallName.length() > 12){
            return -5;
        }


        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByShoppingMallName(shoppingmallName);

        if(shoppingMallModel != null ){
            // 중복된 쇼핑몰이름
            return -2;
        }

        // 파일저장
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로
        Map<Integer,String> fileNames = makeFile.makeFileImage(userId, multipartFile,request);


        ShoppingMallModel shoppingMallModelSave =
                ShoppingMallModel.builder()
                        .shoppingMallName(shoppingmallName)
                        .shoppingMallExplanation(shoppingMallExplanation)
                        .thumbnailUrlPath(fileNames.get(1))
                        .thumbnailFilePath(fileNames.get(2))
                        .filefolderPath(fileNames.get(3))
                        .username(username)
                        .build();

        shoppingMallModelRepositry.save(shoppingMallModelSave);

        return 1;
    }


    /**
     * 쇼핑몰 수정 메소드 이다
     * 사진이 새로 추가됐다면 해당사진은삭제
     * 새사진이 올라온다
     *
     *  리턴값이 -5면 제목이 12자 이상 이라는뜻
     * */
    @Transactional
    public int modifyShoppingMall(MultipartFile multipartFile,
                                  String shoppingMallName,
                                  String shoppingMallExplanation,
                                  String urlFilePath,
                                  HttpServletRequest request){

        Map<Integer,Object> returnmap = sellerReturnTokenUsername.tokenGetUsername(request);

        // 12길이보다 길면
        if(shoppingMallName.length() > 12){
            return -5;
        }

        // 해당유저가 이미 쇼핑몰이 있다는 가정 하에
        // 영속화
        ShoppingMallModel shoppingMallModel1ByUserModel = shoppingMallModelRepositry.findByUsername((String) returnmap.get(1));

        // 사진이 달라졌을경우 삭제 후 추가
        // 그대로라면 그냥 그대로
        if(shoppingMallModel1ByUserModel == null ) {
            // 해당 유저가 가진 쇼핑몰이 없을 때
            log.info("No shopping mall owned by this user");
            return -3;
        }

        // 비어있다면 아마 새로운 파일을 보낸것일테니 삭제
        if(urlFilePath == null){
            // 새로운 파일을 저장하기 앞서 먼저 삭제해야함 해당 사진은
            // 파일 경로를 불러와 그대로 삭제
            makeFile.folderPathImageDelete(shoppingMallModel1ByUserModel.getFilefolderPath());
            // 새로운 파일저장 입니다.
            // 1. url 사진 경로
            // 2. 컴퓨터 사진 파일 경로
            Map<Integer,String> fileNames =makeFile.makeFileImage((Integer) returnmap.get(2), multipartFile,request);
            shoppingMallModel1ByUserModel.setThumbnailUrlPath(fileNames.get(1));
            shoppingMallModel1ByUserModel.setThumbnailFilePath(fileNames.get(2));
        }

        // 더티체킹
        shoppingMallModel1ByUserModel.setShoppingMallName(shoppingMallName);
        shoppingMallModel1ByUserModel.setShoppingMallExplanation(shoppingMallExplanation);


        return 1;
    }

    /**
     * 쇼핑몰 리스트를 반환해줌
     *
     * */
    @Transactional(readOnly = true)
    public List<ShoppingMallModel> findAllShoppingMallList(){
        return shoppingMallModelRepositry.findAll();
    }

}
