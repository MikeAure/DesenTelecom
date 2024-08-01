package com.lu.gademo.timeSeries;

import Jama.Matrix;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


public class MainTest {

    public static String SecretKeyS1 = "qwerasdfqwerasds";
    public static String SecretKeyS2 = "uiophjklbnmtyuig";
    public static int upBoundLeafNode = 1;
    public static int lamda = 0;

    public static double Outsourcing(String filename, String split, SHSParamters filterParam, SHSParamters verifyParam, Matrix A, int n, int d, int k, int cycle) throws Exception {

        HashMap<Integer, ArrayList<Double>> doubleDataList = Data.ReadFixNumData(filename, split, n, d);
        HashMap<Integer, ArrayList<Integer>> intDataList = Data.TransformDoubleToInt(doubleDataList, 100);

        double time = 0.0;
        for (int i = 0; i < cycle; i++) {
            double t1 = System.currentTimeMillis();
            HashMap<Integer, ArrayList<Integer>> pivotsList = PreCompute.ChoosePivots(intDataList, k);
            HashMap<Integer, int[]> distanceList = PreCompute.PreComputeDistance(intDataList, pivotsList, lamda);
            Node T = KDTree.TreeBuild(distanceList, upBoundLeafNode, k);
            EncryptNode encryptT = KDTree.encryptTree(intDataList, T, k, filterParam, verifyParam, A);
            double t2 = System.currentTimeMillis();
            if (i > cycle / 2) {
                time = time + (t2 - t1);
            }
        }

        return time * 2 / cycle;
    }

    public static double[] TWEDComputationEach(String filename, String split, SHSParamters filterParam, SHSParamters verifyParam, Matrix A, int n, int d, int k, int delta, int cycle, BigInteger FilterCipherOne, BigInteger FilterCipherZero, BigInteger VerifyCipherOne, BigInteger VerifyCipherZero, BigInteger VerifycipherMiunsOne) throws Exception {
        HashMap<Integer, ArrayList<Double>> doubleDataList = Data.ReadFixNumData(filename, split, n, d);
        HashMap<Integer, ArrayList<Integer>> intDataList = Data.TransformDoubleToInt(doubleDataList, 100);

        // encryption by naive solution
        HashMap<BigInteger, ArrayList<BigInteger>> encryptTimeSeriesList = NaiveSolution.encryption(intDataList, verifyParam);

        // encryption by our scheme
        HashMap<Integer, ArrayList<Integer>> pivotsList = PreCompute.ChoosePivots(intDataList, k);
        HashMap<Integer, int[]> distanceList = PreCompute.PreComputeDistance(intDataList, pivotsList, lamda);
        Node T = KDTree.TreeBuild(distanceList, upBoundLeafNode, k);
        EncryptNode encryptT = KDTree.encryptTree(intDataList, T, k, filterParam, verifyParam, A);

        int loc = (int) (Math.random() * n);
        ArrayList<Integer> q = intDataList.get(loc);

        // token generation by naive solution
        NaiveToken naiveToken = NaiveSolution.tokenGeneration(q, delta, verifyParam);

        // token generation by our scheme
        Token token = Token.generateToken(q, pivotsList, lamda, delta, A, filterParam, verifyParam, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero);

        double[] t = new double[2];
        // query processing with our scheme
        HashMap<BigInteger, ArrayList<BigInteger>> filterResult = KDTree.SearchTreeFiltration(encryptT, token, filterParam, verifyParam);
        t[0] = KDTree.SearchTreeVerification(filterResult, token, verifyParam, VerifycipherMiunsOne, VerifyCipherOne, SecretKeyS1, SecretKeyS2);

        t[1] = NaiveSolution.QueryProcessing(encryptTimeSeriesList, naiveToken, verifyParam, VerifycipherMiunsOne, VerifyCipherOne, SecretKeyS1, SecretKeyS2);

        return t;

    }


