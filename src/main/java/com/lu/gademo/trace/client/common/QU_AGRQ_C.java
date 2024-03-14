package com.lu.gademo.trace.client.common;

//import android.util.Log;

import com.lu.gademo.trace.client.util.SMUtils;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Random;


public class QU_AGRQ_C {

    private final int[] k = new int[4];//参数K
    private BigInteger s, sInverse, p, alpha;//参数s，s的逆，p,α
    private final BigInteger[] c_in = new BigInteger[4];//数组c
    private final BigInteger[] r_i = new BigInteger[2];//数组r
    private String key_QU;//会话密钥
    private String QUID;//用户表示
    private static final int thresholdValue = 5 * 60 * 1000;//时延门限值
    private static final String symbol1 = "%%";//分隔符1
    private static final String symbol2 = "&&";//分隔符2
    private static final String symbol3 = "@@";//分隔符3
    private BigInteger A = null;//半径的平方和
    private BigInteger r = null;//半径

    public QU_AGRQ_C() {
        //init();
        this("BBF4244CECAAE7FC3F05329D2F279A7B", "1");
    }

    public QU_AGRQ_C(String key_QU, String QUID) {
        this.key_QU = key_QU;
        this.QUID = QUID;
        init();
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
		p= BigInteger.probablePrime(k[0], new Random(System.currentTimeMillis()));//k1 p
		alpha= BigInteger.probablePrime(k[1], new Random(System.currentTimeMillis()));//k2 alpha
		s=new BigInteger(70, new Random(System.currentTimeMillis()));//s取值问题
		s_Inverse=s.modInverse(p);
		*/

        s = new BigInteger("504994818564917003702");
        alpha = new BigInteger("1000286945907146489561925541631911126123120564769");
        p = new BigInteger("7463198780056285544145239772801612205520503806165531418261541054588774004271514418661269602558830075009772464570653898278021471788183550198819818819909761");
        sInverse = new BigInteger("4598453610455512631565011695692117160861716460817154970305475701194235436028198518077757292442138972524482080487968542466670420954056298813838154325967509");


    }

    /**
     * 作为AGRQ_C的QU端发送数据
     *
     * @param circleArea
     * @return 发送的字符串
     */
    public String QU_AGRQ_C_QDC(CircleArea circleArea) {
        BigInteger q_x = BigInteger.valueOf(circleArea.getCenter_x());
        BigInteger q_y = BigInteger.valueOf(circleArea.getCenter_y());
        A = q_x.pow(2).add(q_y.pow(2));//初始化A值
        r = BigInteger.valueOf(circleArea.getRadius());
        BigInteger[] C_i = new BigInteger[4];
        /*
         * 初始化数组c_in
         */
        for (int i = 0; i < c_in.length; i++) {
            c_in[i] = new BigInteger(k[2], new Random(System.currentTimeMillis()));
        }
        C_i[0] = q_x.multiply(alpha).add(c_in[0]).multiply(s).mod(p);
        C_i[1] = q_y.multiply(alpha).add(c_in[1]).multiply(s).mod(p);
        C_i[2] = c_in[2].multiply(s).mod(p);
        C_i[3] = c_in[3].multiply(s).mod(p);
        String CStr = C_i[0] + symbol3 + C_i[1] + symbol3 + C_i[2] + symbol3 + C_i[3];//生成C,C_i之间以symbol3分隔
        BigInteger A = q_x.pow(2).add(q_y.pow(2));

        /*
         * 生成签名内容 不同字符类型以symbol2隔开
         */
        long nowTime = System.currentTimeMillis();
        String H_pre = alpha.toString() + symbol2 + p.toString() + symbol2 + CStr + symbol2 + QUID + symbol2 + nowTime;
        /*
         * 先使用SM3和SM4加密
         */
        String H = SMUtils.encryptBySm3(H_pre);
        String H_QU = SMUtils.encryptBySm4(H, key_QU);
        /*
         * 生成所需发送的数据 不同字符类型以symbol2隔开
         */
        String sendMessage = H_pre + symbol2 + H_QU;
        System.out.println("发送数据生成 ：" + sendMessage);
        return sendMessage;
    }

