package com.lu.gademo.trace.client.util;

import java.math.BigInteger;

public class Util {
    /**
     * 鏁村舰杞崲鎴愮綉缁滀紶杈撶殑瀛楄妭娴侊紙瀛楄妭鏁扮粍锛夊瀷鏁版嵁
     *
     * @param num 涓�涓暣鍨嬫暟鎹�
     * @return 4涓瓧鑺傜殑鑷繁鏁扮粍
     */
    public static byte[] intToBytes(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (0xff & (num >> 0));
        bytes[1] = (byte) (0xff & (num >> 8));
        bytes[2] = (byte) (0xff & (num >> 16));
        bytes[3] = (byte) (0xff & (num >> 24));
        return bytes;
    }

    /**
     * 鍥涗釜瀛楄妭鐨勫瓧鑺傛暟鎹浆鎹㈡垚涓�涓暣褰㈡暟鎹�
     *
     * @param bytes 4涓瓧鑺傜殑瀛楄妭鏁扮粍
     * @return 涓�涓暣鍨嬫暟鎹�
     */
    public static int byteToInt(byte[] bytes) {
        int num = 0;
        int temp;
        temp = (0x000000ff & (bytes[0])) << 0;
        num = num | temp;
        temp = (0x000000ff & (bytes[1])) << 8;
        num = num | temp;
        temp = (0x000000ff & (bytes[2])) << 16;
        num = num | temp;
        temp = (0x000000ff & (bytes[3])) << 24;
        num = num | temp;
        return num;
    }

