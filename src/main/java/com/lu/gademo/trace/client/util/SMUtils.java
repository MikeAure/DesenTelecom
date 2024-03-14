package com.lu.gademo.trace.client.util;

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
     * sm2鐨勮幏鍙栦竴瀵瑰叕绉侀挜瀵癸紝浠ュ崄鍏繘鍒跺瓧绗︿覆褰㈠紡淇濆瓨
     *
     * @return
     */
    public static HashMap<String, String> sm2generateKeyPair() {
        HashMap<String, String> keyMap = new HashMap<String, String>();
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
     * sm2鍔犲瘑绠楁硶锛屼紶鍏ヤ竴涓槑鏂囧瓧绗︿覆鍜屽崄鍏繘鍒跺瓧绗︿覆鐨勫叕閽ワ紝杩斿洖涓�涓崄鍏繘鍒跺瓧绗︿覆鐨勫瘑鏂�
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
     * sm2瑙ｅ瘑绠楁硶锛屼紶鍏ヤ竴涓崄鍏繘鍒跺瓧绗︿覆鐨勫瘑鏂囧拰涓�涓崄鍏繘鍒跺瓧绗︿覆鐨勭閽ワ紝杩斿洖涓�涓槑鏂囧瓧绗︿覆
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
     * sm3Hash鍔犲瘑绠楁硶锛岃繑鍥炰竴涓ぇ鍐欑殑鍗佸叚杩涘埗瀛楃涓�
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
     * sm4鐢熸垚鍔犲瘑绉橀挜锛岃繑鍥炰竴涓崄鍏繘鍒跺瓧绗︿覆
     *
     * @return
     * @throws Exception
     */
    public static String sm4generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(new SecureRandom());
        SecretKey secretKye = keyGenerator.generateKey();
        byte[] keyBytes = secretKye.getEncoded();
        String key = Util.byteToHex(keyBytes);
        return key;
    }

    /**
     * Sm4鍔犲瘑绠楁硶锛屼紶鍏ヤ竴涓瓧绗︿覆浣滀负鏄庢枃锛岃繑鍥炰竴涓瓧绗︿覆
     *
     * @param sourceStr
     * @return
     */
    public static String encryptBySm4(String sourceStr, String secretKey) {
        String cipherText = SM4Util.encryptData_ECB(sourceStr, secretKey);
        return cipherText;
    }

    /**
     * Sm4瑙ｅ瘑绠楁硶锛屼紶鍏ヤ竴涓瘑鏂囧瓧绗︿覆锛岃繑鍥炰竴涓槑鏂囧瓧绗︿覆
     *
     * @param cipherText
     * @return
     */
    public static String decryptBySm4(String cipherText, String secretKey) {
        String plainText = SM4Util.decryptData_ECB(cipherText, secretKey);
        return plainText;
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
