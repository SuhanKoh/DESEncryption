package com.suhankoh;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Suhan on 10/13/15.
 */
public class CBC {
    public static void main(String[] args) {
        if (args.length >= 3) {
            String encryptOrDecrypt = args[0];
            String secretKey = args[1];
            File file = new File(args[2]);
            CBC cbc = new CBC();
            cbc.performCBC(encryptOrDecrypt, secretKey, file);
        }
    }

    public void performCBC(String function, String secretKey, File file) {
        try {
            FileOutputStream writer = new FileOutputStream("test2.txt");

            DESLibrary des = new DESLibrary(secretKey, "DES");
            byte[] bFile = new byte[8]; //8 bytes = 64 bits
            FileInputStream is = new FileInputStream(file);
            int readSize = 0;

            int offset = 0;
            byte[] desBytes = new byte[8];
            byte[] desBytes2 = new byte[8];
            byte[] xorResult = new byte[8];
            byte[] IV = new byte[]{1, 2, 3, 4, 1, 2, 3, 4, 1};
            while ((readSize = is.read(bFile, 0, bFile.length)) != -1) {

                xorResult = xor(bFile, IV, readSize);//perform xor
                desBytes = des.encrypt(bFile); //encrypt

                writer.write(xorResult, 0, readSize);
                offset += readSize;
            }


        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }

    /**
     * Take an input byte array, and IV byte array, and will perform XOR on it, and return an array of byte.
     * @param input byte array of the input
     * @param IV
     * @param readSize
     * @return
     */
    public byte[] xor(byte[] input, byte[] IV, int readSize) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < readSize; i++) {
            result[i] = (byte) (input[i] ^ IV[i]);
        }
        return result;
    }

    public void useless() {
        byte[] b1 = "TEST".getBytes();
        byte[] b2 = "keys".getBytes();
        byte[] b3 = new byte[b1.length];
        byte[] b4 = new byte[b1.length];

        int i = 0;
        System.out.println(new String(b1, StandardCharsets.UTF_8) + " " + new String(b2, StandardCharsets.UTF_8) + "\nBytes" + b1.length + " AND " + b2.length);
        for (byte bytes : b1)
            b3[i] = (byte) (bytes ^ b2[i++]);
        System.out.println(new String(b3, StandardCharsets.UTF_8));
        i = 0;

        for (byte bytes : b3)
            b4[i] = (byte) (bytes ^ b2[i++]);

        System.out.println(new String(b4, StandardCharsets.UTF_8) + " AND " + new String(b1, StandardCharsets.UTF_8));

    }

}
