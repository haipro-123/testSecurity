package com.example.demo.util;

import com.example.demo.exception.IllegalArgumentMyFunctionException;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Component
public class AesUtil {
    private final static int AES_KEY_SIZE = 256;
    private final static String AES_ALGORITHM = "AES";
    public String generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGen.init(AES_KEY_SIZE);
        return Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
    }
    public String encryptData(String data,SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if(Objects.isNull(data)||Objects.isNull(key)){
            throw new IllegalArgumentMyFunctionException("data or key is null");
        }
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    public String decryptData(String encryptedText,SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if(Objects.isNull(encryptedText)||Objects.isNull(key)){
            throw new IllegalArgumentMyFunctionException("ecryptedText or key is null");
        }
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedData);
    }
}
