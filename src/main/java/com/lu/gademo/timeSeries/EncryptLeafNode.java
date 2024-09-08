package com.lu.gademo.timeSeries;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;


public class EncryptLeafNode extends EncryptNode {

    // the first element is the encrypted identity
    public HashMap<BigInteger, ArrayList<BigInteger>> encryptVectorList;

    public EncryptLeafNode(HashMap<BigInteger, ArrayList<BigInteger>> encryptVectorList, boolean isLeafNode) {
        super();
        this.encryptVectorList = encryptVectorList;
        this.isLeafNode = isLeafNode;
    }


}
