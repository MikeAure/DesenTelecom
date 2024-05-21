package com.lu.gademo.trace.client.util;

//import android.content.Context;
//import android.widget.Toast;

import com.lu.gademo.trace.model.QuadTree;

import java.io.*;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Queue;

public class MapUtils {

    /**
     * 排序后的地图数据
     */
    public static String[] mapData;
    /**
     * 公共参数α与p与私钥
     */
    public static BigInteger alpha;
    public static BigInteger p;
    public static String p_key;
    public int index = 0;

//     private final Context context;

    public MapUtils() {
        initMapdata();
    }

    /**
     * 初始化即将文本中加密的地图数据进行排序
     */
    public void initMapdata() {
        sortMapData(readFromAssets("MapData.txt"));
        //setAlphaAndP();
    }

    /**
     * 从assets文本中读取字符串并返回一个串
     *
     * @param fileName
     * @return
     */
    public String readFromAssets(String fileName) {
        String str = "";
        try {
//            InputStream in = Files.newInputStream(Paths.get(fileName));
//            BufferedInputStream bis = new BufferedInputStream(in);
//            BufferedReader br = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8));
            InputStream mapDataStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (mapDataStream == null) {
                throw new FileNotFoundException(fileName + " not found");
            }
//                BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(mapFile.toPath()));
//                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8));
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(mapDataStream));
            str = bufferReader.readLine();
            //Log.e(fileName,str.length()+":"+str);
        } catch (IOException e) {

            // Toast.makeText(this.context, "读取地图数据异常！", Toast.LENGTH_SHORT).show();
            System.out.println("读取地图数据异常");
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将文本中用";"隔开的地图数据分别放到数组中，并在排序后返回
     *
     * @param mapData
     */
    public void sortMapData(String mapData) {
        MapUtils.mapData = mapData.split(";");
//        Log.e("length:",mapDatas.length+"");
        QuadTree qTree = new QuadTree();
        qTree.setDepth(1);
        insertMapData(qTree);
        index = 0;
        printByLevelOrder(qTree);
    }

    /**
     * 从头结点的第一个孩子节点开始按深度优先放置数据
     *
     * @param qTree
     */
    public void insertMapData(QuadTree qTree) {
        while (qTree.getDepth() < 4 && !qTree.isAllChildrenSet()) {
            if (qTree.firstChild == null) {
                qTree.firstChild = new QuadTree();
                qTree.firstChild.setMapData(mapData[index++]);
                qTree.firstChild.setDepth(qTree.getDepth() + 1);
                insertMapData(qTree.firstChild);
            }
            if (qTree.secondChild == null) {
                qTree.secondChild = new QuadTree();
                qTree.secondChild.setMapData(mapData[index++]);
                qTree.secondChild.setDepth(qTree.getDepth() + 1);
                insertMapData(qTree.secondChild);
            }
            if (qTree.thirdChild == null) {
                qTree.thirdChild = new QuadTree();
                qTree.thirdChild.setMapData(mapData[index++]);
                qTree.thirdChild.setDepth(qTree.getDepth() + 1);
                insertMapData(qTree.thirdChild);
            }
            if (qTree.fourthChild == null) {
                qTree.fourthChild = new QuadTree();
                qTree.fourthChild.setMapData(mapData[index++]);
                qTree.fourthChild.setDepth(qTree.getDepth() + 1);
                insertMapData(qTree.fourthChild);
            }
        }
    }

    /**
     * 不含头结点的按层次遍历
     *
     * @param quadTree
     */
    public void printByLevelOrder(QuadTree quadTree) {
        Queue<QuadTree> queue = new LinkedList<QuadTree>();

        queue.add(quadTree.firstChild);
        queue.add(quadTree.secondChild);
        queue.add(quadTree.thirdChild);
        queue.add(quadTree.fourthChild);

        while (!queue.isEmpty()) {
            QuadTree tempNode = queue.poll();
            mapData[index++] = tempNode.getMapData();
            if (tempNode.firstChild != null) queue.add(tempNode.firstChild);
            if (tempNode.secondChild != null) queue.add(tempNode.secondChild);
            if (tempNode.thirdChild != null) queue.add(tempNode.thirdChild);
            if (tempNode.fourthChild != null) queue.add(tempNode.fourthChild);
        }
    }

    public void setAlphaAndP() {
        String strs = readFromAssets("Code.txt");
        String[] paras = strs.split(";");
        alpha = new BigInteger(paras[1].split(":")[1]);
        p = new BigInteger(paras[2].split(":")[1]);
        p_key = paras[4];
    }

}
