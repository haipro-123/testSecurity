package com.example.demo.service;

import com.example.demo.dto.request.LichSuGiaoDichDTO;
import com.example.demo.entity.LichSuGiaoDich;
import com.example.demo.exception.IllegalArgumentMyFunctionException;
import com.example.demo.repository.LichSuGiaoDichRepository;
import com.example.demo.util.AesUtil;
import com.example.demo.util.Constant;
import com.example.demo.util.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
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
    private String AES_KEY_TEXT;
    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyString = Constant.RSA_PRIVATE_KEY_TEXT
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    private SecretKey getAESKey() throws NoSuchAlgorithmException {
        byte[] keyData = Base64.getDecoder().decode(AES_KEY_TEXT);
        SecretKey secretKey = new SecretKeySpec(keyData,0,keyData.length,"AES");
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
