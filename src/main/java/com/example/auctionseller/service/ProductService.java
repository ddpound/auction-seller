package com.example.auctionseller.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionseller.model.ProductModel;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.ProductModelRepository;
import com.example.auctionseller.repository.ShoppingMallModelRepositry;
import com.example.auctionseller.sellercommon.ReturnTokenUsername;
import com.example.auctionseller.userinterface.AuctionUserInterFace;
import com.example.modulecommon.enums.AuthNames;
import com.example.modulecommon.frontModel.UserModelFront;
import com.example.modulecommon.jwtutil.JWTUtil;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final JWTUtil jwtUtil;

    private final ReturnTokenUsername returnTokenUsername;

    private final ProductModelRepository productModelRepository;

    private final AuctionUserInterFace auctionUserInterFace;

    @Transactional
    public int saveProduct(Integer ProductID,
                           String productName,
                           int productPrice,
                           int productquantity,
                           String content,
                           List<MultipartFile> fileList,
                           HttpServletRequest request,
                           boolean modify) {

        Map returnmap = returnTokenUsername.tokenGetUsername(request);


        StringBuilder productFilePath = new StringBuilder();
        StringBuilder productUrlPath = new StringBuilder();

        // 주의 반드시 옮기고 난다음에 content를 수정할것
        Map<Integer, String> makeFileResult = null;

        if(!modify){
            makeFileResult = makeFile.saveMoveImageFiles((Integer) returnmap.get(2), content, AuthNames.Seller,"");
        }

        // 사진을 옮긴후 content 내용의 url경로도 변경
        String changecontent = "";


        // 수정일땐 썸네일이 있어도 되고 없어도 됨
        // 하지만 처음 저장 할때는 썸네일이 반드시 필요함


        // 수정이 아니고, 리스트가 0일때
        // 썸네일이 없으니깐 -5를 반환
        if(fileList.size() ==0 && !modify){
            return -5;
        }



        // 파일저장 (make file)
        // 1. url 사진 경로
        // 2. 컴퓨터 사진 파일 경로

        // 썸네일이 있을 때
        if (fileList.size() > 0) {
            if (fileList.size() > 3) {
                return -4; // 3개 이상의 썸네일
            }

            Map<Integer, String> returnPathName = new HashMap<>();

            // 썸네일 저장하기
            for (MultipartFile file : fileList
            ) {
                returnPathName = makeFile.makeFileImage((Integer) returnmap.get(2), file, request);

                productUrlPath.append(returnPathName.get(1)).append(",");
                productFilePath.append(returnPathName.get(2)).append(",");
            }
        }

        // 수정이 참이면서
        // ID가 널이 아닐때, 수정을 진행
        if (modify && ProductID != null) {

            // 수정이니 이미 제품이 있으니 검사를 시도
            // 동시에 영속화
            Optional<ProductModel> productModel = productModelRepository.findById(ProductID);

            makeFileResult = makeFile.saveMoveImageFiles((Integer) returnmap.get(2), content, AuthNames.Seller,productModel.get().getFilefolderPath());

            if (makeFileResult.get(1) == "-3") {
                return -3; // 사진 10을 넘겨버림
            }

            // 사진을 옮긴후 content 내용의 url경로도 변경
            changecontent = makeFile.changeContentImageUrlPath((Integer) returnmap.get(2), content, makeFileResult.get(2), request);

            // 받아온 썸네일이 있을때
            if (fileList.size() > 0) {

                // 전에 받은 애들은 삭제해야하니 삭제과정을 거치도록하겠습니다.
                String filepathString = productModel.get().getPictureFilePath();
                List<String> deleteProductFile = List.of(filepathString.substring(0, filepathString.length()-1).split(","));

                for (String i:deleteProductFile
                ) {

                    // i는 각각의 경로 그 경로를 받아와 삭제
                    makeFile.filePathImageDelete(i);
                }

                productModel.get().setPictureUrlPath(productUrlPath.toString());
                productModel.get().setPictureFilePath(productFilePath.toString());
            }

            productModel.get().setProductName(productName);
            productModel.get().setProductQuantity(productquantity);
            productModel.get().setProductPrice(productPrice);
            productModel.get().setContent(changecontent);

            // 수정 진행하고 끝내버리자
            // 문제 없다면 반환 1, 여기까지왔다면 임시파일 삭제


            //수정 진행후 사용하지 않는 이미지 삭제
            makeFile.modifyImageFile(productModel.get().getFilefolderPath(),changecontent);
            // 임시파일 삭제
            makeFile.deleteTemporary((Integer) returnmap.get(2));
            return 1;
        }



        // 영속화
        ShoppingMallModel shoppingMallModel = shoppingMallModelRepositry.findByUsername((String) returnmap.get(1));

        if (makeFileResult.get(1) == "-3") {
            return -3; // 사진 10을 넘겨버림
        }

        // 사진을 옮긴후 content 내용의 url경로도 변경
        changecontent = makeFile.changeContentImageUrlPath((Integer) returnmap.get(2), content, makeFileResult.get(2), request);


        makeFile.deleteTemporary((Integer) returnmap.get(2));


        ProductModel productModel = ProductModel.builder()
                .productName(productName)
                .productPrice(productPrice)
                .productQuantity(productquantity)
                .pictureFilePath(productFilePath.toString())
                .content(changecontent)
                .filefolderPath(makeFileResult.get(2))
                .pictureUrlPath(productUrlPath.toString())
                .shoppingMall(shoppingMallModel)
                .build();

        try {
            productModelRepository.save(productModel);

        } catch (Exception e) {
            log.info(e);
            return -1; // 단순 에러
        }


        // 문제 없다면 반환 1, 여기까지왔다면 임시파일 삭제
        // 임시파일 삭제
        makeFile.deleteTemporary((Integer) returnmap.get(2));
        return 1;
    }

    public Optional<ProductModel> findProduct(int id){
        return productModelRepository.findById(id);
    }

    @Transactional
    public int deleteProduct(int productId, HttpServletRequest request){


        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtRHeader = request.getHeader("RefreshToken");

        String token = jwtHeader.replace("Bearer ", "");
        String reToken = jwtHeader.replace("Bearer ", "");

        DecodedJWT decodedJWT = JWT.decode(token);

        ResponseEntity<UserModelFront> findFrontUserModel = auctionUserInterFace
                .findUserModelFront(jwtHeader,jwtRHeader,decodedJWT.getClaim("userId").asInt());

        // 수정이니 이미 제품이 있으니 검사를 시도
        // 동시에 영속화
        Optional<ProductModel> productModel = productModelRepository.findById(productId);

        // 삭제하려는 유저와 제품의 유저가 달라 -2를 반환
        // 리스트의 길이가 3이 아닐때 즉 어드민이 아니면서 해당 사용자의 제품이 아니라면
        if(productModel.get().getShoppingMall().getUsername().equals(findFrontUserModel.getBody().getUserName())
                && findFrontUserModel.getBody().getRole().equals("ADMIN")){
            return -2;
        }

        // 썸네일이 있을 때
        if(productModel.get().getPictureFilePath().length() >0){
            // 썸네일 파일 삭제
            String filepathString = productModel.get().getPictureFilePath();
            List<String> deleteProductFile = List.of(filepathString.substring(0, filepathString.length()-1).split(","));

            for (String i:deleteProductFile
            ) {
                // i는 각각의 경로 그 경로를 받아와 삭제
                makeFile.filePathImageDelete(i);
            }

        }

        // 사진을 담아놓는 폴더 경로를 통해 파일 삭제
        if(productModel.get().getFilefolderPath().length() >0){
            makeFile.folderPathImageDelete(productModel.get().getFilefolderPath());
        }

        //마지막 단계에 삭제
        productModelRepository.delete(productModel.get());
        return 1;
    }

}
