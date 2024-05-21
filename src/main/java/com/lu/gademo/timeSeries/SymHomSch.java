package com.lu.gademo.timeSeries;

import java.math.BigInteger;
import java.security.SecureRandom;

//SHS = Symmetric Homomorphic Scheme
public class SymHomSch {

    public static SecureRandom rnd = new SecureRandom();
    public static int k0;
    public static int k1;
    public static int k2;

    public static SHSParamters KeyGen(int k0, int k1, int k2) {

        BigInteger p = new BigInteger(k0, 40, rnd); // Certainty = 40
        BigInteger q = new BigInteger(k0, 40, rnd); // Certainty = 40
        BigInteger N = p.multiply(q);
        BigInteger L = new BigInteger(k2, rnd); //L in {1,2,3,..., 2^k2}; e.g., k2=80;

        SHSParamters Param = new SHSParamters(k0, k1, k2, p, q, N, L);

        return Param;

    }

    public static BigInteger EncInt(int val, SHSParamters Param) {

        int k0 = Param.k0;
        int k1 = Param.k1;
        int k2 = Param.k2;

        BigInteger p = Param.p;
        BigInteger L = Param.L;
        BigInteger N = Param.N;

        BigInteger message = new BigInteger(Integer.toString(val));

        BigInteger r = new BigInteger(k2, rnd);
//    	System.out.println("r1:" + r);
        BigInteger rp = (new BigInteger(k0, rnd));

        return (((r.multiply(L)).add(message)).multiply((BigInteger.ONE).add(rp.multiply(p)))).mod(N);

    }

    public static BigInteger EncBiginteger(BigInteger val, SHSParamters Param) {

        int k0 = Param.k0;
        int k1 = Param.k1;
        int k2 = Param.k2;

        BigInteger p = Param.p;
        BigInteger L = Param.L;
        BigInteger N = Param.N;


        BigInteger r = new BigInteger(k2, rnd);
//    	System.out.println("r1:" + r);
        BigInteger rp = (new BigInteger(k0, rnd));

        return (((r.multiply(L)).add(val)).multiply((BigInteger.ONE).add(rp.multiply(p)))).mod(N);

    }

    public static BigInteger EncWithCipher(int val, SHSParamters Param, BigInteger cipherOne, BigInteger cipherZero) {

        int k1 = Param.k1;
        BigInteger N = Param.N;


        BigInteger r = new BigInteger(k1, rnd);
        BigInteger c = new BigInteger(Integer.toString(val));
        c = c.multiply(cipherOne).mod(N);
        c = c.add(r.multiply(cipherZero)).mod(N);

        return c;

    }


    public static BigInteger Dec(BigInteger cipher, SHSParamters Param) {

        BigInteger p = Param.p;
        BigInteger L = Param.L;

        BigInteger res = cipher.mod(p);
//    	System.out.println("after p:" + res);

        res = res.mod(L);
//    	System.out.println("after L:" + res);

        return res;

    }


    public static void main(String[] args) {

        // filtration parameters
        int filter_k1 = 20;
        int filter_k2 = 80;
        int filter_k0 = 1024;

        SHSParamters filterParam = SymHomSch.KeyGen(filter_k0, filter_k1, filter_k2);

        BigInteger cipherMinusOne = SymHomSch.EncInt(-1, filterParam);

        BigInteger m1 = SymHomSch.EncInt(1000, filterParam);
        BigInteger m2 = SymHomSch.EncInt(100, filterParam);

        BigInteger x = m1.add(m2.multiply(cipherMinusOne));

        x = x.add(m2.multiply(cipherMinusOne));

        System.out.println(SymHomSch.Dec(x, filterParam));


    }
}
