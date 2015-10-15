package com.suhankoh;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Suhan on 10/15/15.
 */
public class CFB2 {
    private DESLibrary des;
    static String plainText = "QWERQWER";
    byte[] IV;

    public static void main(String[] args) throws Exception {
        CFB2 cfb2 = new CFB2();
        String secretKey = "QWERTYUI";

        System.out.println(cfb2.CFB2(plainText, secretKey, "TESTQWER", 8));
    }

    public String CFB2(String m, String key, String mIV, int k) throws Exception {
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

        for (int loop = 0; loop < msgBinaries.length(); loop += k) { // Each blocks
            for (int i = 0; i < k; i++) { //k bits
                if (((int) msgChar[i] + (int) binaryEncryptedIV[i]) == (49 + 48)) {
                    resultXor[i] = 1;
                } else {
                    resultXor[i] = 0;
                }
                append += (int)(resultXor[i]);

            }
//            System.out.println("test:\t\t" + append);

            //Each cipher blocks
            cipher += charArrayToString(resultXor);
            //"shifting bits" and appending new k-bits
            IVString = discardBinary(IVString, k, IVString.length()) + append;

            //Reencrypt the IV and key with the new IV
            IVByte = IVString.getBytes();
            encryptedIV = des.encrypt(IVByte); // encrypt K with IV
            System.out.println(IVString);
            binaryEncryptedIV = byteToBinary(encryptedIV, encryptedIV.length).toCharArray();
            append = "";
        }

        /* OFB2
        0100010101010011010101000101000101010111010001010101001000010111
0101001101010100010100010101011101000101010100100001011111110111
0101010001010001010101110100010101010010000101111111011111010010
0101000101010111010001010101001000010111111101111101001010000000
0101011101000101010100100001011111110111110100101000000001010100
0100010101010010000101111111011111010010100000000101010011010011
0101001000010111111101111101001010000000010101001101001111110111
0001011111110111110100101000000001010100110100111111011111001011
0100011010100110100000111101000100000101100000101010011010011010
0001011111110111110100101000000001010100110100111111011111001011

         */
        /* CFB2
        0100010101010011010101000101000101010111010001010101001001000110
0101001101010100010100010101011101000101010100100100011010100110
0101010001010001010101110100010101010010010001101010011010000011
0101000101010111010001010101001001000110101001101000001111010001
0101011101000101010100100100011010100110100000111101000100000101
0100010101010010010001101010011010000011110100010000010110000010
0101001001000110101001101000001111010001000001011000001010100110
0100011010100110100000111101000100000101100000101010011010011010
0100011010100110100000111101000100000101100000101010011010011010
0100011010100110100000111101000100000101100000101010011010011010
         */

        System.out.println(cipher + "\n" + IVString + "\n" + IVStr + "\n\n" + new String(binaryToByte(cipher)));
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
