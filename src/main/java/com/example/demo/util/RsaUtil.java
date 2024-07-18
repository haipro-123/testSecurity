package com.example.demo.util;

import com.example.demo.exception.IllegalArgumentMyFunctionException;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.runtime.ObjectMethods;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Objects;

@Component
public class RsaUtil {
    private static final String RSA_ALGORITHM = "RSA";
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
    public String ecryptData(String data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if(Objects.isNull(data)||Objects.isNull(publicKey)){
            throw new IllegalArgumentMyFunctionException();
        }
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    public String decryptData(String ecryptedText, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        if(Objects.isNull(ecryptedText)||Objects.isNull(privateKey)){
            throw new IllegalArgumentMyFunctionException();
        }
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(ecryptedText));
        return new String(decryptedData);
    }
}
