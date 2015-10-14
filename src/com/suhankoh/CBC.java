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
        if(args.length >= 3) {
            String encryptOrDecrypt = args[0];
            String secretKey = args[1];
            File file = new File(args[2]);

            try {
                FileOutputStream writer = new FileOutputStream("test2.txt");

                DESLibrary des = new DESLibrary(secretKey, "DES");
                byte[] bFile = new byte[8]; //8 bytes = 64 bits
                FileInputStream is = new FileInputStream(file);
                int size = 0;
                int offset = 0;
                byte[] desBytes = new byte[8];
                while((size = is.read(bFile, 0, bFile.length)) != -1){
                    
                    desBytes = des.encrypt(bFile);
                    System.out.println(desBytes);
                    writer.write(bFile,0,size);
                    offset += size;
                }


            } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
                e.printStackTrace();
            }

        }

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
