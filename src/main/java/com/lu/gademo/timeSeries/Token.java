package com.lu.gademo.timeSeries;

import Jama.Matrix;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;


public class Token {

    public BigInteger[] filtrationToken;
    public ArrayList<BigInteger> verificationToken;
    public BigInteger encryptDelta;
    public BigInteger encryptLamda;

    public Token(BigInteger[] filtrationToken, ArrayList<BigInteger> verificationToken, BigInteger encryptDelta, BigInteger encryptLamda) {
        super();
        this.filtrationToken = filtrationToken;
        this.verificationToken = verificationToken;
        this.encryptDelta = encryptDelta;
        this.encryptLamda = encryptLamda;
    }


    public static Token generateToken(ArrayList<Integer> q, HashMap<Integer, ArrayList<Integer>> pivots, int lamda, Integer delta, Matrix A, SHSParamters FiltrationParam, SHSParamters VerificationParam, BigInteger FilterCipherOne, BigInteger FilterCipherZero, BigInteger VerifyCipherOne, BigInteger VerifyCipherZero) {


        int k = pivots.size();
        BigInteger L = FiltrationParam.L;

        // Precompute distances
        int[] preComputeDistance = new int[k];
        for (int i = 0; i < k; i++) {
            preComputeDistance[i] = TWED.TWEDComputePlain(q, pivots.get(i), lamda);
        }

        double[] queryRange = new double[2 * k + 1];
        for (int i = 0; i < k; i++) {
            queryRange[2 * i] = preComputeDistance[i] - delta;
            queryRange[2 * i + 1] = preComputeDistance[i] + delta;
        }
        queryRange[2 * k] = 1;

        // generate filtration token
        Matrix queryRangeMatrix = new Matrix(queryRange, 1);
        queryRangeMatrix = queryRangeMatrix.times(A);

        BigInteger[] FiltrationToken = new BigInteger[2 * k + 1];
        for (int i = 0; i < 2 * k + 1; i++) {
            int data = (int) queryRangeMatrix.get(0, i);
            FiltrationToken[i] = SymHomSch.EncWithCipher(data, FiltrationParam, FilterCipherOne, FilterCipherZero);
        }

        // generate verification token
        int lq = q.size();
        ArrayList<BigInteger> VerificationToken = new ArrayList<BigInteger>();
        for (int i = 0; i < lq; i++) {
            VerificationToken.add(SymHomSch.EncWithCipher(q.get(i), VerificationParam, VerifyCipherOne, VerifyCipherZero));
        }

        BigInteger encryptDelta = SymHomSch.EncInt(delta, VerificationParam);
        BigInteger encryptLamda = SymHomSch.EncInt(lamda, VerificationParam);

        Token token = new Token(FiltrationToken, VerificationToken, encryptDelta, encryptLamda);

        return token;


    }

}
