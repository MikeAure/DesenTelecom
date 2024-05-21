package com.lu.gademo.timeSeries;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;


public class TWED {

    public static int TWEDComputePlain(ArrayList<Integer> t1, ArrayList<Integer> t2, int lamda) {

        int l1 = t1.size();
        int l2 = t2.size();

        int[][] distance = new int[l1][l2];
        distance[0][0] = Math.abs(t1.get(0) - t2.get(0));

        for (int i = 1; i < l1; i++) {
            distance[i][0] = distance[i - 1][0] + Math.abs(t1.get(i) - t1.get(i - 1)) + lamda;
        }

        for (int j = 1; j < l2; j++) {
            distance[0][j] = distance[0][j - 1] + Math.abs(t2.get(j) - t2.get(j - 1)) + lamda;
        }

        int d1, d2, d3, d;
        for (int i = 1; i < l1; i++) {
            for (int j = 1; j < l2; j++) {
                // delete t1
                d1 = distance[i - 1][j] + Math.abs(t1.get(i) - t1.get(i - 1)) + lamda;
                // match
                d2 = distance[i - 1][j - 1] + Math.abs(t1.get(i) - t2.get(j)) + Math.abs(t1.get(i - 1) - t2.get(j - 1));
                // delete t2
                d3 = distance[i][j - 1] + Math.abs(t2.get(j) - t2.get(j - 1)) + lamda;

                d = Math.min(d1, d2);
                d = Math.min(d, d3);

                distance[i][j] = d;

            }
        }


        return distance[l1 - 1][l2 - 1];

    }

    public static BigInteger TWEDComputeCipher(ArrayList<BigInteger> t1, ArrayList<BigInteger> t2, BigInteger encryptLamda, BigInteger cipherMiunsOne, BigInteger cipherOne, SHSParamters Param) {


        BigInteger N = Param.N;

        int l1 = t1.size();
        int l2 = t2.size();


        BigInteger[][] distance = new BigInteger[l1][l2];
        BigInteger absVal;

        distance[0][0] = BasicProtocol.absoluteComputeProtocol(t1.get(0), t2.get(0), cipherMiunsOne, cipherOne, Param);

        for (int i = 1; i < l1; i++) {
            absVal = BasicProtocol.absoluteComputeProtocol(t1.get(i), t1.get(i - 1), cipherMiunsOne, cipherOne, Param);
            distance[i][0] = distance[i - 1][0].add(absVal).add(encryptLamda).mod(Param.N);
            if (i % 3 == 0) {
                distance[i][0] = BasicProtocol.Reencryption(distance[i][0], cipherMiunsOne, Param);
            }
        }

        for (int j = 1; j < l2; j++) {
            absVal = BasicProtocol.absoluteComputeProtocol(t2.get(j), t2.get(j - 1), cipherMiunsOne, cipherOne, Param);
            distance[0][j] = distance[0][j - 1].add(absVal).add(encryptLamda).mod(Param.N);
            if (j % 3 == 0) {
                distance[0][j] = BasicProtocol.Reencryption(distance[0][j], cipherMiunsOne, Param);
            }
        }

        BigInteger d1, d2, d3, d;
        for (int i = 1; i < l1; i++) {
            for (int j = 1; j < l2; j++) {

                // delete t1
                absVal = BasicProtocol.absoluteComputeProtocol(t1.get(i), t1.get(i - 1), cipherMiunsOne, cipherOne, Param);
                d1 = distance[i - 1][j].add(absVal).add(encryptLamda).mod(N);


                // match
                absVal = BasicProtocol.absoluteComputeProtocol(t1.get(i), t2.get(j), cipherMiunsOne, cipherOne, Param);
                d2 = distance[i - 1][j - 1].add(absVal).mod(N);
                absVal = BasicProtocol.absoluteComputeProtocol(t1.get(i - 1), t2.get(j - 1), cipherMiunsOne, cipherOne, Param);
                d2 = d2.add(absVal).mod(N);

                // delete t2
                absVal = BasicProtocol.absoluteComputeProtocol(t2.get(j), t2.get(j - 1), cipherMiunsOne, cipherOne, Param);
                d3 = distance[i][j - 1].add(absVal).add(encryptLamda).mod(N);

                d = BasicProtocol.minimumComputeProtocol(d1, d2, cipherMiunsOne, cipherOne, Param);
                d = BasicProtocol.minimumComputeProtocol(d, d3, cipherMiunsOne, cipherOne, Param);

//				if((i + j) % 2 == 0) {
//					System.out.println("before:" + SymHomSch.Dec(d, Param));
                d = BasicProtocol.Reencryption(d, cipherMiunsOne, Param);
//					System.out.println("after:" + SymHomSch.Dec(d, Param));
//				}

                distance[i][j] = d;


            }
        }

        return distance[l1 - 1][l2 - 1];
    }

    public static void main(String[] args) throws Exception {


        String filename = "src/ElectricDevices_TEST.tsv";
        String split = "\\s+";

        int n = 100;
        int[] l = {10, 20, 30, 40, 50, 60, 70, 80, 90};
        int cycle = 30;

        int lamda = 0;
        int verify_k1 = 20;
        int verify_k2 = 80;
        int verify_k0 = 1024;

        SHSParamters verifyParam = SymHomSch.KeyGen(verify_k0, verify_k1, verify_k2);
        BigInteger L = verifyParam.L;
        BigInteger LMinusOne = L.subtract(BigInteger.ONE);
        BigInteger cipherMiunsOne = SymHomSch.EncBiginteger(LMinusOne, verifyParam);
        BigInteger cipherOne = SymHomSch.EncBiginteger(BigInteger.ONE, verifyParam);
        BigInteger encryptLamda = SymHomSch.EncInt(lamda, verifyParam);

        System.out.print("\t The dimension \t Time\n");
        for (int v = 0; v < l.length; v++) {

            double t = 0.0;
            HashMap<Integer, ArrayList<Double>> dataList = Data.ReadFixNumData(filename, split, n, l[v]);

            HashMap<Integer, ArrayList<Integer>> intDataList = Data.TransformDoubleToInt(dataList, 100);

            HashMap<Integer, ArrayList<BigInteger>> encryptedDataList = new HashMap<Integer, ArrayList<BigInteger>>();

            for (int i = 0; i < intDataList.size(); i++) {
                ArrayList<BigInteger> encryptedRecord = new ArrayList<BigInteger>();
                for (int j = 0; j < intDataList.get(i).size(); j++) {
                    BigInteger cipher = SymHomSch.EncInt(intDataList.get(i).get(j), verifyParam);
                    encryptedRecord.add(cipher);
                }
                encryptedDataList.put(i, encryptedRecord);
            }


            for (int w = 0; w < cycle; w++) {
                int loc1 = (int) (Math.random() * n);
                int loc2 = (int) (Math.random() * n);

                double t1 = System.currentTimeMillis();
                BigInteger x = TWED.TWEDComputeCipher(encryptedDataList.get(loc1), encryptedDataList.get(loc2), encryptLamda, cipherMiunsOne, cipherOne, verifyParam);
                double t2 = System.currentTimeMillis();

                if (w >= cycle / 2) {
                    t = t + t2 - t1;
                }
            }


            System.out.printf("\t d = %d \t %f\n", l[v], (t * 2 / cycle));

        }


    }
}