    public static int QueryProcessing(ArrayList<Integer> q, String filename, String split, SHSParamters filterParam, SHSParamters verifyParam, Matrix A, int n, int d, int k, int delta, int cycle, BigInteger FilterCipherOne, BigInteger FilterCipherZero, BigInteger VerifyCipherOne, BigInteger VerifyCipherZero, BigInteger VerifycipherMiunsOne) throws Exception {

        HashMap<Integer, ArrayList<Double>> doubleDataList = Data.ReadFixNumData(filename, split, n, d);
        HashMap<Integer, ArrayList<Integer>> intDataList = Data.TransformDoubleToInt(doubleDataList, 100);

        /*for (Map.Entry<Integer, ArrayList<Integer>> entry : intDataList.entrySet()) {
            Integer key = entry.getKey();
            ArrayList<Integer> values = entry.getValue();

            System.out.print("Key: " + key + ", Values: ");
            for (Integer value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }*/

        // encryption by naive solution
        //  HashMap<BigInteger, ArrayList<BigInteger>> encryptTimeSeriesList = NaiveSolution.encryption(intDataList, verifyParam);

        int operationTime = 0;

        //for (int i = 0; i < cycle; i++) {

        // encryption by our scheme
        HashMap<Integer, ArrayList<Integer>> pivotsList = PreCompute.ChoosePivots(intDataList, k);
        HashMap<Integer, int[]> distanceList = PreCompute.PreComputeDistance(intDataList, pivotsList, lamda);
        Node T = KDTree.TreeBuild(distanceList, upBoundLeafNode, k);
        EncryptNode encryptT = KDTree.encryptTree(intDataList, T, k, filterParam, verifyParam, A);

        //for (int j = 0; j < 20; j++) {

        //int loc = (int) (Math.random() * n);
               /* ArrayList<Integer> q = intDataList.get(loc);
        System.out.println("raw:" + loc);
        System.out.println(q);*/
        //System.out.println(q);
        //q.remove(q.size()-1);
        //ArrayList<Integer> q = new ArrayList<>(java.util.Arrays.asList(4, 5, 6, 7, 8, 9, 10,123));

        // token generation by our scheme
        Token token = Token.generateToken(q, pivotsList, lamda, delta, A, filterParam, verifyParam, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero);

        // query processing with our scheme
        HashMap<BigInteger, ArrayList<BigInteger>> filterResult = KDTree.SearchTreeFiltration(encryptT, token, filterParam, verifyParam);
        System.out.println("operationTime:" + filterResult.size());


        operationTime = operationTime + filterResult.size();

        //}
        int index = (int) KDTree.SearchTreeVerification(filterResult, token, verifyParam, VerifycipherMiunsOne, VerifyCipherOne, SecretKeyS1, SecretKeyS2);
        System.out.println(index);
//        operationTime = operationTime / cycle / 20;
        operationTime = operationTime / cycle;
        System.out.println(doubleDataList.get(index).toString());
        System.out.println(intDataList.get(index).toString());

        //System.out.println("current parameter: n = " + n + ", d = " + d + ", k = " + k + ", delta = " + delta + ", cycle = " + cycle + ", operation time: " + operationTime);

        //return operationTime;
        return index + 1;
    }


    public static double QueryTokenGen(String filename, String split, SHSParamters filterParam, SHSParamters verifyParam, Matrix A, int n, int d, int k, int delta, int cycle, BigInteger FilterCipherOne, BigInteger FilterCipherZero, BigInteger VerifyCipherOne, BigInteger VerifyCipherZero) throws Exception {

        HashMap<Integer, ArrayList<Double>> doubleDataList = Data.ReadFixNumData(filename, split, n, d);
        HashMap<Integer, ArrayList<Integer>> intDataList = Data.TransformDoubleToInt(doubleDataList, 100);
        HashMap<Integer, ArrayList<Integer>> pivotsList = PreCompute.ChoosePivots(intDataList, k);

        HashMap<Integer, int[]> distanceList = PreCompute.PreComputeDistance(intDataList, pivotsList, lamda);
        Node T = KDTree.TreeBuild(distanceList, upBoundLeafNode, k);
        EncryptNode encryptT = KDTree.encryptTree(intDataList, T, k, filterParam, verifyParam, A);

        double time = 0.0;
        for (int i = 0; i < cycle; i++) {

            double t1 = System.nanoTime();
            int loc = (int) (Math.random() * n);
            ArrayList<Integer> q = intDataList.get(loc);
            Token token = Token.generateToken(q, pivotsList, lamda, delta, A, filterParam, verifyParam, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero);
            double t2 = System.nanoTime();

            if (i > cycle / 2) {
                time = time + (t2 - t1);
            }

        }

        return time * 2 / cycle;
    }


