package com.lu.gademo.trace.client.util;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;

public class SM2Utils {
    //鐢熸垚闅忔満绉橀挜瀵�
    public static void generateKeyPair() {
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();

        System.out.println("公钥：" + Util.byteToHex(publicKey.getEncoded(false)));
        System.out.println("私钥：" + Util.byteToHex(privateKey.toByteArray()));
    }

    //鏁版嵁鍔犲瘑
    public static String encrypt(byte[] publicKey, byte[] data) throws IOException {
        if (publicKey == null || publicKey.length == 0) {
            return null;
        }

        if (data == null || data.length == 0) {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        SM2 sm2 = SM2.Instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);

        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);

//		System.out.println("C1 " + Util.byteToHex(c1.getEncoded()));
//		System.out.println("C2 " + Util.byteToHex(source));
//		System.out.println("C3 " + Util.byteToHex(c3));
        //C1 C2 C3鎷艰鎴愬姞瀵嗗瓧涓�
        return Util.byteToHex(c1.getEncoded(false)) + Util.byteToHex(source) + Util.byteToHex(c3);

    }

    //鏁版嵁瑙ｅ瘑
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        if (privateKey == null || privateKey.length == 0) {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0) {
            return null;
        }
        //鍔犲瘑瀛楄妭鏁扮粍杞崲涓哄崄鍏繘鍒剁殑瀛楃涓� 闀垮害鍙樹负encryptedData.length * 2
        String data = Util.byteToHex(encryptedData);
        /***鍒嗚В鍔犲瘑瀛椾覆
         * 锛圕1 = C1鏍囧織浣�2浣� + C1瀹炰綋閮ㄥ垎128浣� = 130锛�
         * 锛圕3 = C3瀹炰綋閮ㄥ垎64浣�  = 64锛�
         * 锛圕2 = encryptedData.length * 2 - C1闀垮害  - C2闀垮害锛�
         */
        byte[] c1Bytes = Util.hexToByte(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Util.hexToByte(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);

        //閫氳繃C1瀹炰綋瀛楄妭鏉ョ敓鎴怑CPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);

        //杩斿洖瑙ｅ瘑缁撴灉
        return c2;
    }

    public static void main(String[] args) throws Exception {
        //鐢熸垚瀵嗛挜瀵�
        generateKeyPair();

        String plainText = "ererfeiisgod";
        byte[] sourceData = plainText.getBytes();

        //涓嬮潰鐨勭閽ュ彲浠ヤ娇鐢╣enerateKeyPair()鐢熸垚鐨勭閽ュ唴瀹�
        // 鍥藉瘑瑙勮寖姝ｅ紡绉侀挜
        String prik = "3690655E33D5EA3D9A4AE1A1ADD766FDEA045CDEAA43A9206FB8C430CEFE0D94";
        // 鍥藉瘑瑙勮寖姝ｅ紡鍏挜
        String pubk = "04F6E0C3345AE42B51E06BF50B98834988D54EBC7460FE135A48171BC0629EAE205EEDE253A530608178A98F1E19BB737302813BA39ED3FA3C51639D7A20C7391A";

        System.out.println("鍔犲瘑: ");
        String cipherText = SM2Utils.encrypt(Util.hexToByte(pubk), sourceData);
        System.out.println(cipherText);
        System.out.println("瑙ｅ瘑: ");
        plainText = new String(SM2Utils.decrypt(Util.hexToByte(prik), Util.hexToByte(cipherText)));
        System.out.println(plainText);

    }
}
