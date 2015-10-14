package com.suhankoh;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Suhan on 10/13/15.
 */
public class CBC {

    static String plainText = "Lorem ipsum dolor sit amet, consectetur ";
    public static final String charset = "UTF8";
    static byte[] IV;
    DESLibrary des;
    byte[][] cipherBytes;
    String[] cipherText;
    public static void main(String[] args) throws Exception {
        IV = new byte[8];
        new SecureRandom().nextBytes(IV);

        String encryptOrDecrypt = "encrypt";
        String secretKey = "QWERTYUI";
        CBC cbc = new CBC(secretKey);
        cbc.performEncryption();
        cbc.performDecryption();
    }

    public CBC(String secretKey) throws Exception{
        des = new DESLibrary(secretKey, "DES");
        cipherBytes = new byte[5][8];
    }
    public void performEncryption() throws Exception{
        byte[] plainTextByte = plainText.getBytes();
        byte[] msg = new byte[8];
        cipherText = new String[plainTextByte.length/8];
        int blocks = 0;
        for(int i = 0; i < plainTextByte.length; i++) {
            if (i%9 == 0) {
                System.out.println(msg.length + "length msg");
                byte[] xor = xor(msg, IV);
                cipherBytes[blocks] = des.encrypt(xor);
                IV = cipherBytes[blocks];
                cipherText[blocks] = new String(cipherBytes[blocks]);
                blocks++;
            }
            msg[i%8] = plainTextByte[i];
        }
        for(int i = 0; i< cipherText.length; i++) {
            System.out.println(cipherText[i]);
        }
    }

    public void performDecryption() throws Exception {
        for(int i = 0; i < cipherBytes.length; i++ ) {
            //byte[] cipherbyte = cipherText[i].getBytes();
            byte[] plaintext = des.decrypt(cipherBytes[i]);
            byte[] xor = xor(plaintext, IV);
            IV = cipherBytes[i];
            System.out.println(new String(xor));
        }
    }

    /**
     * Take an input byte array, and IV byte array, and will perform XOR on it, and return an array of byte.
     *
     * @param input byte array of the input
     * @param IV
     * @return
     */
    public byte[] xor(byte[] input, byte[] IV) {
        //System.out.println(input.length + "  " + IV.length);
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) ((int) input[i] ^ (int) IV[i]);
        }
        return result;
    }
}

/*
    public void performCBCDecrypt(String function, String secretKey, File file) throws IOException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        PrintWriter writer = new PrintWriter("test3.txt");
        byte[] bFile = new byte[8]; //8 bytes = 64 bits
        FileInputStream is = new FileInputStream(file);
        int readSize = 0;
        byte[] xor;
        byte[] crypt = new byte[8];
        String result = "";
        byte[] fakeIV = IV;
        while ((readSize = is.read(bFile, 0, bFile.length)) != -1) {
            System.out.println("testde");
            //padding
            if (readSize < 8) {
                for(int i = readSize; i < 8; i++) {
                    System.out.println("padd2");
                    bFile[i] = (byte)'\0';
                }
            }
                crypt = des.decrypt(bFile);
                xor = xor(crypt, fakeIV);
                fakeIV = crypt;
                result = new String(xor, charset);


            writer.write(result);
        }
        writer.close();

    }
    public void performCBCEncrypt(String function, String secretKey, File file) {
        try {
            FileOutputStream writer = new FileOutputStream("test2.txt");

            byte[] bFile = new byte[8]; //8 bytes = 64 bits
            FileInputStream is = new FileInputStream(file);
            int readSize = 0;
            byte[] xor;
            byte[] crypt = new byte[8];
            byte[] fakeIV = IV;
            int offset = 0;
            String result = "";
            while ((readSize = is.read(bFile, 0, bFile.length)) != -1) {
                System.out.println("test");
                //padding
                if (readSize < 8) {
                    for(int i = readSize; i < 8; i++) {
                        System.out.println("padd");
                        bFile[i] = (byte)'\0';
                    }
                }
                if(function.equals("encrypt")) {
                    xor = xor(bFile, fakeIV);
                    crypt = des.encrypt(xor);
                    fakeIV = crypt;
//                    result = new String(crypt, charset);
                }

                writer.write(crypt);
            }
            writer.close();

        } catch (InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

    }

 */