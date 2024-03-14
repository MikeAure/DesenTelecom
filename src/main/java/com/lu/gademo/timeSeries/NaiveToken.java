package com.lu.gademo.timeSeries;

import java.math.BigInteger;
import java.util.ArrayList;


public class NaiveToken {
	
	public ArrayList<BigInteger> encryptRecord;
	public BigInteger encryptDelta;
	public BigInteger encryptLamda;
	
	
	public NaiveToken(ArrayList<BigInteger> encryptRecord, BigInteger encryptDelta, BigInteger encryptLamda) {
		super();
		this.encryptRecord = encryptRecord;
		this.encryptDelta = encryptDelta;
		this.encryptLamda = encryptLamda;
	}
	
	
	

}