    // Outsourcing with N
    public static void OutsourcingN(String filename, String split, SHSParamters filterParam, SHSParamters verifyParam) throws Exception {

        int[] n = {5000, 10000, 15000};
        int d = 96;
        int[] k = {4, 6, 8, 10};

        int cycle = 20;

        System.out.println("Outsourcing time with N and K");

        double[][] time = new double[k.length][n.length];

        for (int i = 0; i < k.length; i++) {

            Matrix A = Matrix.identity(2 * k[i] + 1, 2 * k[i] + 1);
            for (int j = 0; j < n.length; j++) {
                time[i][j] = Outsourcing(filename, split, filterParam, verifyParam, A, n[j], d, k[i], cycle);
            }
        }

        for (int j = 0; j < n.length; j++) {
            System.out.printf(" \t N = %d", n[j]);
        }
        System.out.println();

        for (int i = 0; i < k.length; i++) {
            System.out.printf("K = %d \t", k[i]);
            for (int j = 0; j < n.length; j++) {
                System.out.printf("%f \t", time[i][j]);
            }

            System.out.println();
        }

    }

    // Outsourcing with D
    public static void OutsourcingD(String filename, String split, SHSParamters filterParam, SHSParamters verifyParam) throws Exception {

        int n = 16637;
        int[] d = {30, 60, 90};
        int[] k = {4, 6, 8, 10};

        int cycle = 20;
        double[][] time = new double[k.length][d.length];

        System.out.println("Outsourcing time with D and K");


        for (int i = 0; i < k.length; i++) {
            Matrix A = Matrix.identity(2 * k[i] + 1, 2 * k[i] + 1);

            for (int j = 0; j < d.length; j++) {
                time[i][j] = Outsourcing(filename, split, filterParam, verifyParam, A, n, d[j], k[i], cycle);
            }
        }

        for (int j = 0; j < d.length; j++) {
            System.out.printf(" \t D = %d", d[j]);
        }
        System.out.println();

        for (int i = 0; i < k.length; i++) {
            System.out.printf("K = %d \t", k[i]);
            for (int j = 0; j < d.length; j++) {
                System.out.printf("%f \t", time[i][j]);
            }

            System.out.println();
        }


    }

    // QueryProcessing with N
    public static int QueryProcessingN(ArrayList<Integer> q, String filename, String split, SHSParamters filterParam, SHSParamters verifyParam, BigInteger FilterCipherOne, BigInteger FilterCipherZero, BigInteger VerifyCipherOne, BigInteger VerifyCipherZero, BigInteger VerifycipherMiunsOne) throws Exception {

       /* int[] n = {2000, 4000, 6000, 8000, 10000, 12000, 14000, 16000};
        int d = 96;
        int[] k = {4, 6, 8, 10};

        int delta = 350;
        int cycle = 25;*/

        int[] n = {2000};
        int d = 48;
        int[] k = {4};
        int delta = 350;
        int cycle = 1;


//		double[] time = new double[2];
        double[][] operationTimes = new double[k.length][n.length];

        System.out.println("Query Processing time with N and K");
        Matrix A = Matrix.identity(2 * k[0] + 1, 2 * k[0] + 1);
//		time = TWEDComputationEach(filename, split, filterParam, verifyParam, A, n[0], d, k[0], delta, cycle, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMiunsOne);

        int index = 0;
        for (int i = 0; i < k.length; i++) {
            A = Matrix.identity(2 * k[i] + 1, 2 * k[i] + 1);
            for (int j = 0; j < n.length; j++) {
                operationTimes[i][j] = QueryProcessing(q, filename, split, filterParam, verifyParam, A, n[j], d, k[i], delta, cycle, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMiunsOne);
                index = (int) operationTimes[i][j];
            }
        }

//		System.out.println("basic operation time in our scheme:" + time[0]);
//		System.out.println("basic operation time in naive solution:" + time[1]);

        for (int j = 0; j < n.length; j++) {
            System.out.printf(" \t N = %d", n[j]);
        }
        System.out.println();

        for (int i = 0; i < k.length; i++) {
            System.out.printf("K = %d \t", k[i]);
            for (int j = 0; j < n.length; j++) {
                System.out.printf("%f \t", operationTimes[i][j]);
            }

            System.out.println();
        }

        return index;
    }

