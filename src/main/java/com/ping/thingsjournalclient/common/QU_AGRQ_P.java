package com.ping.thingsjournalclient.common;

import com.ping.thingsjournalclient.util.SMUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;


public class QU_AGRQ_P {

    private static final int thresholdValue = 20000;//时延门限值
    private static final String symbol1 = "%%";//分隔符1
    private static final String symbol2 = "&&";//分隔符2
    private static final String symbol3 = "@@";//分隔符3
    private final int[] k = new int[4];//参数K
    private final BigInteger[] c_in = new BigInteger[6];//数组c
    private BigInteger s, s_Inverse, p, alpha;//参数s，s的逆，p,α
    private BigInteger r_i;//数组r
    private String key_QU;//会话密钥
    private String QUID;//用户表示


    public QU_AGRQ_P() {
//		init();
        this("BBF4244CECAAE7FC3F05329D2F279A7B", "1");
    }

    public QU_AGRQ_P(String key_QU, String QUID) {
        this.key_QU = key_QU;
        this.QUID = QUID;
        init();
    }

//	public 
//	public QU_AGRQ_P()

    public static void main(String[] args) throws Exception {
        boolean c = true;
        int i = 0;
        while (c) {
            i++;
            QU_AGRQ_P test = new QU_AGRQ_P(SMUtils.sm4generateKey(), "1");
            ArrayList<Vertex> testVertex = new ArrayList();
            testVertex.add(new Vertex(1, 1));
            testVertex.add(new Vertex(101, 1));
            testVertex.add(new Vertex(101, 101));
            testVertex.add(new Vertex(1, 101));
            String a = test.QU_AGRQ_P_QDC(testVertex);
            String b = test.UF_AGRQ_P_RDC(a, new Vertex(70, 70));

            c = test.QU_AGRQ_P_QRR(b);
            if (i == 50) {
                break;
            }
        }
        System.out.println(i);
    }

    public String getKey_QU() {
        return key_QU;
    }

    public void setKey_QU(String key_QU) {
        this.key_QU = key_QU;
    }

    public String getQUID() {
        return QUID;
    }

    public void setQUID(String qUID) {
        QUID = qUID;
    }

    /**
     * 系统必要初始化
     */
    private void init() {
        k[0] = 512;
        k[1] = 160;
        k[2] = 75;
        k[3] = 75;

		/*
		p=BigInteger.probablePrime(k[0], new Random(System.currentTimeMillis()));//k1 p
		alpha=BigInteger.probablePrime(k[1], new Random(System.currentTimeMillis()));//k2 alpha
		s=new BigInteger(70, new Random(System.currentTimeMillis()));//s取值问题
		s_Inverse=s.modInverse(p);
		*/
        s = new BigInteger("504994818564917003702");
        alpha = new BigInteger("1000286945907146489561925541631911126123120564769");
        p = new BigInteger("7463198780056285544145239772801612205520503806165531418261541054588774004271514418661269602558830075009772464570653898278021471788183550198819818819909761");
        s_Inverse = new BigInteger("4598453610455512631565011695692117160861716460817154970305475701194235436028198518077757292442138972524482080487968542466670420954056298813838154325967509");


        //先生成秘钥以后可删除
        //System.out.println("init finish");
    }

