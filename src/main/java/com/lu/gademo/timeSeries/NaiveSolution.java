package com.lu.gademo.timeSeries;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;


public class NaiveSolution {
	
	public static HashMap<BigInteger, ArrayList<BigInteger>> encryption(HashMap<Integer, ArrayList<Integer>> intDataList, SHSParamters verifyParam) {
		
		HashMap<BigInteger, ArrayList<BigInteger>> encryptTimeSeriesList = new HashMap<BigInteger, ArrayList<BigInteger>>();
		
		for (int i = 0; i < intDataList.size(); i++) {
			
			int ID = i;
			BigInteger enryptID = SymHomSch.EncInt(ID, verifyParam);
			ArrayList<BigInteger> encryptTimeSeries = KDTree.EncryptVector(intDataList.get(i), verifyParam);	
			encryptTimeSeriesList.put(enryptID, encryptTimeSeries);
		}
		
		return encryptTimeSeriesList;
		
	} 
	
	
	public static NaiveToken tokenGeneration(ArrayList<Integer> query, int delta, SHSParamters verifyParam) {
		
		ArrayList<BigInteger> encryptQuery = KDTree.EncryptVector(query, verifyParam);
		BigInteger encryptDelta = SymHomSch.EncInt(delta, verifyParam);
		BigInteger encryptLamda = SymHomSch.EncInt(0, verifyParam);
		
		
		NaiveToken token = new NaiveToken(encryptQuery, encryptDelta, encryptLamda);
		
		return token;
	}
	
	
	public static double QueryProcessing(HashMap<BigInteger, ArrayList<BigInteger>> encryptTimeSeriesList, NaiveToken token, SHSParamters VerificationParam, BigInteger cipherMiunsOne, BigInteger cipherOne, String SecretKeyS1, String SecretKeyS2) {
				
		BigInteger encryptDelta = token.encryptDelta;
		BigInteger N = VerificationParam.N;
		BigInteger L = VerificationParam.L;
		
		
		int k1 = VerificationParam.k1;
		SecureRandom rnd = new SecureRandom();
		BigInteger r1 = BigInteger.ONE;
		BigInteger r2 = BigInteger.ONE;
		
		ArrayList<String> resultS1 = new ArrayList<String>();
		ArrayList<String> resultS2 = new ArrayList<String>();
		
		ArrayList<BigInteger> encryptQuery = token.encryptRecord;
		BigInteger encryptLamda = token.encryptLamda;
		
		double t = 0.0;
					
		for(int i = 0; i < 20; i++) {
			
			double t1 = System.currentTimeMillis();
			BigInteger encryptID = (BigInteger) encryptTimeSeriesList.keySet().toArray()[0];
			BigInteger encryptDistance = TWED.TWEDComputeCipher(encryptQuery, encryptTimeSeriesList.get(encryptID), encryptLamda, cipherMiunsOne, cipherOne, VerificationParam);
			
			BigInteger encryptValue = encryptDelta.add(cipherMiunsOne.multiply(encryptDistance).mod(N)).mod(N);
			
			// server S1
			r1 = (new BigInteger(k1, rnd));
			r2 = (new BigInteger(k1, rnd));
			while(r1.compareTo(r2) == -1 || r1.compareTo(r2) == 0) {
				r2 = (new BigInteger(k1, rnd));
	    	}
			
	    	BigInteger c = encryptValue.multiply(r1).mod(N).add(r2).mod(N);
	    	//server S2
	    	BigInteger m = SymHomSch.Dec(c, VerificationParam);
	    	
	    	int sign;
	    	BigInteger encryptSign = BigInteger.ONE;
	    	
	    	if(m.bitLength() != VerificationParam.L.bitLength()) {
	    		sign = 1;
	    		encryptSign = SymHomSch.EncInt(1, VerificationParam);
	    	}else{
	    		BigInteger LminusOne = L.subtract(BigInteger.ONE);
	    		encryptSign = SymHomSch.EncBiginteger(LminusOne, VerificationParam);
	    	}
	    	
	    	// S1
	    	encryptID = encryptID.multiply(encryptSign).mod(N);
	    	
			r1 = (new BigInteger(k1, rnd));
			r2 = (new BigInteger(k1, rnd));
			while(r1.compareTo(r2) == -1 || r1.compareTo(r2) == 0) {
				r2 = (new BigInteger(k1, rnd));
	    	}
			
	    	c = encryptID.multiply(r1).mod(N).add(r2).mod(N);
	    	
			String message1 = r1.toString();
			String message2 = r2.toString();
			
			String cipher1 = AES.encrypt(message1, SecretKeyS1);
			String cipher2 = AES.encrypt(message2, SecretKeyS1);
			

	    	
	    	//server S2
	    	m = SymHomSch.Dec(c, VerificationParam);
	    		    	
	    	if(m.bitLength() != VerificationParam.L.bitLength()) {
	    		String cipher3 = AES.encrypt(m.toString(), SecretKeyS2);
	    		resultS1.add(cipher1);
	    		resultS1.add(cipher2);
	    		resultS2.add(cipher3);
	    	}
	    	
	    	double t2 = System.currentTimeMillis();
	    	
	    	if(i >= 10) {
	    		 t = t + (t2 - t1);
	    	}	    	
	    	
		}
		
//		QueryResult result = new QueryResult(resultS1, resultS2);
		
		return t/10;
		
	}
	
	
	
	

}
