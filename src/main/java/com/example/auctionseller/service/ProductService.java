package com.example.auctionseller.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auctionseller.model.ProductModel;
import com.example.auctionseller.model.ProductOption;
import com.example.auctionseller.model.ShoppingMallModel;
import com.example.auctionseller.repository.*;
import com.example.auctionseller.sellercommon.SellerReturnTokenUsername;
import com.example.auctionseller.userinterface.AuctionUserInterFace;
import com.example.modulecommon.allstatic.MyTokenProperties;
import com.example.modulecommon.enums.AuthNames;
import com.example.modulecommon.frontModel.UserModelFront;
import com.example.modulecommon.jwtutil.CookieJWTUtil;
import com.example.modulecommon.jwtutil.JWTUtil;
import com.example.modulecommon.makefile.MakeFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ShoppingMallModelRepositry shoppingMallModelRepositry;

    private final MakeFile makeFile;

    private final SellerReturnTokenUsername sellerReturnTokenUsername;

    private final ProductModelRepository productModelRepository;

    private final AuctionUserInterFace auctionUserInterFace;

    private final ProductOptionRepositry productOptionRepositry;

    private final ReservationRepository reservationRepository;

    private final ReservationOptionRepository reservationOptionRepository;

    private final CookieJWTUtil cookieJWTUtil;

    private final MyTokenProperties myTokenProperties;

    @Transactional
    public int saveProduct(Integer ProductID,
                           String productName,
                           int productPrice,
                           int productquantity,
                           String content,
                           JSONArray optionList,
                           List<MultipartFile> fileList,
                           HttpServletRequest request,
                           boolean modify) {

        Map returnmap = sellerReturnTokenUsername.tokenGetUsername(request);


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

            List<ProductOption> findProductOption = productOptionRepositry.findAllByProductId(ProductID);

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

            List<ProductOption> productOptions = new ArrayList<>();

            // 수정할 옵션이 있을 경우
            if(optionList.size() >0){

                // 기존에 있던 모든 애들 전부 삭제후 추가해주기
                productOptionRepositry.deleteAll(findProductOption);

                JSONObject jsonObject;
                for (int i=0; i < optionList.size();i++){
                    jsonObject = (JSONObject) optionList.get(i);

                    ProductOption productOption = ProductOption
                            .builder()
                            .productId(productModel.get().getId())
                            .optionTitle(jsonObject.get("optionTitle").toString())
                            .detailedDescription(jsonObject.get("detailedDescription").toString())
                            .build();
                    productOptionRepositry.save(productOption);
                }

            }


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
            int saveProductId = productModelRepository.save(productModel).getId();


            // 저장할 옵션이 있을 경우
            if(optionList.size() >0){

                JSONObject jsonObject;

                for (int i=0; i < optionList.size();i++){
                    jsonObject = (JSONObject) optionList.get(i);

                    ProductOption productOption = ProductOption
                            .builder()
                            .productId(saveProductId)
                            .optionTitle(jsonObject.get("optionTitle").toString())
                            .detailedDescription(jsonObject.get("detailedDescription").toString())
                            .build();
                    productOptionRepositry.save(productOption);
                }

            }
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

    // 제품을 삭제하면 구매 내역도 전부 삭제해야함
    @Transactional
    public int deleteProduct(int productId, HttpServletRequest request){

        try {
            log.info("try product delete, product Id : " + productId);
            Map<Integer, Object> returnMapUserData = sellerReturnTokenUsername.tokenGetUsername(request);

            String token = cookieJWTUtil
                    .requestListCookieGetString(myTokenProperties.getJWT_COOKIE_NAME(),request);

            log.info("check userId : " + returnMapUserData.get(2));

            ResponseEntity<UserModelFront> findFrontUserModel = auctionUserInterFace
                    .findUserModelFront(myTokenProperties.getJWT_COOKIE_NAME()+"="+token,(Integer) returnMapUserData.get(2));

            log.info("check find user application : " + Objects.requireNonNull(findFrontUserModel.getBody()).getUserName());
            // 수정이니 이미 제품이 있으니 검사를 시도
            // 동시에 영속화
            Optional<ProductModel> productModel = productModelRepository.findById(productId);

            if(productModel.isEmpty()){
                log.info("not found Product, found try id :" + productId);
                return -3; // 값이 없어요
            }

            // 삭제하려는 유저와 제품의 유저가 달라 -2를 반환
            // 리스트의 길이가 3이 아닐때 즉 어드민이 아니면서 해당 사용자의 제품이 아니라면
            if(productModel.get().getShoppingMall().getUsername().equals(findFrontUserModel.getBody().getUserName())
                    && findFrontUserModel.getBody().getRole().equals("ADMIN")){
                log.info("seller not same");
                log.info("this product seller : " + productModel.get().getShoppingMall().getUsername());
                log.info("try seller : " + findFrontUserModel.getBody().getUserName());
                return -2;
            }

            // 썸네일이 있을 때
            if(productModel.get().getPictureFilePath().length() >0){
                log.info("thumbnail delete try, product board");

                // 썸네일 파일 삭제
                String filepathString = productModel.get().getPictureFilePath();
                List<String> deleteProductFile = List.of(filepathString.substring(0, filepathString.length()-1).split(","));
                log.info("thumbnail delete try, product board");

                log.info("thumbnail tracking try, " + deleteProductFile.listIterator());

                for (String i : deleteProductFile
                     ) {
                    log.info("thumbnail tracking try : " + i);
                }

                for (String i:deleteProductFile
                ) {
                    // i는 각각의 경로 그 경로를 받아와 삭제
                    makeFile.filePathImageDelete(i);
                }
                log.info("thumbnail delete success, product board");
            }

            // 사진을 담아놓는 폴더 경로를 통해 파일 삭제
            if(productModel.get().getFilefolderPath().length() >0){
                makeFile.folderPathImageDelete(productModel.get().getFilefolderPath());
                log.info("success image delete folder");
            }


            reservationOptionRepository.deleteAllByProductId(productModel.get().getId());
            reservationRepository.deleteAllByProductId(productModel.get());

            //마지막 단계에 삭제
            productModelRepository.delete(productModel.get());

            log.info("success delete product");
            return 1;

        }catch (Exception e){
            e.printStackTrace();

            return -9; // 알수없는 에러
        }

    }

}