    /**
     * 作为UF端，收到查询数据后，发送查询所必须的处理结果
     *
     * @param message
     * @param UF_j
     * @return 若数据查询不符合格式，返回null
     * @throws Exception
     */
    public String UF_AGRQ_C_RDC(String message, Vertex UF_j) throws Exception {
        BigInteger x_j = BigInteger.valueOf(UF_j.getX());
        BigInteger y_j = BigInteger.valueOf(UF_j.getY());
        boolean flag = this.verifyMessageQDT(message);
        if (flag) {
            String[] messages = message.trim().split(symbol2);//以symbol2分开
            BigInteger alpha_QU = new BigInteger(messages[0]);
            BigInteger p_QU = new BigInteger(messages[1]);
            String C_QUStr = messages[2];
            String[] C_iStr = C_QUStr.trim().split(symbol3);
            if (C_iStr.length != 4) {
                System.out.println("UF_AGRQ_C_RDC_Error C_i的长度不为4");
                return null;
            }

            BigInteger[] C_i = new BigInteger[4];
            for (int i = 0; i < C_i.length; i++) {
                C_i[i] = new BigInteger(C_iStr[i].trim());
            }
            /*
             * 计算B&D
             */
            BigInteger[] D_i = new BigInteger[4];
            for (int i = 0; i < r_i.length; i++) {
                r_i[i] = new BigInteger(k[3], new Random(System.currentTimeMillis()));
            }
            D_i[0] = x_j.multiply(alpha_QU).multiply(C_i[0]).mod(p_QU);
            D_i[1] = y_j.multiply(alpha_QU).multiply(C_i[1]).mod(p_QU);
            D_i[2] = r_i[0].multiply(C_i[2]).mod(p_QU);
            D_i[3] = r_i[1].multiply(C_i[3]).mod(p_QU);
            BigInteger D = D_i[0].add(D_i[1]).add(D_i[2]).add(D_i[3]);
            BigInteger B = x_j.pow(2).add(y_j.pow(2));
            /*
             * 签名认证加密
             */
            long nowTime = System.currentTimeMillis();
            String H_pre = B + symbol2 + D + symbol2 + QUID + symbol2 + nowTime;//同字符类型之间以symbol2隔开
            String H = SMUtils.encryptBySm3(H_pre);
            String H_UF = SMUtils.encryptBySm4(H, key_QU);
            String sendMessage = H_pre + symbol2 + H_UF;//同字符类型之间以symbol2隔开
//            System.out.println("UF_AGRQ_C_RDC发送数据生成成功：" + sendMessage);
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
    public boolean QU_AGRQ_P_QRR(String message, CircleArea circleArea) throws Exception {
        BigInteger q_x = BigInteger.valueOf(circleArea.getCenter_x());
        BigInteger q_y = BigInteger.valueOf(circleArea.getCenter_y());
        A = q_x.pow(2).add(q_y.pow(2));//初始化A值
        r = BigInteger.valueOf(circleArea.getRadius());
        if (this.verifyMessageRDT(message)) {
            String[] messages = message.trim().split(symbol2);
            /*
             * 解析出B&D
             */
            BigInteger B = new BigInteger(messages[0]);
            BigInteger D = new BigInteger(messages[1]);
            BigInteger alpha_2 = alpha.pow(2);
            /*
             * 处理数据得出结论
             */
            BigInteger E_pre = sInverse.multiply(D).mod(p);
            BigInteger E = E_pre.subtract(E_pre.mod(alpha_2)).divide(alpha_2);
            BigInteger R = A.add(B).subtract(E.add(E)).subtract(r.pow(2));
            return R.compareTo(BigInteger.ZERO) == -1;
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
        if (messages.length != 5) {
            System.out.println("RDT验证失败：不同字符串类型数量不是5");
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long carriedTimeMillis = new Long(messages[3].trim());
//		long tempTime= System.currentTimeMillis()-new Long(messages[3].trim());
        long tempTime = currentTimeMillis - carriedTimeMillis;
        if (tempTime < thresholdValue) {
            StringBuilder H_pre1 = new StringBuilder();
            String H2 = SMUtils.decryptBySm4(messages[4], key_QU);
            for (int i = 0; i < 4; i++) {
                H_pre1.append(messages[i] + symbol2);
            }
            String H_pre1Temp = H_pre1.toString();
            String H1 = SMUtils.encryptBySm3(H_pre1Temp.substring(0, H_pre1Temp.lastIndexOf(symbol2)));
            if (H1.equals(H2)) {
                System.out.println("RDT H值验证通过");
                return true;
            }
            System.out.println("RDT H值验证不通过");
        } else {
            System.out.println("RDT验证失败：超时");
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
//			Log.e("Current:",format.format(new Date(currentTimeMillis)));
//			Log.e("Carried:",format.format(new Date(carriedTimeMillis)));
//			Log.e("Difference:",tempTime+"");
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
        long currentTimeMillis = System.currentTimeMillis();
        long carriedTimeMillis = new Long(messages[4].trim());
        long flagLong = currentTimeMillis - carriedTimeMillis;
        if (flagLong < thresholdValue) {
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
//			Log.e("Current:",format.format(new Date(currentTimeMillis)));
//			Log.e("Carried:",format.format(new Date(carriedTimeMillis)));
//			Log.e("Difference:",flagLong+"");
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        int i = 0;
        for (i = 0; i < 100; i++) {
            QU_AGRQ_C test = new QU_AGRQ_C(SMUtils.sm4generateKey(), "1");
            String a = test.QU_AGRQ_C_QDC(new CircleArea(3000, 4000, 4000));
            String b = test.UF_AGRQ_C_RDC(a, new Vertex(3123, 3123));
            if (b == null) {
                System.exit(0);
            }
            if (!test.QU_AGRQ_P_QRR(b, new CircleArea(3000, 4000, 4000))) {
                break;
            }

        }
        System.out.println(i);
    }
}
