package com.suhankoh;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Suhan on 10/15/15.
 */
public class OFB2 {
    private DESLibrary des;
    static String plainText = "QWERQWER";
    byte[] IV;

    public static void main(String[] args) throws Exception {
        OFB2 ofb2 = new OFB2();
        String secretKey = "QWERTYUI";

        System.out.println(ofb2.OFB2(plainText, secretKey, "TESTQWER", 8));
    }

    public String OFB2(String m, String key, String mIV, int k) throws Exception {
        this.IV = new byte[8];
//        new SecureRandom().nextBytes(IV);
        des = new DESLibrary(key, "DES");

        byte[] msg = m.getBytes();
        String msgBinaries = byteToBinary(msg, msg.length);
        byte[] IVByte = mIV.getBytes();
        String IVString = byteToBinary(IVByte, IVByte.length);
        byte[] encryptedIV = des.encrypt(IVByte); // encrypt K with IV

        String IVStr = byteToBinary(encryptedIV, encryptedIV.length);

        char[] msgChar = msgBinaries.toCharArray(); //transform all the msg into binaries char for XOR2
        char[] binaryEncryptedIV = byteToBinary(encryptedIV, encryptedIV.length).toCharArray(); //for XOR1
        char[] resultXor = new char[k];
        String append = "";
        String cipher = "";

        for(int loop = 0; loop < msgBinaries.length(); loop+=k){ // Each blocks
            for (int i = 0; i < k; i++) { //k bits
                if (((int) msgChar[i] + (int) binaryEncryptedIV[i]) == (49+48)) {
                    resultXor[i] = 1;
                } else {
                    resultXor[i] = 0;
                }
                append += (binaryEncryptedIV[i]);
            }
            //Each cipher blocks
            cipher += charArrayToString(resultXor);

            //"shifting bits" and appending new k-bits
            IVString = discardBinary(IVString, k, IVString.length()) + append;

            //Reencrypt the IV and key with the new IV
            IVByte = IVString.getBytes();
            encryptedIV = des.encrypt(IVByte); // encrypt K with IV

            binaryEncryptedIV = byteToBinary(encryptedIV, encryptedIV.length).toCharArray();
            append = "";
        }

        System.out.println(cipher +"\n"+IVString + "\n" + IVStr + "\n\n" + new String(binaryToByte(cipher)));
        return cipher;
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
        for (int i = 0; i < Byte.SIZE * size; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    public byte[] binaryToByte(String binaryString) {
        ArrayList<Integer> byteArrayList = new ArrayList<Integer>();
        for (String str : binaryString.split("(?<=\\G.{8})")) { //Regex for Continue matching only if \\G.{8} matches on the left, And \\G.{8} matches the first 8 everytime.
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
        int minLength = Math.min(input.length, IV.length);
        char[] result = new char[minLength];
        for (int i = 0; i < result.length; i++) {
            result[i] = (char) (input[i] ^ IV[i]);
        }
        return result;
    }

}
/*
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

                binaryIV = binaryIV + discardBinary(temp_binaryEncryptedIV, 0, binaryMsgStr.length());
                IV = binaryToByte(binaryIV);
                encryptedIV = des.encrypt(IV); // encrypt K with IV
                binaryIV = byteToBinary(IV, IV.length);
                binaryEncryptedIV = byteToBinary(encryptedIV, encryptedIV.length).toCharArray(); //for XOR1

                System.out.println("the i is:" + i + " binary: " + binary + "and byte:" + binary.length() / 8 + " string: " + binaryMsgStr);
            }

 */