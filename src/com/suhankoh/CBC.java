package com.suhankoh;

import java.util.ArrayList;

/**
 * Created by Suhan on 10/13/15.
 */
public class CBC {

//            static String plainText = "Lorem ipsum dolor sit amet, consectetur ";
    static String plainText = "qwerqweree";
//    static String plainText = "qwerqw";

    public static final String charset = "UTF8";
    static byte[] IV;
    DESLibrary des;
    ArrayList<byte[]> cipherBlocks;
    String[] cipherTextBlocks;

    public static void main(String[] args) throws Exception {
        IV = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        String secretKey = "QWERTYUI";
        CBC cbc = new CBC(secretKey);
        cbc.performEncryption();
        cbc.performDecryption();
    }

    public CBC(String secretKey) throws Exception {
        des = new DESLibrary(secretKey, "DES");
        cipherBlocks = new ArrayList<>();
    }

    public void performEncryption() throws Exception {
        byte[] tempIV = IV;
        byte[] plainTextByte = plainText.getBytes();
        byte[] msg = new byte[8];
        int blockCounter = (int) plainTextByte.length / 8;
        int paddingCounter = (plainTextByte.length % 8);
        int min = 0;
        if (plainTextByte.length - paddingCounter <= 0) {
            min = plainTextByte.length;
        } else {
            min = plainTextByte.length - paddingCounter;
        }
        if (paddingCounter > 0) {
            blockCounter++;
        }
//        System.out.println("Block is : " + blockCounter + " padding: " + paddingCounter + " textlength: " + plainTextByte.length);
        cipherTextBlocks = new String[blockCounter];
        int blocks = 0;
        for (int i = 0; i < min; i++) {
            msg[i % 8] = (plainTextByte[i]);
            if ((i + 1) == min) {
                System.out.println("i  is " + i);
                for (int j = (i % 8) + 1; j < 8; j++) {
                    System.out.println("j is " + j);
                    msg[j % 8] = (byte) '\0';
                }
            }
            if ((((i + 1) % 8 == 0) || (i >= min-1))&& i > 0) {
                byte[] xor = xor(msg, tempIV);
                cipherBlocks.add(des.encrypt(xor));
                tempIV = cipherBlocks.get(blocks);
                cipherTextBlocks[blocks] = new String(cipherBlocks.get(blocks));
                blocks++;
            }
        }

        for (int i = 0; i < cipherTextBlocks.length; i++) {
            System.out.println(cipherTextBlocks[i]);
        }
    }

    public void performDecryption() throws Exception {
        System.out.println("\n\n");
        byte[] tempIV = IV;
        for (int i = 0; i < cipherBlocks.size(); i++) {
//            System.out.println("Length is : " + cipherBlocks.get(i).length + " And tempIV: " + tempIV.length);
            //byte[] cipherbyte = cipherTextBlocks[i].getBytes();
            byte[] plaintext = des.decrypt(cipherBlocks.get(i));
            byte[] xor = xor(plaintext, tempIV);
            tempIV = cipherBlocks.get(i);
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
            //System.out.println("Input: " + (int) input[i] + "  IV: " + (int) IV[i] + "  and the result is: " + (int) result[i]);
        }
        return result;
    }
}