    // QueryProcessing with D
    public static void QueryProcessingD(String filename, String split, SHSParamters filterParam, SHSParamters verifyParam, BigInteger FilterCipherOne, BigInteger FilterCipherZero, BigInteger VerifyCipherOne, BigInteger VerifyCipherZero, BigInteger VerifycipherMiunsOne) throws Exception {

        int n = 16637;
        int[] d = {80, 84, 88, 92, 96};
        int[] k = {4, 6, 8, 10};
        int delta = 300;

        int cycle = 25;

//		double[][] time = new double[d.length][2];
//		
//		for(int i = 0; i < d.length; i++) {
//			Matrix A = Matrix.identity(2*k[0] + 1, 2*k[0] + 1);
//			for(int j = 0; j < 2; j++) {
//				double[] t = TWEDComputationEach(filename, split, filterParam, verifyParam, A, n, d[i], k[0], delta, cycle, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMiunsOne);
//				time[i][0] = t[0];
//				time[i][1] = t[1];
//			}
//		}
//		
//		System.out.println("basic operation time \t our scheme \t naive solution\n");
//		for(int i = 0; i < d.length; i++) {
//			for(int j = 0; j < 2; j++) {
//				System.out.printf("\t %f \t %f \n", time[i][0], time[i][1]);
//			}
//		}

        double[][] operationTimes = new double[k.length][d.length];
        System.out.println("Query Processing time with D and K");

        for (int i = 0; i < k.length; i++) {
            Matrix A = Matrix.identity(2 * k[i] + 1, 2 * k[i] + 1);
            for (int j = 0; j < d.length; j++) {
                //operationTimes[i][j] = QueryProcessing(filename, split, filterParam, verifyParam, A, n, d[j], k[i], delta, cycle, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMiunsOne);
            }
        }


        System.out.println("number of operations");
        for (int j = 0; j < d.length; j++) {
            System.out.printf(" \t D = %d", d[j]);
        }
        System.out.println();

        for (int i = 0; i < k.length; i++) {
            System.out.printf("K = %d \t", k[i]);
            for (int j = 0; j < d.length; j++) {
                System.out.printf("%f \t", operationTimes[i][j]);
            }

            System.out.println();
        }


    }


    // QueryProcessing with Delta
    public static void QueryProcessingDelta(String filename, String split, SHSParamters filterParam, SHSParamters verifyParam, BigInteger FilterCipherOne, BigInteger FilterCipherZero, BigInteger VerifyCipherOne, BigInteger VerifyCipherZero, BigInteger VerifycipherMiunsOne) throws Exception {

        int n = 16637;
        int d = 96;
        int[] k = {4, 6, 8, 10};
        int[] delta = {300, 320, 340, 360, 380, 400};

        int cycle = 25;

        double[] time = new double[2];
        Matrix A = Matrix.identity(2 * k[0] + 1, 2 * k[0] + 1);
//		time = TWEDComputationEach(filename, split, filterParam, verifyParam, A, n, d, k[0], delta[0], cycle, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMiunsOne);

        double[][] operationTimes = new double[k.length][delta.length];
        System.out.println("Query Processing time with Delta and K");


        for (int i = 0; i < k.length; i++) {
            A = Matrix.identity(2 * k[i] + 1, 2 * k[i] + 1);
            for (int j = 0; j < delta.length; j++) {
                //operationTimes[i][j] = QueryProcessing(filename, split, filterParam, verifyParam, A, n, d, k[i], delta[j], cycle, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMiunsOne);
            }
        }

//		System.out.println("basic operation time in our scheme:" + time[0]);
//		System.out.println("basic operation time in naive solution:" + time[1]);


        for (int j = 0; j < delta.length; j++) {
            System.out.printf(" \t Delta = %d", delta[j]);
        }
        System.out.println();

        for (int i = 0; i < k.length; i++) {
            System.out.printf("K = %d \t", k[i]);
            for (int j = 0; j < delta.length; j++) {
                System.out.printf("%f \t", operationTimes[i][j]);

            }

            System.out.println();
        }

    }


    // Token Generation with D
    public static void TokengenD(String filename, String split, SHSParamters filterParam, SHSParamters verifyParam, BigInteger FilterCipherOne, BigInteger FilterCipherZero, BigInteger VerifyCipherOne, BigInteger VerifyCipherZero) throws Exception {

        int n = 16637;
        int[] d = {80, 84, 88, 92, 96};
        int[] k = {4, 6, 8, 10};
        int delta = 350;
        int cycle = 4000;

        double[][] time = new double[k.length][d.length];


        System.out.println("Token Generation time with D and K");


        for (int i = 0; i < k.length; i++) {
            Matrix A = Matrix.identity(2 * k[i] + 1, 2 * k[i] + 1);
            for (int j = 0; j < d.length; j++) {
                time[i][j] = QueryTokenGen(filename, split, filterParam, verifyParam, A, n, d[j], k[i], delta, cycle, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero);
            }
        }

        for (int j = 0; j < d.length; j++) {
            System.out.printf(" \t D = %d", d[j]);
        }
        System.out.println();

        for (int i = 0; i < k.length; i++) {
            System.out.printf("K = %d \t", k[i]);
            for (int j = 0; j < d.length; j++) {
                System.out.printf("%f \t", time[i][j]);
            }

            System.out.println();
        }


    }


