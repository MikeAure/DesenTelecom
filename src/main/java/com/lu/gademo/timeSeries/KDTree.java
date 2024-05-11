package com.lu.gademo.timeSeries;

import Jama.Matrix;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;


public class KDTree {


    public static Node TreeBuild(HashMap<Integer, int[]> dataList, int upBoundLeafNode, int k) {

        int N = dataList.size();

        if (dataList.size() <= upBoundLeafNode) {

            LeafNode leafNode = new LeafNode(dataList.keySet(), true);
            return leafNode;
        }

        int c = chooseCD(dataList, k);

        int[] dataInC = new int[N];

        int i = 0;
        for (Iterator iterator = dataList.keySet().iterator(); iterator.hasNext(); ) {
            int key = (int) iterator.next();
            dataInC[i] = dataList.get(key)[c];
            i++;
        }

        Arrays.sort(dataInC);
        int max = dataInC[dataInC.length - 1];
        int val = Median(dataInC);

        if (val == max) {

            LeafNode leafNode = new LeafNode(dataList.keySet(), true);
            return leafNode;
        }

        HashMap<Integer, int[]> leftDataList = new HashMap<Integer, int[]>();
        HashMap<Integer, int[]> rightDataList = new HashMap<Integer, int[]>();


        for (Iterator iterator = dataList.keySet().iterator(); iterator.hasNext(); ) {

            int key = (int) iterator.next();
            if (dataList.get(key)[c] <= val) {
                leftDataList.put(key, dataList.get(key));
            } else {
                rightDataList.put(key, dataList.get(key));
            }

        }

        Node left = TreeBuild(leftDataList, upBoundLeafNode, k);
        Node right = TreeBuild(rightDataList, upBoundLeafNode, k);

        InterNode interNode = new InterNode(c, val, left, right, false);

        return interNode;

    }

    public static void printTree(Node root) {

        if (root.isLeafNode) {
            LeafNode leafNode = (LeafNode) root;

            System.out.println("leafnode");
            System.out.println(leafNode.IDSet);
            return;
        }

        if (!root.isLeafNode) {

            InterNode interNode = (InterNode) root;
            int c = interNode.c;
            int val = interNode.val;

            System.out.println("c:" + c + ",val:" + val);
            printTree(interNode.left);
            printTree(interNode.right);

        }

    }


    public static EncryptNode encryptTree(HashMap<Integer, ArrayList<Integer>> TimeSeriesList, Node node, int k, SHSParamters filterParam, SHSParamters VerifyParam, Matrix A) {

        if (node.isLeafNode) {

            LeafNode leafNode = (LeafNode) node;

            HashMap<BigInteger, ArrayList<BigInteger>> encryptTimeSeriesList = new HashMap<BigInteger, ArrayList<BigInteger>>();

            for (Iterator iterator = leafNode.IDSet.iterator(); iterator.hasNext(); ) {

                int ID = (int) iterator.next();
                ArrayList<Integer> timeSeries = TimeSeriesList.get(ID);

                BigInteger enryptID = SymHomSch.EncInt(ID, VerifyParam);
                ArrayList<BigInteger> encryptTimeSeries = EncryptVector(timeSeries, VerifyParam);
                encryptTimeSeriesList.put(enryptID, encryptTimeSeries);

            }

            EncryptLeafNode encryptLeafNode = new EncryptLeafNode(encryptTimeSeriesList, true);

            return encryptLeafNode;
        }

        if (!node.isLeafNode) {

            InterNode interNode = (InterNode) node;
            int c = interNode.c;
            int val = interNode.val;

            BigInteger[] cipherL = EncryptL(c, val, k, A, filterParam);
            BigInteger[] cipherR = EncryptR(c, val, k, A, filterParam);

            EncryptNode left = encryptTree(TimeSeriesList, interNode.left, k, filterParam, VerifyParam, A);
            EncryptNode right = encryptTree(TimeSeriesList, interNode.right, k, filterParam, VerifyParam, A);

            EncryptNode root = new EncryptInterNode(cipherL, cipherR, left, right, false);

            return root;

        }

        return null;
    }


