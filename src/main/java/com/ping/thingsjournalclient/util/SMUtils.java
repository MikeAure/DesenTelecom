package com.ping.thingsjournalclient.util;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

public class SMUtils {
    /**
     * sm2的获取一对公私钥对，以十六进制字符串形式保存
     *
     * @return
     */
    public static HashMap<String, String> sm2generateKeyPair() {
        HashMap<String, String> keyMap = new HashMap<>();
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        keyMap.put("publicKey", Util.byteToHex(publicKey.getEncoded(false)));
        keyMap.put("privateKey", Util.byteToHex(privateKey.toByteArray()));
        return keyMap;
    }

    /**
     * sm2加密算法，传入一个明文字符串和十六进制字符串的公钥，返回一个十六进制字符串的密文
     *
     * @param sourceStr
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static String encryptBySm2(String sourceStr, String publicKeyStr) throws Exception {
        byte[] sourceData = sourceStr.getBytes();
        String cipherText = SM2Utils.encrypt(Util.hexToByte(publicKeyStr), sourceData);
        return cipherText;
    }

    /**
     * sm2解密算法，传入一个十六进制字符串的密文和一个十六进制字符串的私钥，返回一个明文字符串
     *
     * @param cipherText
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptBySm2(String cipherText, String privateKey) throws Exception {
        String plainText = new String(SM2Utils.decrypt(Util.hexToByte(privateKey), Util.hexToByte(cipherText)));
        return plainText;
    }

    /**
     * sm3Hash加密算法，返回一个大写的十六进制字符串
     *
     * @param sourceStr
     * @return
     */
    public static String encryptBySm3(String sourceStr) {
        byte[] md = new byte[32];
        byte[] msg1 = sourceStr.getBytes();
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg1, 0, msg1.length);
        sm3.doFinal(md, 0);
        String s = new String(Hex.encode(md));
        return s.toUpperCase();
    }

    /**
     * sm4生成加密秘钥，返回一个十六进制字符串
     *
     * @return
     * @throws Exception
     */
    public static String sm4generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(new SecureRandom());
        SecretKey secretKye = keyGenerator.generateKey();
        byte[] keyBytes = secretKye.getEncoded();
        return Util.byteToHex(keyBytes);
    }

    /**
     * Sm4加密算法，传入一个字符串作为明文，返回一个字符串
     *
     * @param sourceStr
     * @return
     */
    public static String encryptBySm4(String sourceStr, String secretKey) {
        return SM4Util.encryptData_ECB(sourceStr, secretKey);
    }

    /**
     * Sm4解密算法，传入一个密文字符串，返回一个明文字符串
     *
     * @param cipherText
     * @return
     */
    public static String decryptBySm4(String cipherText, String secretKey) {
        return SM4Util.decryptData_ECB(cipherText, secretKey);
    }

    public static void main(String[] args) throws Exception {
//		HashMap<String, String> a= SMUtils.sm2generateKeyPair();
//		String b = "nihao";
//		System.out.println(b);
//		String c = SMUtils.encryptBySm2(b, a.get("publicKey"));
//		System.out.println(c);
//		String d = SMUtils.decryptBySm2(c, a.get("privateKey"));
//		System.out.println(d);
//		String a = "nihao";
//		System.out.println(SMUtils.encryptBySm3(a));
        long start = System.currentTimeMillis();
        String a = SMUtils.sm4generateKey();
        System.out.println(a + " " + (System.currentTimeMillis() - start));
        String b = "nihao";
        String d = SMUtils.encryptBySm4(b, a);
        System.out.println(d);
        System.out.println(SMUtils.decryptBySm4(d, a));
    }

}
