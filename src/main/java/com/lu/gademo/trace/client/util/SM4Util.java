package com.lu.gademo.trace.client.util;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SM4Util {

    public static String encryptData_ECB(String plainText, String secretKey) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            keyBytes = Util.hexStringToBytes(secretKey);


            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes(StandardCharsets.UTF_8));
//            String cipherText = new BASE64Encoder().encode(encrypted);
            String cipherText = Base64.getEncoder().encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptData_ECB(String cipherText, String secretKey) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            keyBytes = Util.hexStringToBytes(secretKey);


            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
//            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, new BASE64Decoder().decodeBuffer(cipherText));
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