    public static HashMap<BigInteger, ArrayList<BigInteger>> SearchTreeFiltration(EncryptNode root, Token token, SHSParamters FiltrationParam, SHSParamters VerificationParam) {
        BigInteger[] filtrationToken = token.filtrationToken;
        BigInteger encryptLamda = token.encryptLamda;

        HashMap<BigInteger, ArrayList<BigInteger>> distanceList = new HashMap<BigInteger, ArrayList<BigInteger>>();

        Stack S = new Stack<>();
        S.push(root);

        while (S.size() > 0) {

            EncryptNode node = (EncryptNode) S.pop();
            if (node.isLeafNode) {

                EncryptLeafNode encryptLeafNode = (EncryptLeafNode) node;

                HashMap<BigInteger, ArrayList<BigInteger>> encryptVectorList = encryptLeafNode.encryptVectorList;

                for (Iterator iterator = encryptVectorList.keySet().iterator(); iterator.hasNext(); ) {

                    BigInteger key = (BigInteger) iterator.next();
                    ArrayList<BigInteger> record = encryptVectorList.get(key);
                    distanceList.put(key, record);
                }
            } else {

                EncryptInterNode encryptInterNode = (EncryptInterNode) node;
                BigInteger[] cipherL = encryptInterNode.cipherL;
                BigInteger[] cipherR = encryptInterNode.cipherR;


                // Parameter
                BigInteger N = FiltrationParam.N;

                // search left tree
                BigInteger innerProduct = BigInteger.ZERO;
                for (int i = 0; i < cipherL.length; i++) {
                    innerProduct = innerProduct.add(filtrationToken[i].multiply(cipherL[i]).mod(N)).mod(N);
                }

                int sign = BasicProtocol.signComputeProtocol(innerProduct, FiltrationParam);
                if (sign == -1) {
                    S.push(encryptInterNode.left);
                }

                // search right tree
                innerProduct = BigInteger.ZERO;
                for (int i = 0; i < cipherR.length; i++) {
                    innerProduct = innerProduct.add(filtrationToken[i].multiply(cipherR[i]).mod(N)).mod(N);
                }

                sign = BasicProtocol.signComputeProtocol(innerProduct, FiltrationParam);
                if (sign == -1) {
                    S.push(encryptInterNode.right);
                }

            }
        }

        return distanceList;

    }

    public static double SearchTreeVerification(HashMap<BigInteger, ArrayList<BigInteger>> distanceList, Token token, SHSParamters VerificationParam, BigInteger cipherMiunsOne, BigInteger cipherOne, String SecretKeyS1, String SecretKeyS2) {

        BigInteger encryptDelta = token.encryptDelta;
        BigInteger encryptLamda = token.encryptLamda;
        ArrayList<BigInteger> queryRecord = token.verificationToken;
        BigInteger N = VerificationParam.N;
        BigInteger L = VerificationParam.L;


        int k1 = VerificationParam.k1;
        SecureRandom rnd = new SecureRandom();
        BigInteger r1 = BigInteger.ONE;
        BigInteger r2 = BigInteger.ONE;

        ArrayList<String> resultS1 = new ArrayList<String>();
        ArrayList<String> resultS2 = new ArrayList<String>();


        double t = 0.0;
        /*for(int i = 0; i < 20; i++) {*/

        double t1 = System.currentTimeMillis();
        BigInteger encryptID = (BigInteger) distanceList.keySet().toArray()[0];
        BigInteger encryptDistance = TWED.TWEDComputeCipher(queryRecord, distanceList.get(encryptID), encryptLamda, cipherMiunsOne, cipherOne, VerificationParam);
        BigInteger encryptValue = encryptDelta.add(cipherMiunsOne.multiply(encryptDistance).mod(N)).mod(N);

        // server S1
        r1 = (new BigInteger(k1, rnd));
        r2 = (new BigInteger(k1, rnd));
        while (r1.compareTo(r2) == -1 || r1.compareTo(r2) == 0) {
            r2 = (new BigInteger(k1, rnd));
        }

        BigInteger c = encryptValue.multiply(r1).mod(N).add(r2).mod(N);
        //server S2
        BigInteger m = SymHomSch.Dec(c, VerificationParam);

        int sign;
        BigInteger encryptSign = BigInteger.ONE;

        if (m.bitLength() != VerificationParam.L.bitLength()) {
            sign = 1;
            encryptSign = SymHomSch.EncInt(1, VerificationParam);
        } else {
            BigInteger LminusOne = L.subtract(BigInteger.ONE);
            encryptSign = SymHomSch.EncBiginteger(LminusOne, VerificationParam);
        }

        // S1
        encryptID = encryptID.multiply(encryptSign).mod(N);

        r1 = (new BigInteger(k1, rnd));
        r2 = (new BigInteger(k1, rnd));
        while (r1.compareTo(r2) == -1 || r1.compareTo(r2) == 0) {
            r2 = (new BigInteger(k1, rnd));
        }

        c = encryptID.multiply(r1).mod(N).add(r2).mod(N);

        String message1 = r1.toString();
        String message2 = r2.toString();

        String cipher1 = AES.encrypt(message1, SecretKeyS1);
        String cipher2 = AES.encrypt(message2, SecretKeyS1);

        //server S2
        m = SymHomSch.Dec(c, VerificationParam);

        if (m.bitLength() != VerificationParam.L.bitLength()) {
            String cipher3 = AES.encrypt(m.toString(), SecretKeyS2);
            resultS1.add(cipher1);
            resultS1.add(cipher2);
            resultS2.add(cipher3);
        }

	    	/*double t2 = System.currentTimeMillis();
			if(i >= 10) {
				t = t + t2 - t1;
			}
			
		}*/
        System.out.println("result1:" + resultS1.size());
        System.out.println("result2:" + resultS2.size());
        Set<BigInteger> ID = new HashSet<BigInteger>();
        for (int i = 0; i < resultS2.size(); i++) {

            String s1 = AES.decrypt(resultS1.get(2 * i), SecretKeyS1);
            String s2 = AES.decrypt(resultS1.get(2 * i + 1), SecretKeyS1);

            r1 = new BigInteger(s1);
            r2 = new BigInteger(s2);

            String s3 = AES.decrypt(resultS2.get(i), SecretKeyS2);
            BigInteger x = new BigInteger(s3);
            ID.add(x.subtract(r2).divide(r1));
        }
        System.out.println("ID大小：" + ID.size());
        System.out.println("ID内容：");
        for (BigInteger element : ID) {
            System.out.println(element);
            t = element.intValue();
        }

        return t;
    }

