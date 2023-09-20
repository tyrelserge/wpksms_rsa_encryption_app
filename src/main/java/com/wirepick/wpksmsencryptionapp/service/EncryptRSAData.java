package com.wirepick.wpksmsencryptionapp.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirepick.wpksmsencryptionapp.model.EncMsg;
import com.wirepick.wpksmsencryptionapp.model.RSAPublicKey;

@Service
public class EncryptRSAData {

    // public final static Path rootPath = Paths.get("src/main/resources");
    static ClassPathResource resource = new ClassPathResource("/rsakeys/pubkey.pem");
    private static ObjectMapper mapper = new ObjectMapper();

    public static String updatePublicKey(RSAPublicKey publicKeyBase64String) {

        try {
            mapper.writeValue(resource.getFile(), publicKeyBase64String);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "UPDATED";
    }

    public static EncMsg encryptContentMessage(String message) throws Exception {

        InputStream inputstream;
        RSAPublicKey rsaPublicKey = null;

        try {
            // inputstream = new FileInputStream(rootPath + "/rsakeys/pubkey.pem");
            inputstream = new FileInputStream(resource.getFile());
            TypeReference<RSAPublicKey> typereference = new TypeReference<RSAPublicKey>() {};
            rsaPublicKey = mapper.readValue(inputstream, typereference);
        
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(rsaPublicKey==null)
            return null;

        String publicKeyBase64String = rsaPublicKey.getPublicKeyBase64String();
        
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyBase64String);
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        
        return new EncMsg(Base64.getEncoder().encodeToString(encryptedBytes));
    }

}
