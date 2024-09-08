package com.lu.gademo.timeSeries;

import lombok.ToString;

import java.math.BigInteger;

@ToString
public class EncryptInterNode extends EncryptNode {

    public BigInteger[] cipherL;
    public BigInteger[] cipherR;
    public EncryptNode left;
    public EncryptNode right;


    public EncryptInterNode(BigInteger[] cipherL, BigInteger[] cipherR, EncryptNode left, EncryptNode right, boolean isLeafNode) {

        this.cipherL = cipherL;
        this.cipherR = cipherR;
        this.left = left;
        this.right = right;
        this.isLeafNode = isLeafNode;
    }


}
