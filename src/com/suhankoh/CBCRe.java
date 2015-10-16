package com.suhankoh;

import java.util.ArrayList;

/**
 * Created by Suhan on 10/15/15.
 */
public class CBCRe {
    String plainText = "";

    public static final String charset = "UTF8";
    byte[] IV;
    DESLibrary des;
    ArrayList<byte[]> cipherBlocks;
    String[] cipherTextBlocks;

    public static void main(String[] args) throws Exception {
        String secretKey = "QWERTYUI";
        CBCRe cbcre = new CBCRe();
        System.out.println("Residue: " + cbcre.CBCRe("qwertyuiasdfghjkzxcvbnm, q", secretKey));
//        cbcre.performDecryption(); //de-comment this to see the result.
    }

    public String CBCRe(String plainText, String secretKey) throws Exception{ //Use this method
        des = new DESLibrary(secretKey, "DES");
        cipherBlocks = new ArrayList<>();
        this.plainText = plainText;
//        this.IV = IV.getBytes();
        return performEncryption();
    }

    public String performEncryption() throws Exception {
        byte[] tempIV = new String("").getBytes(); //NO XOR FOR THE FIRST TIME.
        byte[] plainTextByte = plainText.getBytes();
        byte[] msg = new byte[8];
        int blockCounter = (int) plainTextByte.length / 8; //how many blocks are there for the plaintext
        int paddingCounter = (plainTextByte.length % 8); //how many padding it require
        String residue = "";
        if (paddingCounter > 0) {
            blockCounter++;
        }
        cipherTextBlocks = new String[blockCounter + 1];
        int blocks = 0;
        for (int i = 0; i < blockCounter * 8; i++) {
            if (i < plainTextByte.length) { //avoid msg get replaced when it requires padding
                msg[i % 8] = (plainTextByte[i]);
            } else { //padding
                for (int j = (i) % 8; j < 8; j++) {
                    msg[j % 8] = (byte) '\0';
                }
            }
            if ((((i + 1) % 8 == 0))) { //when it get 8 bytes and can be encrypted into a block
                byte[] xor = xor(msg, tempIV);
                cipherBlocks.add(des.encrypt(xor));
                tempIV = cipherBlocks.get(blocks);
                cipherTextBlocks[blocks] = new String(tempIV);
                residue = new String(tempIV);

                blocks++;
                if (blocks - 1 == blockCounter) {
                    residue = new String(tempIV);
                    System.out.println("qq");
                    break;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cipherTextBlocks.length; i++) {
            sb.append(cipherTextBlocks[i]);
            System.out.println(i + "\t " + cipherTextBlocks[i] + "\t" + cipherTextBlocks.length);
        }

        return residue;
    }

    /**
     * Method that will take the byte and decrypt.
     * @throws Exception
     */
    public void performDecryption() throws Exception {
        System.out.println("\n\n");
        byte[] tempIV = IV;
        for (int i = 0; i < cipherBlocks.size(); i++) {
            byte[] plaintext = des.decrypt(cipherBlocks.get(i));
            byte[] xor = xor(plaintext, tempIV);
            tempIV = cipherBlocks.get(i);
            System.out.println(i + "\t" + new String(xor));
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
        int minLength = Math.min(input.length, IV.length);
//        System.out.println(minLength);
        byte[] result = new byte[minLength];
        for (int i = 0; i < minLength; i++) {
            result[i] = (byte) ((int) input[i] ^ (int) IV[i]);
        }
        return result;
    }

}
