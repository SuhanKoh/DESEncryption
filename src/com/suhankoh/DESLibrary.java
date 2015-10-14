package com.suhankoh;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DESLibrary {

    private SecretKeySpec secretKey;
    Cipher cipher;
    Cipher decipher;
    public DESLibrary(String keys, String algorithm) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, UnsupportedEncodingException {
        byte[] test = {1,2,3,4,5,6,7,8};
        byte[] keyBytes = new byte[8];
        secretKey = new SecretKeySpec(test, "DES");

//        secretKey = new SecretKeySpec(test, algorithm);

        cipher = Cipher.getInstance("DES");
        decipher = Cipher.getInstance("DES");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        decipher.init(Cipher.DECRYPT_MODE, secretKey);

    }

    public byte[] encrypt(byte[] bytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return cipher.doFinal(bytes);
    }

    public byte[] decrypt(byte[] bytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        System.out.println(bytes.length);

        byte[] test = decipher.doFinal(bytes);
        return test;
    }

}
