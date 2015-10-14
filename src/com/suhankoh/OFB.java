package com.suhankoh;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Suhan on 10/14/15.
 */
public class OFB {
    private byte[] IV;
    private DESLibrary des;
    static String plainText = "qwerqweree";

    public static void main(String[] args) throws Exception {
        OFB ofb = new OFB();
        ofb.performOFB();

    }


    public void performOFB() throws Exception {
        IV = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        String secretKey = "QWERTYUI";
        des = new DESLibrary(secretKey, "DES");
        byte[] plantTextBytes = plainText.getBytes();

        byte[] encryptedIV = des.encrypt(IV); // encrypt K with IV
        String binaries = toBinary(encryptedIV);
        byte[] array = new byte[]{0xa};
        byte[] shift = bitsShift(array, 2);
        System.out.println(String.format("%x", shift[0]));




    }

    public String discardBinary(String binary, int from, int to) {
        return binary.substring(from, to);
    }

    public String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    public byte[] binaryToByte(String binaryString) {
        ArrayList<Integer> byteArrayList = new ArrayList<Integer>();

        for (String str : binaryString.split("(?<=\\G.{8})")) { //Regex for Continue matching only if \\G.{8} matches on the left, And \\G.{8} matches the first 8 everytime.
            System.out.println(str);
            byteArrayList.add(Integer.parseInt(str, 2));
        }
        byte[] bytes = new byte[byteArrayList.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(byteArrayList.get(5).toString());
        }
        return bytes;
    }

    public byte[] bitsShift(byte[] input, int position) {
        BigInteger bigInt = new BigInteger(input);
        System.out.println(bigInt);
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

//        byte[] shifted = bitsShift(array, 1);
//        int sourceByte = 0xFF & (int)(array[0]);  // Convert byte to unsigned int

//        for (byte b : shifted) {
//            System.out.println(String.format("%x", 0x20));
//            System.out.println(String.format("%x", b));
//        }
//
//        String test = toBinary(array);
//        BigInteger t = new BigInteger(test);
//        BigInteger shiftInt = t.shiftRight(1);

//        Integer.parseInt(hex, 16);
//        Integer.parseInt(binary, 2);

//        System.out.println(Integer.parseInt(array[0], 2));
//        System.out.println(Byte.parseByte("11111110", 2));
//        byte[] b = new byte[1];
//        b[0] = (byte)Integer.parseInt(byteArrayList.get(5).toString());
//        System.out.println(String.format("%x", b[0]));  // Outputs [78, 187, 96, 17, 21]
//String binaryString = "10101010";