    public static Set<BigInteger> QueryResultRecovery(QueryResult result, String SecretKeyS1, String SecretKeyS2) {

        Set<BigInteger> ID = new HashSet<BigInteger>();
        ArrayList<String> resultS1 = result.ResultS1;
        ArrayList<String> resultS2 = result.ResultS2;

        for (int i = 0; i < resultS2.size(); i++) {

            String s1 = AES.decrypt(resultS1.get(2 * i), SecretKeyS1);
            String s2 = AES.decrypt(resultS1.get(2 * i + 1), SecretKeyS1);

            BigInteger r1 = new BigInteger(s1);
            BigInteger r2 = new BigInteger(s2);

            String s3 = AES.decrypt(resultS2.get(i), SecretKeyS2);
            BigInteger x = new BigInteger(s3);
            ID.add(x.subtract(r2).divide(r1));
        }

        return ID;

    }

    /*public static Set<>*/


    public static BigInteger[] EncryptL(int cd, int val, int k, Matrix A, SHSParamters Param) {

        double[] vectorL = new double[2 * k + 1];
        vectorL[2 * cd] = 1.0;
        vectorL[2 * k] = -val;

        Matrix matrixL = new Matrix(vectorL, 1);
        matrixL = matrixL.times(A);

        BigInteger[] cipherL = new BigInteger[2 * k + 1];

        for (int i = 0; i < 2 * k + 1; i++) {
            int data = (int) matrixL.get(0, i);
            cipherL[i] = SymHomSch.EncInt(data, Param);
        }

        return cipherL;

    }

    public static BigInteger[] EncryptR(int cd, int val, int k, Matrix A, SHSParamters Param) {

        // cd \in [0, k-1]

        double[] vectorR = new double[2 * k + 1];
        vectorR[2 * cd + 1] = -1.0;
        vectorR[2 * k] = val;

        Matrix matrixR = new Matrix(vectorR, 1);
        matrixR = matrixR.times(A);

        BigInteger[] cipherR = new BigInteger[2 * k + 1];

        for (int i = 0; i < 2 * k + 1; i++) {
            int data = (int) matrixR.get(0, i);
            cipherR[i] = SymHomSch.EncInt(data, Param);
        }

        return cipherR;

    }


    public static ArrayList<BigInteger> EncryptVector(ArrayList<Integer> vector, SHSParamters Param) {

        ArrayList<BigInteger> CiphertextVector = new ArrayList<BigInteger>();

        for (int i = 0; i < vector.size(); i++) {

            int val = vector.get(i);
            CiphertextVector.add(SymHomSch.EncInt(val, Param));
        }

        return CiphertextVector;

    }


    public static int Median(int[] dataInC) {

        int median;

        int N = dataInC.length;
        if (N % 2 == 0) {
            median = (int) (0.5 * (dataInC[N / 2] + dataInC[N / 2 - 1]));
        } else {
            median = dataInC[N / 2];
        }

        return median;

    }

    public static int chooseCD(HashMap<Integer, int[]> dataList, int k) {

        int c = 0;
        double maxVariance = 0;
        int N = dataList.size();

        for (int i = 0; i < k; i++) {
            int[] data = new int[N];
            int j = 0;
            for (Iterator iterator = dataList.keySet().iterator(); iterator.hasNext(); ) {
                int key = (int) iterator.next();
                data[j] = dataList.get(key)[i];
                j = j + 1;
            }

            double variance = varianceImperative(data);
            if (variance > maxVariance) {
                c = i;
                maxVariance = variance;
            }
        }

        return c;
    }

    public static double varianceImperative(int[] population) {
        double average = 0.0;
        for (int p : population) {
            average += p;
        }
        average /= population.length;

        double variance = 0.0;
        for (int p : population) {
            variance += (p - average) * (p - average);
        }
        return variance;
    }


}
