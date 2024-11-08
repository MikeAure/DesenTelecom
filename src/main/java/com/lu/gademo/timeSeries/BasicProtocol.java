package com.lu.gademo.timeSeries;

import java.math.BigInteger;
import java.security.SecureRandom;


public class BasicProtocol {

    public static int signComputeProtocol(BigInteger c1, SHSParamters Param) {

        int k1 = Param.k1;
        int k2 = Param.k2;
        BigInteger N = Param.N;

        SecureRandom rnd = new SecureRandom();

        // server S1
        BigInteger r1 = new BigInteger(Param.k1, rnd);
        BigInteger r2 = new BigInteger(Param.k1, rnd);

        while (r1.compareTo(r2) == -1 || r1.compareTo(r2) == 0) {
            r1 = new BigInteger(Param.k1, rnd);
            r2 = new BigInteger(Param.k1, rnd);
        }

        BigInteger c = c1.multiply(r1).mod(N).add(r2).mod(N);

        //server S2
        BigInteger m = SymHomSch.Dec(c, Param);

        if (m.bitLength() == Param.L.bitLength()) {
            return -1;
        } else {
            return 1;
        }
    }


    public static BigInteger minimumComputeProtocol(BigInteger c1, BigInteger c2, BigInteger cipherMiunsOne, BigInteger cipherOne, SHSParamters Param) {
        int k1 = Param.k1;
        int k2 = Param.k2;
        BigInteger L = Param.L;
        BigInteger N = Param.N;

        // server s1
        SecureRandom rnd = new SecureRandom();
        BigInteger c = c1.add(c2.multiply(cipherMiunsOne)).mod(N);
        int sign = signComputeProtocol(c, Param);
        int data;
        BigInteger result = BigInteger.ONE;

        if (sign == -1) {
            data = 1;
        } else {
            data = 0;
        }

        // server s2
        BigInteger encryptSign = SymHomSch.EncInt(data, Param);

        //server s1
        BigInteger result1 = c1.multiply(encryptSign).mod(N);
        BigInteger result2 = c2.multiply(cipherOne.add(encryptSign.multiply(cipherMiunsOne))).mod(N);

        result = result1.add(result2).mod(N);

        return result;

    }

    public static BigInteger absoluteComputeProtocol(BigInteger c1, BigInteger c2, BigInteger cipherMiunsOne, BigInteger cipherOne, SHSParamters Param) {

        int k1 = Param.k1;
        int k2 = Param.k2;
        BigInteger L = Param.L;
        BigInteger N = Param.N;

//		System.out.println("k0:" + Param.k0 +";k1" + Param.k1 + ";k2" + Param.k2);

//		System.out.println("p:" + Param.p + ";q:" + Param.q + ";N" + N + ";L" + Param.L);


        SecureRandom rnd = new SecureRandom();
        BigInteger divided = L.divide(BigInteger.valueOf(2));
        System.out.println("在密文上进行计算：");
        BigInteger c = c1.add(c2.multiply(cipherMiunsOne)).mod(N);
        System.out.println("c1:" + c1);
        System.out.println("c2:" + c2);
        System.out.println("c:" + c);
        System.out.println("解密后的明文：");
        BigInteger decC1 = SymHomSch.Dec(c1, Param).compareTo(divided) == -1 ? SymHomSch.Dec(c1, Param) : SymHomSch.Dec(c1, Param).subtract(L);

        BigInteger decC2 = SymHomSch.Dec(c2, Param).compareTo(divided) == -1 ? SymHomSch.Dec(c2, Param) : SymHomSch.Dec(c2, Param).subtract(L);
        BigInteger decC = SymHomSch.Dec(c, Param).compareTo(divided) == -1 ? SymHomSch.Dec(c, Param) : SymHomSch.Dec(c, Param).subtract(L);
        System.out.println("c1:" + decC1);
        System.out.println("c2:" + decC2);
        System.out.println("c:" + decC);
        int sign = signComputeProtocol(c, Param);

        BigInteger result = BigInteger.ONE;

        BigInteger Sign = BigInteger.ONE;
        if (sign == -1) {
            Sign = L.subtract(BigInteger.ONE);
        } else {
            Sign = BigInteger.ONE;
        }

        BigInteger encryptSign = SymHomSch.EncBiginteger(Sign, Param);
        result = c.multiply(encryptSign).mod(N);

        return result;

    }

    public static BigInteger Reencryption(BigInteger c, BigInteger cipherMiunsOne, SHSParamters Param) {

        // server S1
        SecureRandom rnd = new SecureRandom();
        BigInteger r1 = new BigInteger(Param.k1, rnd);
        BigInteger x = c.add(r1).mod(Param.N);

        // server S2
        BigInteger m = SymHomSch.Dec(x, Param);
        // re-encrypt
        m = SymHomSch.EncBiginteger(m, Param);

        // recover result
        BigInteger result = m.add(cipherMiunsOne.multiply(r1)).mod(Param.N);
        return result;

    }

    public static void main(String[] args) {

        int verify_w = 80;
        int verify_k1 = 10;
        int verify_k2 = 30;
        int verify_k0 = (verify_w + 1) * 2 * verify_k2 + verify_k2;

        SHSParamters verifyParam = SymHomSch.KeyGen(verify_k0, verify_k1, verify_k2);
        BigInteger L = verifyParam.L;
        BigInteger LMinusOne = L.subtract(BigInteger.ONE);
        BigInteger cipherMiunsOne = SymHomSch.EncBiginteger(LMinusOne, verifyParam);
        BigInteger cipherOne = SymHomSch.EncBiginteger(BigInteger.ONE, verifyParam);


        int m = 10001;
        BigInteger c = SymHomSch.EncInt(m, verifyParam);

        BigInteger result = Reencryption(c, cipherMiunsOne, verifyParam);

        System.out.println(SymHomSch.Dec(result, verifyParam));

    }


}

