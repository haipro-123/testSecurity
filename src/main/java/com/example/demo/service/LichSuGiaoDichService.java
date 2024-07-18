package com.example.demo.service;

import com.example.demo.dto.LichSuGiaoDichDTO;
import com.example.demo.entity.LichSuGiaoDich;
import com.example.demo.exception.IllegalArgumentMyFunctionException;
import com.example.demo.repository.LichSuGiaoDichRepository;
import com.example.demo.util.AesUtil;
import com.example.demo.util.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Objects;

@Service
public class LichSuGiaoDichService {
    @Autowired
    private LichSuGiaoDichRepository lichSuGiaoDichRepository;
    @Autowired
    private AesUtil aesUtil;
    @Autowired
    private RsaUtil rsaUtil;
    @Value("${security.aes.key}")
    private static String AES_KEY_TEXT;
    private static final String RSA_PRIVATE_KEY_TEXT = """
            -----BEGIN RSA PRIVATE KEY-----
            MIICXgIBAAKBgQDi1MHuMVZUQTd4Kcly+EppjwcDfz4lzKdwzsI8Ucq243CU3Kze
            ljeA9A5O2HUIRSw6HN4F9ejfYDx99kqASBDPWmK+MkNFrgfInIgOof7JNAU1Nzqa
            I8XZ+yfrSjTAoyozZRQZLFzpehx/Okeeh5iMmzQrBs7EzltWY3O6OxE8QwIDAQAB
            AoGBAN/oDJbHdOQujEa9WPF4Tlvsp4u4Kuy9G/uq3OkWA/iMgjEvtCT0O027m/QQ
            j1Xekk26+R8BIyJ8qLneSKRgftWTUny0utzKsAZr9+y9Lk/j9E0dS5Lji6GHvgcK
            saj4PxjsiLHrmE0VBIMc2AjHO66DJQc2YBWpjpf9MQnYMJwBAkEA//3rj/StWrBn
            As0W2EYhPcm4xh64NOgasE9g69tWBmMHx8dINct9N7fkZBYjjpL1ou9qQubmrkSj
            Grb+fT13wwJBAOLWmbdwsz6J+1CDO1NHHIxWK1gk43C3U4xAnri6r6Jtu5RNiOCe
            19O2G2i5bY2CraAipfgo2E9GAbN7ohP9YYECQQCrTPBn6XRjnm2gOztRSESQQz9p
            HD9p7/OEDeouihybs4MOVbVlgiDtuxmTPBlZG9BR0uIJmNe+v+FhTBkqF4rfAkAg
            0b/HxKyKXdhYm8QXlnBQ9Z6r0BqAEmYqIqdUPt5ud8Xt/RHSveioHu70Re/Ny5xn
            UNwGfZJeVdilKTwX/E8BAkEAtaek+VMyuIocYE4AcsF5BIMzqm1hJHIeeKi+n0uG
            pvNy0sjBewjkcJ37Sh/uh5DJOOTlrVgVQP352+wRSp72dQ==
            -----END RSA PRIVATE KEY-----
            """;
    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyString = RSA_PRIVATE_KEY_TEXT
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    private SecretKey getAESKey() throws NoSuchAlgorithmException {
        byte[] keyData = AES_KEY_TEXT.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyData,"AES");
        return secretKey;
    }

    public void add(LichSuGiaoDichDTO lichSuGiaoDichDTO) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, InvalidKeySpecException, ParseException {
        if(Objects.isNull(lichSuGiaoDichDTO)){
            throw new IllegalArgumentMyFunctionException();
        }
        PrivateKey privateKey = getPrivateKey();
        String account = rsaUtil.decryptData(lichSuGiaoDichDTO.getAccount(),privateKey);
        String accountEncrypted = aesUtil.encryptData(account,getAESKey());
        String transactionID = rsaUtil.decryptData(lichSuGiaoDichDTO.getTransactionID(),privateKey);
        Double inDent = Double.parseDouble(rsaUtil.decryptData(lichSuGiaoDichDTO.getInDent(),privateKey));
        Integer have = Integer.parseInt(rsaUtil.decryptData(lichSuGiaoDichDTO.getHave(),privateKey));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date time = formatter.parse(rsaUtil.decryptData(lichSuGiaoDichDTO.getTime(),privateKey));
        LichSuGiaoDich lichSuGiaoDich = new LichSuGiaoDich();
        lichSuGiaoDich.setAccount(accountEncrypted);
        lichSuGiaoDich.setTransactionID(transactionID);
        lichSuGiaoDich.setInDent(inDent);
        lichSuGiaoDich.setHave(have);
        lichSuGiaoDich.setTime(new Date(time.getTime()));
        lichSuGiaoDichRepository.save(lichSuGiaoDich);
    }
}