    /**
     * 闀挎暣褰㈣浆鎹㈡垚缃戠粶浼犺緭鐨勫瓧鑺傛祦锛堝瓧鑺傛暟缁勶級鍨嬫暟鎹�
     *
     * @param num 涓�涓暱鏁村瀷鏁版嵁
     * @return 4涓瓧鑺傜殑鑷繁鏁扮粍
     */
    public static byte[] longToBytes(long num) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (0xff & (num >> (i * 8)));
        }

        return bytes;
    }

    /**
     * 澶ф暟瀛楄浆鎹㈠瓧鑺傛祦锛堝瓧鑺傛暟缁勶級鍨嬫暟鎹�
     *
     * @param n
     * @return
     */
    public static byte[] byteConvert32Bytes(BigInteger n) {
        byte[] tmpd = null;
        if (n == null) {
            return null;
        }

        if (n.toByteArray().length == 33) {
            tmpd = new byte[32];
            System.arraycopy(n.toByteArray(), 1, tmpd, 0, 32);
        } else if (n.toByteArray().length == 32) {
            tmpd = n.toByteArray();
        } else {
            tmpd = new byte[32];
            for (int i = 0; i < 32 - n.toByteArray().length; i++) {
                tmpd[i] = 0;
            }
            System.arraycopy(n.toByteArray(), 0, tmpd, 32 - n.toByteArray().length, n.toByteArray().length);
        }
        return tmpd;
    }

    /**
     * 鎹㈠瓧鑺傛祦锛堝瓧鑺傛暟缁勶級鍨嬫暟鎹浆澶ф暟瀛�
     *
     * @param b
     * @return
     */
    public static BigInteger byteConvertInteger(byte[] b) {
        if (b[0] < 0) {
            byte[] temp = new byte[b.length + 1];
            temp[0] = 0;
            System.arraycopy(b, 0, temp, 1, b.length);
            return new BigInteger(temp);
        }
        return new BigInteger(b);
    }

    /**
     * 鏍规嵁瀛楄妭鏁扮粍鑾峰緱鍊�(鍗佸叚杩涘埗鏁板瓧)
     *
     * @param bytes
     * @return
     */
    public static String getHexString(byte[] bytes) {
        return getHexString(bytes, true);
    }

    /**
     * 鏍规嵁瀛楄妭鏁扮粍鑾峰緱鍊�(鍗佸叚杩涘埗鏁板瓧)
     *
     * @param bytes
     * @param upperCase
     * @return
     */
    public static String getHexString(byte[] bytes, boolean upperCase) {
        String ret = "";
        for (int i = 0; i < bytes.length; i++) {
            ret += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return upperCase ? ret.toUpperCase() : ret;
    }

    /**
     * 鎵撳嵃鍗佸叚杩涘埗瀛楃涓�
     *
     * @param bytes
     */
    public static void printHexString(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print("0x" + hex.toUpperCase() + ",");
        }
        System.out.println();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 鐢ㄤ簬寤虹珛鍗佸叚杩涘埗瀛楃鐨勮緭鍑虹殑灏忓啓瀛楃鏁扮粍
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 鐢ㄤ簬寤虹珛鍗佸叚杩涘埗瀛楃鐨勮緭鍑虹殑澶у啓瀛楃鏁扮粍
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃鏁扮粍
     *
     * @param data byte[]
     * @return 鍗佸叚杩涘埗char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃鏁扮粍
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 浼犳崲鎴愬皬鍐欐牸寮� 锛� <code>false</code> 浼犳崲鎴愬ぇ鍐欐牸寮�
     * @return 鍗佸叚杩涘埗char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃鏁扮粍
     *
     * @param data     byte[]
     * @param toDigits 鐢ㄤ簬鎺у埗杈撳嚭鐨刢har[]
     * @return 鍗佸叚杩涘埗char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃涓�
     *
     * @param data byte[]
     * @return 鍗佸叚杩涘埗String
     */
    public static String encodeHexString(byte[] data) {
        return encodeHexString(data, true);
    }

    /**
     * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃涓�
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 浼犳崲鎴愬皬鍐欐牸寮� 锛� <code>false</code> 浼犳崲鎴愬ぇ鍐欐牸寮�
     * @return 鍗佸叚杩涘埗String
     */
    public static String encodeHexString(byte[] data, boolean toLowerCase) {
        return encodeHexString(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃涓�
     *
     * @param data     byte[]
     * @param toDigits 鐢ㄤ簬鎺у埗杈撳嚭鐨刢har[]
     * @return 鍗佸叚杩涘埗String
     */
    protected static String encodeHexString(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * 灏嗗崄鍏繘鍒跺瓧绗︽暟缁勮浆鎹负瀛楄妭鏁扮粍
     *
     * @param data 鍗佸叚杩涘埗char[]
     * @return byte[]
     * @throws RuntimeException 濡傛灉婧愬崄鍏繘鍒跺瓧绗︽暟缁勬槸涓�涓鎬殑闀垮害锛屽皢鎶涘嚭杩愯鏃跺紓甯�
     */
    public static byte[] decodeHex(char[] data) {
        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 灏嗗崄鍏繘鍒跺瓧绗﹁浆鎹㈡垚涓�涓暣鏁�
     *
     * @param ch    鍗佸叚杩涘埗char
     * @param index 鍗佸叚杩涘埗瀛楃鍦ㄥ瓧绗︽暟缁勪腑鐨勪綅缃�
     * @return 涓�涓暣鏁�
     * @throws RuntimeException 褰揷h涓嶆槸涓�涓悎娉曠殑鍗佸叚杩涘埗瀛楃鏃讹紝鎶涘嚭杩愯鏃跺紓甯�
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch
                    + " at index " + index);
        }
        return digit;
    }

    /**
     * 鏁板瓧瀛楃涓茶浆ASCII鐮佸瓧绗︿覆
     *
     * @param String 瀛楃涓�
     * @return ASCII瀛楃涓�
     */
    public static String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
    }

    /**
     * 鍗佸叚杩涘埗杞瓧绗︿覆
     *
     * @param hexString  鍗佸叚杩涘埗瀛楃涓�
     * @param encodeType 缂栫爜绫诲瀷4锛歎nicode锛�2锛氭櫘閫氱紪鐮�
     * @return 瀛楃涓�
     */
    public static String hexStringToString(String hexString, int encodeType) {
        String result = "";
        int max = hexString.length() / encodeType;
        for (int i = 0; i < max; i++) {
            char c = (char) hexStringToAlgorism(hexString
                    .substring(i * encodeType, (i + 1) * encodeType));
            result += c;
        }
        return result;
    }

    /**
     * 鍗佸叚杩涘埗瀛楃涓茶鍗佽繘鍒�
     *
     * @param hex 鍗佸叚杩涘埗瀛楃涓�
     * @return 鍗佽繘鍒舵暟鍊�
     */
    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }

    /**
     * 鍗佸叚杞簩杩涘埗
     *
     * @param hex 鍗佸叚杩涘埗瀛楃涓�
     * @return 浜岃繘鍒跺瓧绗︿覆
     */
    public static String hexStringToBinary(String hex) {
        hex = hex.toUpperCase();
        String result = "";
        int max = hex.length();
        for (int i = 0; i < max; i++) {
            char c = hex.charAt(i);
            switch (c) {
                case '0':
                    result += "0000";
                    break;
                case '1':
                    result += "0001";
                    break;
                case '2':
                    result += "0010";
                    break;
                case '3':
                    result += "0011";
                    break;
                case '4':
                    result += "0100";
                    break;
                case '5':
                    result += "0101";
                    break;
                case '6':
                    result += "0110";
                    break;
                case '7':
                    result += "0111";
                    break;
                case '8':
                    result += "1000";
                    break;
                case '9':
                    result += "1001";
                    break;
                case 'A':
                    result += "1010";
                    break;
                case 'B':
                    result += "1011";
                    break;
                case 'C':
                    result += "1100";
                    break;
                case 'D':
                    result += "1101";
                    break;
                case 'E':
                    result += "1110";
                    break;
                case 'F':
                    result += "1111";
                    break;
            }
        }
        return result;
    }

    /**
     * ASCII鐮佸瓧绗︿覆杞暟瀛楀瓧绗︿覆
     *
     * @param String ASCII瀛楃涓�
     * @return 瀛楃涓�
     */
    public static String AsciiStringToString(String content) {
        String result = "";
        int length = content.length() / 2;
        for (int i = 0; i < length; i++) {
            String c = content.substring(i * 2, i * 2 + 2);
            int a = hexStringToAlgorism(c);
            char b = (char) a;
            String d = String.valueOf(b);
            result += d;
        }
        return result;
    }

    /**
     * 灏嗗崄杩涘埗杞崲涓烘寚瀹氶暱搴︾殑鍗佸叚杩涘埗瀛楃涓�
     *
     * @param algorism  int 鍗佽繘鍒舵暟瀛�
     * @param maxLength int 杞崲鍚庣殑鍗佸叚杩涘埗瀛楃涓查暱搴�
     * @return String 杞崲鍚庣殑鍗佸叚杩涘埗瀛楃涓�
     */
    public static String algorismToHexString(int algorism, int maxLength) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;
        }
        return patchHexString(result.toUpperCase(), maxLength);
    }

    /**
     * 瀛楄妭鏁扮粍杞负鏅�氬瓧绗︿覆锛圓SCII瀵瑰簲鐨勫瓧绗︼級
     *
     * @param bytearray byte[]
     * @return String
     */
    public static String byteToString(byte[] bytearray) {
        String result = "";
        char temp;

        int length = bytearray.length;
        for (int i = 0; i < length; i++) {
            temp = (char) bytearray[i];
            result += temp;
        }
        return result;
    }

    /**
     * 浜岃繘鍒跺瓧绗︿覆杞崄杩涘埗
     *
     * @param binary 浜岃繘鍒跺瓧绗︿覆
     * @return 鍗佽繘鍒舵暟鍊�
     */
    public static int binaryToAlgorism(String binary) {
        int max = binary.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = binary.charAt(i - 1);
            int algorism = c - '0';
            result += Math.pow(2, max - i) * algorism;
        }
        return result;
    }

    /**
     * 鍗佽繘鍒惰浆鎹负鍗佸叚杩涘埗瀛楃涓�
     *
     * @param algorism int 鍗佽繘鍒剁殑鏁板瓧
     * @return String 瀵瑰簲鐨勫崄鍏繘鍒跺瓧绗︿覆
     */
    public static String algorismToHEXString(int algorism) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;

        }
        result = result.toUpperCase();

        return result;
    }

    /**
     * HEX瀛楃涓插墠琛�0锛屼富瑕佺敤浜庨暱搴︿綅鏁颁笉瓒炽��
     *
     * @param str       String 闇�瑕佽ˉ鍏呴暱搴︾殑鍗佸叚杩涘埗瀛楃涓�
     * @param maxLength int 琛ュ厖鍚庡崄鍏繘鍒跺瓧绗︿覆鐨勯暱搴�
     * @return 琛ュ厖缁撴灉
     */
    static public String patchHexString(String str, int maxLength) {
        String temp = "";
        for (int i = 0; i < maxLength - str.length(); i++) {
            temp = "0" + temp;
        }
        str = (temp + str).substring(0, maxLength);
        return str;
    }

    /**
     * 灏嗕竴涓瓧绗︿覆杞崲涓篿nt
     *
     * @param s          String 瑕佽浆鎹㈢殑瀛楃涓�
     * @param defaultInt int 濡傛灉鍑虹幇寮傚父,榛樿杩斿洖鐨勬暟瀛�
     * @param radix      int 瑕佽浆鎹㈢殑瀛楃涓叉槸浠�涔堣繘鍒剁殑,濡�16 8 10.
     * @return int 杞崲鍚庣殑鏁板瓧
     */
    public static int parseToInt(String s, int defaultInt, int radix) {
        int i = 0;
        try {
            i = Integer.parseInt(s, radix);
        } catch (NumberFormatException ex) {
            i = defaultInt;
        }
        return i;
    }

    /**
     * 灏嗕竴涓崄杩涘埗褰㈠紡鐨勬暟瀛楀瓧绗︿覆杞崲涓篿nt
     *
     * @param s          String 瑕佽浆鎹㈢殑瀛楃涓�
     * @param defaultInt int 濡傛灉鍑虹幇寮傚父,榛樿杩斿洖鐨勬暟瀛�
     * @return int 杞崲鍚庣殑鏁板瓧
     */
    public static int parseToInt(String s, int defaultInt) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            i = defaultInt;
        }
        return i;
    }

    /**
     * 鍗佸叚杩涘埗涓茶浆鍖栦负byte鏁扮粍
     *
     * @return the array of byte
     */
    public static byte[] hexToByte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    /**
     * 瀛楄妭鏁扮粍杞崲涓哄崄鍏繘鍒跺瓧绗︿覆
     *
     * @param b byte[] 闇�瑕佽浆鎹㈢殑瀛楄妭鏁扮粍
     * @return String 鍗佸叚杩涘埗瀛楃涓�
     */
    public static String byteToHex(byte[] b) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] subByte(byte[] input, int startIndex, int length) {
        byte[] bt = new byte[length];
        System.arraycopy(input, 0 + startIndex, bt, 0, length);
        return bt;
    }
}
