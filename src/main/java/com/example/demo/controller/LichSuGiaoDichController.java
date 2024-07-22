package com.example.demo.controller;

import com.example.demo.dto.request.LichSuGiaoDichDTO;
import com.example.demo.entity.LichSuGiaoDich;
import com.example.demo.service.LichSuGiaoDichService;
import com.example.demo.util.AesUtil;
import com.example.demo.util.Constant;
import com.example.demo.util.RsaUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;

@RestController
public class LichSuGiaoDichController {
    @Autowired
    private LichSuGiaoDichService lichSuGiaoDichService;
    @Autowired
    private RsaUtil rsaUtil;
    @Autowired
    private AesUtil aesUtil;
    @PostMapping("")
    public ResponseEntity<?> add(@NotNull @RequestBody LichSuGiaoDich lichSuGiaoDich) throws BadPaddingException, ParseException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        PublicKey publicKey = getPublicKey();
        String transactionId = rsaUtil.ecryptData(lichSuGiaoDich.getTransactionID(),publicKey);
        String account = rsaUtil.ecryptData(lichSuGiaoDich.getAccount(),publicKey);
        String inDent = rsaUtil.ecryptData(lichSuGiaoDich.getInDent().toString(),publicKey);
        String have = rsaUtil.ecryptData(lichSuGiaoDich.getHave().toString(),publicKey);
        String time = rsaUtil.ecryptData(lichSuGiaoDich.getTime().toString(),publicKey);
        LichSuGiaoDichDTO lichSuGiaoDichDTO = new LichSuGiaoDichDTO(transactionId,account,inDent,have,time);
        System.out.println(lichSuGiaoDichDTO.toString());
        lichSuGiaoDichService.add(lichSuGiaoDichDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyString = Constant.RSA_PUBLIC_KEY_TEXT
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        return publicKey;
    }
}
