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
    private Cipher decipher;
    private SecretKey mSecretKey;

    public DESLibrary(String keys, String algorithm) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        desKeySpec = new DESKeySpec(keys.getBytes());

        secretKey = SecretKeyFactory.getInstance(algorithm).generateSecret(desKeySpec);
        cipher = Cipher.getInstance(algorithm); // DES/ECB/PKCS5Padding for SunJCE
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
