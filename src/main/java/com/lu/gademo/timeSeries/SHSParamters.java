package com.lu.gademo.timeSeries;

import java.math.BigInteger;

public class SHSParamters {

    public int k0; //1024; Length of large prime numbers p and q
    public int k1; //30; Length of message and message space
    public int k2; //80; Length of parameter L and generated random values in encryption method

    public BigInteger p;
    public BigInteger q;
    public BigInteger N;
    public BigInteger L;

    public SHSParamters(int k0, int k1, int k2, BigInteger p, BigInteger q, BigInteger N, BigInteger L) {

        this.k0 = k0;
        this.k1 = k1;
        this.k2 = k2;
        this.p = p;
        this.q = q;
        this.N = N;
        this.L = L;

    }


}