    public static void main(String[] args) throws Exception {

        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        /*"time" + "ElectricDevices_TEST.tsv"*/
        String filename = Paths.get(currentPath, "ElectricDevices_TEST.tsv").toString();
        String split = "\\s+";


        // filtration parameters

        int filter_k1 = 20;
        int filter_k2 = 80;
        int filter_k0 = 1024;

        SHSParamters filterParam = SymHomSch.KeyGen(filter_k0, filter_k1, filter_k2);

        // verification parameters
        int verify_k1 = 20;
        int verify_k2 = 80;
        int verify_k0 = 1024;

        SHSParamters verifyParam = SymHomSch.KeyGen(verify_k0, verify_k1, verify_k2);
        BigInteger L = verifyParam.L;
        BigInteger LMinusOne = L.subtract(BigInteger.ONE);

        BigInteger VerifycipherMinusOne = SymHomSch.EncBiginteger(LMinusOne, verifyParam);

        BigInteger FilterCipherOne = SymHomSch.EncBiginteger(BigInteger.ONE, filterParam);
        BigInteger FilterCipherZero = SymHomSch.EncBiginteger(BigInteger.ZERO, filterParam);
        BigInteger VerifyCipherOne = SymHomSch.EncBiginteger(BigInteger.ONE, verifyParam);
        BigInteger VerifyCipherZero = SymHomSch.EncBiginteger(BigInteger.ZERO, verifyParam);


//		OutsourcingN(filename, split, filterParam, verifyParam);
//		OutsourcingD(filename, split, filterParam, verifyParam);

        //QueryProcessingN(filename, split, filterParam, verifyParam, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMinusOne);
        //QueryProcessingDelta(filename, split, filterParam, verifyParam, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMinusOne);

//		TokengenD(filename, split, filterParam, verifyParam, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero);
        //QueryProcessingD(filename, split, filterParam, verifyParam, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMinusOne);


    }

    public static String encryptGraph(String rawData) throws Exception {


        // 创建 ArrayList
        ArrayList<Integer> q = new ArrayList<>();

        // 将字符串分割为数值数组
        String[] numericArray = rawData.split(",");

        // 将数组中的每个元素转换为整数并添加到 ArrayList
        for (String numericValue : numericArray) {
            try {
                int value = Integer.parseInt(numericValue);
                q.add(value);
            } catch (NumberFormatException e) {
                // 处理无法解析为整数的情况，可以选择忽略或进行其他处理
                System.out.println("无法解析为整数: " + numericValue);
            }
        }

        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        /*"time" + "ElectricDevices_TEST.tsv"*/
        String filename = Paths.get(currentPath, "ElectricDevices_TEST.tsv").toString();
        String split = "\\s+";


        // filtration parameters

        int filter_k1 = 20;
        int filter_k2 = 80;
        int filter_k0 = 1024;

        SHSParamters filterParam = SymHomSch.KeyGen(filter_k0, filter_k1, filter_k2);

        // verification parameters
        int verify_k1 = 20;
        int verify_k2 = 80;
        int verify_k0 = 1024;

        SHSParamters verifyParam = SymHomSch.KeyGen(verify_k0, verify_k1, verify_k2);
        BigInteger L = verifyParam.L;
        BigInteger LMinusOne = L.subtract(BigInteger.ONE);

        BigInteger VerifycipherMinusOne = SymHomSch.EncBiginteger(LMinusOne, verifyParam);

        BigInteger FilterCipherOne = SymHomSch.EncBiginteger(BigInteger.ONE, filterParam);
        BigInteger FilterCipherZero = SymHomSch.EncBiginteger(BigInteger.ZERO, filterParam);
        BigInteger VerifyCipherOne = SymHomSch.EncBiginteger(BigInteger.ONE, verifyParam);
        BigInteger VerifyCipherZero = SymHomSch.EncBiginteger(BigInteger.ZERO, verifyParam);


//		OutsourcingN(filename, split, filterParam, verifyParam);
//		OutsourcingD(filename, split, filterParam, verifyParam);

        return String.valueOf(QueryProcessingN(q, filename, split, filterParam, verifyParam, FilterCipherOne, FilterCipherZero, VerifyCipherOne, VerifyCipherZero, VerifycipherMinusOne));

    }


}
