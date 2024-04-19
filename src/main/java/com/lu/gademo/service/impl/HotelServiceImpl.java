package com.lu.gademo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dao.DpHotelDao;
import com.lu.gademo.dao.HotelDao;
import com.lu.gademo.dao.support.IBaseDao;
import com.lu.gademo.entity.DpHotel;
import com.lu.gademo.entity.Hotel;
import com.lu.gademo.entity.templateParam.HotelParam;
import com.lu.gademo.log.SendData;
import com.lu.gademo.service.DpHotelService;
import com.lu.gademo.service.HotelParamService;
import com.lu.gademo.service.HotelService;
import com.lu.gademo.service.support.impl.BaseServiceImpl;
import com.lu.gademo.utils.DpUtil;
import com.lu.gademo.utils.StaticUtil;
import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.DpUtilImpl;
import com.lu.gademo.utils.impl.StaticUtilImpl;
import com.lu.gademo.utils.impl.UtilImpl;
import com.lu.gademo.vo.NumsView;
import com.lu.gademo.vo.SingleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class HotelServiceImpl  extends BaseServiceImpl<Hotel,Integer> implements HotelService {
    @Autowired
    private HotelDao hotelDao;
    @Autowired
    private DpHotelService dpHotelService;
    @Autowired
    private HotelParamService hotelParamService;
    @Autowired
    private DpHotelDao dpHotelDao;

    @Override
    public IBaseDao<Hotel, Integer> getBaseDao() {
        return this.hotelDao;
    }

    // 发送类
    @Autowired
    private SendData sendData;
    // 系统id
    private int systemID = 0x31000000;

    /**
     * 工具类
     */
    Util util = new UtilImpl();
    // 脱敏前信息类型标识
    StringBuffer desenInfoPreIden = new StringBuffer();
    // 脱敏后信息类型标识
    StringBuffer desenInfoAfterIden = new StringBuffer();
    // 脱敏意图
    StringBuffer desenIntention = new StringBuffer();
    //List<String> desenIntention = new ArrayList<>();
    // 脱敏要求
    StringBuffer desenRequirements = new StringBuffer();
    //List<String> desenRequirements = new ArrayList<>();
    // 脱敏控制集合
    String desenControlSet = "densencontrolset";
    //List<String> desenControlSet = new ArrayList<>();
    // 脱敏参数
    StringBuffer desenAlgParam = new StringBuffer();
    //List<String> desenAlgParam = new ArrayList<>();
    // 脱敏级别
    StringBuffer desenLevel = new StringBuffer();
    //List<Integer> desenLevel = new ArrayList<>();
    // 脱敏算法
    StringBuffer desenAlg = new StringBuffer();
    //List<Integer> desenAlg = new ArrayList<>();
    // 脱敏执行主体
    String desenPerformer = "脱敏工具集";
    // 脱敏完成情况
    int desenCom = 0;
    // 脱敏对象大小
    int objectSize;

    // 脱敏开始时间
    String startTime = util.getTime();
    ObjectMapper mapper = new ObjectMapper();


    @Override
    public void tmOperate(List<HotelParam> params) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //向数据库中存入HotelParam数据
        hotelParamService.save(params);
        // 删除历史脱敏数据
        if (dpHotelDao != null) {
            dpHotelDao.deleteAll();
        }

        // 工具类
        DpUtil dpUtil = new DpUtilImpl();
        // 每次调用生成随机数，拼接到文件名，测试用
        Random ran = new Random();
        int num = ran.nextInt(10000);
        long fetch_data_begin = System.currentTimeMillis();
        List<Hotel> hotels = hotelDao.findAll();
        long fetch_data_end = System.currentTimeMillis();
        writeFile("D:\\Data\\HOTEL" + num + ".txt", "fetch_data_time:" + (fetch_data_end - fetch_data_begin) + "ms");
        // 数组初始化
        List<DpHotel> dphotels = new ArrayList<>();
        for (int i = 0; i < hotels.size(); i++) {
            DpHotel dpHotel = new DpHotel();
            dphotels.add(dpHotel);
        }
        //反射操作
        Class<?> Hotel = Class.forName("com.lu.gademo.entity.Hotel");
        Class<?> DpHotel = Class.forName("com.lu.gademo.entity.DpHotel");
        Method method;

        //test_process_time_yl
        String tmp;
        // 遍历处理
        for (HotelParam param : params) {
            tmp = "FiledName:";
            //字段名，首字母大写
            String zdname = param.getFiledName();
            tmp += zdname + ';';
            String colName = param.getColumnName();
            tmp += "columnName:" + colName + ';';
            method = Hotel.getMethod("get" + zdname);
            List<Object> objs = new ArrayList<>();
            for (Hotel hotel : hotels) {
                objs.add(method.invoke(hotel));
            }
            //日期处理
            if (param.getDataType() == 4) {
                tmp += "DataType:日期类型;process_time:";
                long begin = System.currentTimeMillis();
                System.out.println("日期：" + param.getFiledName() + " " + param.getColumnName());
                List<Date> datas = new ArrayList<>();

                try {
                    if(param.getK() == 0) {
                        // 加噪
                        datas = dpUtil.dpDate(objs, param.getTmParam());
                    }
                    else {
                        // k-匿名
                        datas = dpUtil.date_group_replace(objs, param.getTmParam());
                    }
                } catch (ParseException e) {
                    e.getStackTrace();
                }

                int i = 0;
                for (DpHotel dh : dphotels) {
                    method = DpHotel.getMethod("set" + zdname, Date.class);
                    method.invoke(dh, datas.get(i));
                    i++;
                }
                long end = System.currentTimeMillis();
                tmp += (end - begin);
                tmp += "ms";
            }
            //数值处理
            if (param.getDataType() == 0) {
                tmp += "DataType:数值类型;process_time:";
                long begin = System.currentTimeMillis();

                List<Double> datas;
                if (param.getK() == 0) {
                    datas = dpUtil.laplaceToValue(objs, param.getTmParam());
                }
                else {
                    datas = dpUtil.k_NumberCode(objs, param.getTmParam());
                }
                int i = 0;
                for (DpHotel dh : dphotels) {
                    method = DpHotel.getMethod("set" + zdname, Integer.class);
                    method.invoke(dh, datas.get(i));
                    i++;
                }

                long end = System.currentTimeMillis();
                tmp += (end - begin);
                tmp += "ms";
            }
            //单编码处理
            if (param.getDataType() == 1) {
                tmp += "DataType:单编码;process_time:";
                long begin = System.currentTimeMillis();

                System.out.println("单编码：" + param.getFiledName() + " " + param.getColumnName());

                List<String> datas = dpUtil.dpCode(objs, param.getTmParam());
                int i = 0;
                for (DpHotel dh : dphotels) {
                    method = DpHotel.getMethod("set" + zdname, String.class);
                    method.invoke(dh, datas.get(i));
                    i++;
                }

                long end = System.currentTimeMillis();
                tmp += (end - begin);
                tmp += "ms";
            }
            //文本处理
            if (param.getDataType() == 3) {
                tmp += "DataType:文本类型;process_time:";
                long begin = System.currentTimeMillis();

                System.out.println("文本：" + param.getFiledName() + " " + param.getColumnName());
                 if (param.getColumnName().contains("地址") || param.getColumnName().contains("住址")) {
                    System.out.println(objs.size());
                    List<String> datas = dpUtil.addressHide(objs, param.getTmParam());
                    System.out.println(datas.size());
                    int i = 0;
                    for (DpHotel dh : dphotels) {
                        method = DpHotel.getMethod("set" + zdname, String.class);
                        method.invoke(dh, datas.get(i));
                        i++;
                    }
                }
                else if (param.getColumnName().contains("名")) {
                    String name = param.getColumnName();
                     List<String> datas;
                    //System.out.println("传输过来的数据" + datas.get(0));
                     if (name.contains("公安") || name.contains("派出所") || name.contains("街道") || name.contains("酒店") || name.contains("宾馆") || name.contains("旅店")){
                         datas = dpUtil.desenAddressName(objs, param.getTmParam(), name);
                     }else {
                         datas = dpUtil.nameHide(objs, param.getTmParam());
                     }
                     int i = 0;
                     for (DpHotel dh : dphotels) {
                        method = DpHotel.getMethod("set" + zdname, String.class);
                        String a = datas.get(i);
                        method.invoke(dh, a);
                        i++;
                    }
                }  else {
                    List<String> datas = dpUtil.numberHide(objs, param.getTmParam());
                    int i = 0;
                    for (DpHotel dh : dphotels) {
                        method = DpHotel.getMethod("set" + zdname, String.class);
                        method.invoke(dh, datas.get(i));
                        i++;
                    }
                }
                long end = System.currentTimeMillis();
                tmp += (end - begin);
                tmp += "ms";
            }
            writeFile("D:\\Data\\HOTEL" + num + ".txt", tmp);
        }

        //重新遍历参数数组,此时不需要按列处理，直接横向处理
        for (HotelParam param:params){
            if(param.getDataType()!=2){
                continue;
            }
            //字段名，首字母大写
            String zdname = param.getFiledName();
            method = Hotel.getMethod("get" + zdname);

            //身份证的处理
            if(param.getColumnName().contains("身份号码"))
            {
                System.out.println("身份证号处理："+param.getColumnName()+param.getFiledName());
                int index = 0;
                for (DpHotel dh : dphotels) {
                    // 户籍代码为空
                    if (dh.getHjxzqhdm() == null || dh.getHjxzqhdm().length() == 0){
                        // 居住地代码为空
                        if (dh.getJzdxzqhdm() == null || dh.getJzdxzqhdm().length() == 0){
                            dh.setGmffid(dpUtil.IDCode((String) method.invoke(hotels.get(index)), dh.getBirthday(), dh.getXb()));
                        }else {
                            dh.setGmffid(dpUtil.IDCode((String) method.invoke(hotels.get(index)), dh.getJzdxzqhdm(), dh.getBirthday(), dh.getXb()));
                        }
                    }else {
                        dh.setGmffid(dpUtil.IDCode((String) method.invoke(hotels.get(index)), dh.getHjxzqhdm(), dh.getBirthday(), dh.getXb()));
                    }
                    index += 1;
                }
            }
        }
        // 信息主键编号
        for (HotelParam param:params) {
            if (param.getDataType() != 2) {
                continue;
            }

            //身份证的处理
            if (param.getColumnName().contains("信息主键编号")) {
                System.out.println("信息主键编号：" + param.getColumnName() + param.getFiledName());
                //dh.getRzsj().toString()
                for (DpHotel dh : dphotels) {
                       dh.setXxbh(dpUtil.infoID(dh.getGmffid(), dh.getXm(), dh.getRzsj(), dh.getLdmc()));
                }
            }
        }
        long save_time_begin = System.currentTimeMillis();
        for (int i = 0; i < dphotels.size(); i++) {
            dphotels.get(i).setId(i+1);
            //dpCheguanDao.save(dpcheguans.get(i));
            System.out.println(dphotels.get(i).getId());
        }
        //向dpHotel数据库中存数据
        dpHotelService.save(dphotels);
        long save_time_end = System.currentTimeMillis();
        writeFile("D:\\Data\\HOTEL" + num + ".txt", "save_data_time:" + (save_time_end - save_time_begin) + "ms");
    }


    @Override
    public List<NumsView> statisticNums(String[] colNames, String showType) throws Exception {
        List<Hotel> hujis=hotelDao.findAll();
        List<DpHotel> dphujis=dpHotelService.findAll();
        List<NumsView> res= new ArrayList<>();
        for (int i = 0; i <colNames.length ; i++) {
            res.add(new NumsView());
        }
        //存储数据
        List<ArrayList<Object>> preList= new ArrayList<>();
        List<ArrayList<Object>> postList= new ArrayList<>();
        Class<?> Huji=Class.forName("com.lu.gademo.entity.Hotel");
        Class<?> DpHuji=Class.forName("com.lu.gademo.entity.DpHotel");
        Method method;
        for (int i=0;i<colNames.length;i++ ) {
            preList.add(new ArrayList<>());
            postList.add(new ArrayList<>());
            for (Hotel hj: hujis  ) {
                method=Huji.getMethod("get"+colNames[i]);
                String lb= method.getGenericReturnType().getTypeName();
                if(lb.equals("java.lang.Integer")){
                    Integer num= (Integer) method.invoke(hj);
                    preList.get(i).add(num);}
                else{
                    Double num= (Double) method.invoke(hj);
                    preList.get(i).add(num);}
            }
            for (DpHotel dhj: dphujis  ) {
                method=DpHuji.getMethod("get"+colNames[i]);
                Double num= (Double) method.invoke(dhj);
                postList.get(i).add(num);
            }

        }
        //开始处理数据,"0": "平均数",
        //            "1": "方差",
        //            "2": "中位数",
        StaticUtil su= new StaticUtilImpl();
        if("0".equals(showType)){
            for (int i = 0; i <colNames.length ; i++) {
                res.get(i).setColNname(colNames[i]);
                Double pre=su.average(preList.get(i));
                Double post=su.average(postList.get(i));
                res.get(i).setPre(pre);
                res.get(i).setPost(post);
            }
        }

        if("1".equals(showType)){
            for (int i = 0; i <colNames.length ; i++) {
                res.get(i).setColNname(colNames[i]);
                Double pre=su.variance(preList.get(i));
                Double post=su.variance(postList.get(i));
                res.get(i).setPre(pre);
                res.get(i).setPost(post);
            }
        }
        if("2".equals(showType)){
            for (int i = 0; i <colNames.length ; i++) {
                res.get(i).setColNname(colNames[i]);
                Double pre=su.median(preList.get(i));
                Double post=su.median(postList.get(i));
                res.get(i).setPre(pre);
                res.get(i).setPost(post);
            }
        }
        return res;
    }

    @Override
    public List<SingleView> statisticSingles(String[] colNames) throws Exception {
        List<SingleView> resList = new ArrayList<>();
        for (int i = 0; i <colNames.length ; i++) {
            resList.add(new SingleView());
            resList.get(i).setTongMap(new HashMap<>());
        }
        List<Hotel> hujis=hotelDao.findAll();
        List<DpHotel> dphujis=dpHotelService.findAll();
        Class<?> Huji=Class.forName("com.lu.gademo.entity.Hotel");
        Class<?> DpHuji=Class.forName("com.lu.gademo.entity.DpHotel");
        Method method;
        for (int i=0;i<colNames.length;i++ ) {
            SingleView sv = resList.get(i);

            for (Hotel hj: hujis  ) {
                method=Huji.getMethod("get"+colNames[i]);
                String num= (String) method.invoke(hj);
                sv.setColName(colNames[i]);
                if(num==null) {continue;}

                if(!sv.getTongMap().containsKey(num))
                    sv.getTongMap().put(num,new SingleView.Node());
                sv.getTongMap().get(num).PreAdd();
            }
            for (DpHotel dhj: dphujis  ) {
                method=DpHuji.getMethod("get"+colNames[i]);
                String num= (String) method.invoke(dhj);
                sv.setColName(colNames[i]);
                if(num==null) {continue;}
                if(!sv.getTongMap().containsKey(num))
                    sv.getTongMap().put(num,new SingleView.Node());
                sv.getTongMap().get(num).postAdd();
            }

        }
        return resList;
    }

    /**
     *
     * @param ldmc  旅店名称
     * @param lddz  旅店地址
     * @param pageRequest 分页请求
     * @return
     */
    @Override
    public Page<Hotel> findAllLike1(String ldmc, String lddz,PageRequest pageRequest) {
        if (ldmc==null) {lddz="";}
        if(lddz==null){lddz="";}
        return hotelDao.findByLdmcContainsAndLddzmcContains(ldmc,lddz,pageRequest);
    }
    @Override
    public Page<Hotel> findByID(String id, PageRequest pageRequest) {
        return hotelDao.findById(Integer.valueOf(id), pageRequest);
    }

    @Override
    public Page<Hotel> findByName(String name, PageRequest pageRequest) {
        return hotelDao.findByLdmcContains(name, pageRequest);
    }

    //  写入文件，测试用——yl
    public static void writeFile(String path, String content) {
        File writefile;

        try {
            // 通过这个对象来判断是否向文本文件中追加内容

// boolean addStr = append;

            writefile = new File(path);

// 如果文本文件不存在则创建它

            if (!writefile.exists()) {
                writefile.createNewFile();

                writefile = new File(path); // 重新实例化

            }

            FileOutputStream fw = new FileOutputStream(writefile, true);

            Writer out = new OutputStreamWriter(fw, "utf-8");

            out.write(content);

            String newline = System.getProperty("line.separator");

//写入换行

            out.write(newline);

            out.close();

            fw.flush();

            fw.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }

    }

}
