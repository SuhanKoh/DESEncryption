package com.suhankoh;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DESLibrary {

    private SecretKeySpec secretKey;
    private Cipher cipher;
    private Cipher decipher;

    public DESLibrary(String keys, String algorithm) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, UnsupportedEncodingException {
        byte[] test = keys.getBytes();
        secretKey = new SecretKeySpec(test, algorithm);

        cipher = Cipher.getInstance(algorithm);
        decipher = Cipher.getInstance(algorithm);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        decipher.init(Cipher.DECRYPT_MODE, secretKey);

    }

    public byte[] encrypt(byte[] bytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return cipher.doFinal(bytes);
    }

    public byte[] decrypt(byte[] bytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return decipher.doFinal(bytes);
    }
}
