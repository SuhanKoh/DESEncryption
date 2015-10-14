package com.suhankoh;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DESLibrary {

    private DESKeySpec desKeySpec;
    private SecretKey secretKey;
    private Cipher cipher;

    private SecretKey mSecretKey;

    public DESLibrary(String keys, String algorithm) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        desKeySpec = new DESKeySpec(keys.getBytes());

        secretKey = SecretKeyFactory.getInstance(algorithm).generateSecret(desKeySpec);
        cipher = Cipher.getInstance(algorithm); // DES/ECB/PKCS5Padding for SunJCE

    }

    public byte[] encrypt(byte[] bytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(bytes);
    }

    public byte[] decrypt(byte[] bytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(bytes);
    }

}
