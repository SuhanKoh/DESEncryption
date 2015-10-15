package com.suhankoh;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by Suhan on 10/14/15.
 */
public class OFB {
    private byte[] IV;
    private DESLibrary des;
    static String plainText = "QWERQWEWERQWER";

    public static void main(String[] args) throws Exception {
        OFB ofb = new OFB();
        ofb.performOFB();
    }


    public void performOFB() throws Exception {
        IV = new byte[8];
//        new SecureRandom().nextBytes(IV);
        System.out.println("ID311231:" + new String(IV));

        String secretKey = "QWERTYUI";
        des = new DESLibrary(secretKey, "DES");


        byte[] plainTextBytes = plainText.getBytes();
        char[] xor = new char[0];

        byte[] encryptedIV = des.encrypt(IV); // encrypt K with IV

        String binaryIV = byteToBinary(IV, IV.length);

        char[] binaryEncryptedIV = byteToBinary(encryptedIV, encryptedIV.length).toCharArray(); //for XOR1

        byte[] msg = new byte[8];
        int msgCount = 0;
        for (int i = 0; i < plainTextBytes.length; i++) {
            msg[i % 8] = plainTextBytes[i];
            if ((i + 1) % 8 == 0) {
                msgCount = 8;
            } else {
                msgCount++;
            }
            if (((((i + 1) % 8) == 0) || i == plainTextBytes.length - 1) && i > 0) { // 8 byte blocks
                String binaryMsgStr = byteToBinary(msg, msgCount);
                msgCount = 0;
                char[] msgBinary = binaryMsgStr.toCharArray(); //binaries of the msg for XOR2
                xor = xor(binaryEncryptedIV, msgBinary); //perform XOR
                String binary = charArrayToString(xor); //change the XOR chars into String

                binaryIV = discardBinary(binaryIV, binaryMsgStr.length(), binaryIV.length()); //getting the IV

                String temp_binaryEncryptedIV = charArrayToString(binaryEncryptedIV);
                byte[] temp = temp_binaryEncryptedIV.getBytes();
                temp_binaryEncryptedIV = byteToBinary(temp, temp.length);
                System.out.println("ID:" + binaryIV);

                binaryIV = binaryIV + discardBinary(temp_binaryEncryptedIV, 0, binaryMsgStr.length());
                System.out.println("ID:" + new String(IV));
                IV = binaryToByte(binaryIV);
                System.out.println("ID:" + new String(IV));
                encryptedIV = des.encrypt(IV); // encrypt K with IV
                binaryIV = byteToBinary(IV, IV.length);
                binaryEncryptedIV = byteToBinary(encryptedIV, encryptedIV.length).toCharArray(); //for XOR1

                System.out.println("the i is:" + i + " binary: " + binary + "and byte:" + binary.length() / 8 + " string: " + binaryMsgStr);
            }

        }


    }

    public String charArrayToString(char[] chars) {
        StringBuilder xorBinary = new StringBuilder(64);
        for (int j = 0; j < chars.length; j++) {
            xorBinary.append((int) chars[j]);
        }
        return xorBinary.toString();
    }

    public String discardBinary(String binary, int from, int to) {
        return binary.substring(from, to);
    }

    public String byteToBinary(byte[] bytes, int size) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        System.out.println(size);
        for (int i = 0; i < Byte.SIZE * size; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    public byte[] binaryToByte(String binaryString) {
        ArrayList<Integer> byteArrayList = new ArrayList<Integer>();
//        System.out.println("BINARY STRING" + binaryString);
        for (String str : binaryString.split("(?<=\\G.{8})")) { //Regex for Continue matching only if \\G.{8} matches on the left, And \\G.{8} matches the first 8 everytime.
//            System.out.println(str);
            byteArrayList.add(Integer.parseInt(str, 2));
        }
        byte[] bytes = new byte[byteArrayList.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(byteArrayList.get(i).toString());
        }
        return bytes;
    }

    public byte[] bitsShift(byte[] input, int position) {
        BigInteger bigInt = new BigInteger(input);
//        System.out.println(bigInt);
        BigInteger shiftInt = bigInt.shiftRight(position);
        return shiftInt.toByteArray();
    }

    /**
     * Take an input byte array, and IV byte array, and will perform XOR on it, and return an array of byte.
     *
     * @param input byte array of the input
     * @param IV
     * @return
     */
    public char[] xor(char[] input, char[] IV) {
        System.out.println(input.length + "  " + IV.length);
        int minLength = Math.min(input.length, IV.length);
        char[] result = new char[minLength];
        for (int i = 0; i < result.length; i++) {
            result[i] = (char) (input[i] ^ IV[i]);
            //System.out.println("Input: " + (int) input[i] + "  IV: " + (int) IV[i] + "  and the result is: " + (int) result[i]);
        }
        return result;
    }
}