    /**
     * 作为QU端发送查询数据
     *
     * @param vertexes
     * @return 返回一个查询数据
     */
    public String QU_AGRQ_P_QDC(ArrayList<Vertex> vertexes) {
        Vertex q = null;
        Vertex qNext = null;
        BigInteger[] C_i = new BigInteger[6];
        String C_iStr = null;
        StringBuilder C = new StringBuilder();
        /*
         * 计算C_i
         */
        for (int i = 0; i < vertexes.size(); i++) {
            for (int c = 0; c < c_in.length; c++) {
                c_in[c] = new BigInteger(k[2], new Random(System.currentTimeMillis()));
            }
            q = vertexes.get(i);
            if (i == vertexes.size() - 1) {
                qNext = vertexes.get(0);
            } else {
                qNext = vertexes.get(i + 1);
            }
            BigInteger q_x = BigInteger.valueOf(q.getX());
            BigInteger q_y = BigInteger.valueOf(q.getY());
            BigInteger qNext_x = BigInteger.valueOf(qNext.getX());
            BigInteger qNext_y = BigInteger.valueOf(qNext.getY());
            C_i[0] = q_x.multiply(alpha).add(c_in[0]).multiply(s).mod(p);
            C_i[1] = q_y.multiply(alpha).add(c_in[1]).multiply(s).mod(p);
            C_i[2] = qNext_x.multiply(alpha).add(c_in[2]).multiply(s).mod(p);
            C_i[3] = qNext_y.multiply(alpha).add(c_in[3]).multiply(s).mod(p);
            C_i[4] = q_x.multiply(alpha).multiply(qNext_y).add(c_in[4]).multiply(s).mod(p);
            C_i[5] = qNext_x.multiply(alpha).multiply(q_y).add(c_in[5]).multiply(s).mod(p);
            StringBuilder TempC_iStr = new StringBuilder();//相当于一个C_i
            for (int a = 0; a < C_i.length; a++) {
                TempC_iStr.append(C_i[a].toString() + symbol1);
            }
            C_iStr = TempC_iStr.toString();
            C_iStr = C_iStr.substring(0, C_iStr.lastIndexOf(symbol1));//C_in 之间使用symbol1分隔
            C.append(C_iStr + symbol3);//C_i 之间使用symbol3分隔
        }
        /**
         * 使用SM3作为哈希函数
         */
        String CStr = C.toString();
        CStr = CStr.substring(0, CStr.lastIndexOf(symbol3));
        long nowTime = System.currentTimeMillis();
        String H_pre = this.alpha.toString() + symbol2 + this.p.toString() + symbol2 + CStr + symbol2 + QUID + symbol2 + nowTime;
        String H = null;
        H = SMUtils.encryptBySm3(H_pre);

        String H_QU = null;
        H_QU = SMUtils.encryptBySm4(H, this.key_QU);

        String send = this.alpha.toString() + symbol2 + this.p.toString() +
                symbol2 + CStr + symbol2 + QUID + symbol2 + nowTime + symbol2 + H_QU;//不同字符类型之间以symbol2隔开
        System.out.println("发送数据生成 ：" + send);
        return send;
    }

    /**
     * 作为UF端，收到查询数据后，发送查询所必须的处理结果
     *
     * @param message
     * @param UF_j
     * @return 若数据查询不符合格式，返回null
     * @throws Exception
     */
    public String UF_AGRQ_P_RDC(String message, Vertex UF_j) throws Exception {
        BigInteger x_j = BigInteger.valueOf(UF_j.getX());
        BigInteger y_j = BigInteger.valueOf(UF_j.getY());
        boolean flag = this.verifyMessageQDT(message);
        if (flag) {
            String[] messages = message.trim().split(symbol2);//以symbol2分开
            BigInteger alpha_QU = new BigInteger(messages[0]);
            BigInteger p_QU = new BigInteger(messages[1]);
            String C_QUStr = messages[2];
            String[] C_iStr = C_QUStr.trim().split(symbol3);
            String[] C_inStr = null;
            BigInteger[] D_in = new BigInteger[2];
            StringBuilder DSb = new StringBuilder();
            String D = null;
            String D_i = null;
            for (int i = 0; i < C_iStr.length; i++) {
                C_inStr = C_iStr[i].trim().split(symbol1);
                if (C_inStr.length != 6) {
                    System.out.println("UF_AGRQ_P_RDC_Error C_in的长度不为6");
                    return null;
                }
                BigInteger[] C_in = new BigInteger[6];
                for (int a = 0; a < 6; a++) {
                    C_in[a] = new BigInteger(C_inStr[a]);
                }
                r_i = new BigInteger(k[3], new Random(System.currentTimeMillis()));
                D_in[0] = x_j.multiply(C_in[3]).add(y_j.
                        multiply(C_in[0])).add(C_in[5]).multiply(alpha_QU).multiply(r_i).mod(p_QU);//question
                D_in[1] = x_j.multiply(C_in[1]).add(y_j.
                        multiply(C_in[2])).add(C_in[4]).multiply(alpha_QU).multiply(r_i).mod(p_QU);
                D_i = D_in[0] + symbol1 + D_in[1];//D_in 之间以symbol1分隔
                DSb.append(D_i + symbol3);//D_i 之间以symbol3分隔
            }
            String Dtemp = DSb.toString();
            D = Dtemp.substring(0, Dtemp.lastIndexOf(symbol3));
            /*
             * 以下是加密部分
             */
            long nowTime = System.currentTimeMillis();
            String H_pre = D + symbol2 + QUID + symbol2 + nowTime;
            String H = SMUtils.encryptBySm3(H_pre);
            String H_UF = SMUtils.encryptBySm4(H, key_QU);
            String sendMessage = D + symbol2 + QUID + symbol2 + nowTime + symbol2 + H_UF;//同字符类型之间以symbol2隔开
            System.out.println("UF_AGRQ_P_RDC发送数据生成成功：" + sendMessage);
            return sendMessage;
        }
        return null;
    }

    /**
     * 收到朋友端返回的数据后，计算判别该点是否在查询的范围内
     *
     * @param message
     * @return 先规定若在范围内，查询结果为true，数据错误或者不在范围内都会返回false
     * @throws Exception
     */
    public boolean QU_AGRQ_P_QRR(String message) throws Exception {
        if (this.verifyMessageRDT(message)) {
            String[] messages = message.trim().split(symbol2);
            // split m Ai
            String[] DStr = messages[0].trim().split(symbol3);
            BigInteger alpha_2 = alpha.pow(2);
            BigInteger[] E_in = new BigInteger[2];
            BigInteger[] E_inNext = new BigInteger[2];
            BigInteger E_i = null;
            BigInteger[] D_in = new BigInteger[2];
            String[] D_inStr = null;
            for (int i = 0; i < DStr.length; i++) {
                // split Aij1 and Aij2
                D_inStr = DStr[i].trim().split(symbol1);
                if (D_inStr.length != 2) {
                    System.out.println("QU_AGRQ_P_QRR_Error D_in的数组长度值不对");
                    return false;
                }
                for (int a = 0; a < 2; a++) {//计算E_in
                    D_in[a] = new BigInteger(D_inStr[a]);
                    E_inNext[a] = s_Inverse.multiply(D_in[a]).mod(p);
                    E_in[a] = E_inNext[a].subtract(E_inNext[a].mod(alpha_2)).divide(alpha_2);
                }
                E_i = E_in[1].subtract(E_in[0]);
                if (E_i.compareTo(BigInteger.ZERO) == -1) {
                    //System.out.println("该点不在范围内");
                    return false;
                }
            }
            //System.out.println("该点在范围内");
            return true;

        }
        return false;
    }

    /**
     * Query Date Transmission时验证数据是否有效
     *
     * @param message
     * @return
     * @throws Exception
     */
    public boolean verifyMessageQDT(String message) throws Exception {
        String[] messages = message.trim().split(symbol2);
        if (messages.length != 6) {
            System.out.println("QDT验证失败：不同字符串类型数量不是6");
            return false;
        }
        long flagLong = System.currentTimeMillis() - new Long(messages[4].trim());
//		if(flagLong<thresholdValue){
        if (true) {
            StringBuilder H_pre1 = new StringBuilder();
            String H2 = SMUtils.decryptBySm4(messages[5], key_QU);
            for (int i = 0; i < 5; i++) {
                H_pre1.append(messages[i] + symbol2);
            }
            String H_pre1Temp = H_pre1.toString();
            H_pre1Temp = H_pre1Temp.substring(0, H_pre1Temp.lastIndexOf(symbol2));
            String H1 = SMUtils.encryptBySm3(H_pre1Temp);
            if (H1.equals(H2)) {
                System.out.println("QDT H验证通过");
                return true;
            }
            System.out.println("QDT H验证不通过");
        } else {
            System.out.println("QDT验证失败：超时");
        }
        return false;
    }

    /**
     * Response Data Transmission时验证数据是否有效
     *
     * @param message
     * @return
     * @throws Exception
     */
    public boolean verifyMessageRDT(String message) throws Exception {
        String[] messages = message.split(symbol2);
        if (messages.length != 4) {
            System.out.println("RDT验证失败：不同字符串类型数量不是4");
            return false;
        }
        long tempTime = System.currentTimeMillis() - new Long(messages[2].trim());
//		if (tempTime < thresholdValue){
        if (true) {
            StringBuilder H_pre1 = new StringBuilder();
            String H2 = SMUtils.decryptBySm4(messages[3], key_QU);
            for (int i = 0; i < 3; i++) {
                H_pre1.append(messages[i] + symbol2);
            }
            String H_pre1Temp = H_pre1.toString();
            String H1 = SMUtils.encryptBySm3(H_pre1Temp.substring(0, H_pre1Temp.lastIndexOf(symbol2)));
            if (H1.equals(H2)) {
                //System.out.println("RDT H值验证通过");
                return true;
            }
            System.out.println("RDT H值验证不通过");
        } else {
            System.out.println("RDT验证失败：超时");
        }
        return false;
    }


}
