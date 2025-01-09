package com.lu.gademo.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.BasicData;
import com.lu.gademo.utils.AlgorithmInfo;
import com.lu.gademo.utils.AlgorithmsFactory;
import com.lu.gademo.utils.DSObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lu.gademo.service.impl.FileServiceImpl.getDsList;

public class BasicDataAnalysisEventListener extends AnalysisEventListener<BasicData> {
    private static final int BATCH_SIZE = 50000;
    private final List<BasicData> patch;
    private final ExcelWriter excelWriter;
    private final WriteSheet writeSheet;
    private final AlgorithmsFactory algorithmsFactory;
    private final Map<String, ExcelParam> config;
    private static final ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));;

    public BasicDataAnalysisEventListener(AlgorithmsFactory algorithmsFactory, Map<String, ExcelParam> config, String desenFilePath) {
        this.patch = new ArrayList<>(BATCH_SIZE);
        this.excelWriter = EasyExcel.write(desenFilePath, BasicData.class).build();
        this.writeSheet = EasyExcel.writerSheet("Sheet1").build();
        this.algorithmsFactory = algorithmsFactory;
        this.config = config;
    }

    @Override
    public void invoke(BasicData data, AnalysisContext context) {
        this.patch.add(data);

        if (this.patch.size() >= BATCH_SIZE) {
            try {
                dealWithPatch();
            } catch (IllegalAccessException e) {
                this.excelWriter.close();
                throw new RuntimeException(e);
            }
            this.patch.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        try {
            dealWithPatch();
            sdf.remove();
            this.excelWriter.close();
        } catch (IllegalAccessException e) {
            sdf.remove();
            this.excelWriter.close();
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle the data in the current patch.
     * The data will be stored in a map where the key is the index of the field and the value is a list of objects
     * The type of the object in the list is the same as the type of the field
     * If the type of the field is Date, the object in the list will be a Date
     * If the type of the field is String, the object in the list will be a String
     * @throws IllegalAccessException if the field is not accessible
     */
    public void dealWithPatch() throws IllegalAccessException {
        if (patch.isEmpty()) {
            excelWriter.close();
            return;
        }
        Map<String, List<Object>> map = new HashMap<>();
        Field[] fields = BasicData.class.getDeclaredFields();

        for (Field item : fields) {
//            System.out.println(item.getName());
            map.put(item.getName(), new ArrayList<>());
        }
        // 清洗数据
        for (BasicData obj : this.patch) {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == Date.class) {
                    Date value = (Date) field.get(obj);
                    map.get(field.getName()).add(sdf.get().format(value));
                } else {
                    Object value = field.get(obj);
                    map.get(field.getName()).add(value);
                }
            }
        }

        Map<String, List<Object>> maskedData = desen(map, this.config);
        List<BasicData> desenResult = getDesenResult(maskedData, fields);
        System.out.println(desenResult.get(0).getIdcardNum());
        this.excelWriter.write(desenResult, this.writeSheet);
        maskedData.clear();
        desenResult.clear();
        map.clear();
        maskedData = null;
        desenResult = null;
        map = null;
        System.gc();
        System.out.println(patch.size());
    }

    private Map<String, List<Object>> desen(Map<String, List<Object>> map, Map<String, ExcelParam> config) {

        Map<String, List<Object>> desenResult = new HashMap<>();
        for (String key : map.keySet()) {
            System.out.println("key: " + key);
            ExcelParam param = config.get(key);
            Integer algoNum = param.getK();
            Integer level = param.getTmParam();
            List<Object> rawList = map.get(key);
            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromId(algoNum);
            DSObject rawData = new DSObject(rawList);
            List<Object> dsList = getDsList(algorithmInfo, rawData, param);
            desenResult.put(key, dsList);
        }

        return desenResult;
    }

    private List<BasicData> getDesenResult(Map<String, List<Object>> desenData, Field[] fields) throws IllegalAccessException {
        List<BasicData> result = new ArrayList<>();
        System.out.println("idcardNum size: " + desenData.get("idcardNum").size());
        for (int i = 0; i < desenData.get("idcardNum").size(); i++) {
            BasicData basicData = new BasicData();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == Date.class) {
                    Date value = (Date) desenData.get(field.getName()).get(i);
                    field.set(basicData, value);

                } else if (field.getType() == double.class) {
                    double value = (double) desenData.get(field.getName()).get(i);
                    field.set(basicData, value);
                } else {
                    String value = (String) desenData.get(field.getName()).get(i);
                    field.set(basicData, value);
                }
            }
            result.add(basicData);
        }
        return result;
    }

    /**
     * 用于测试的函数，非真实脱敏
     * @param obj
     * @param fields
     * @return
     * @throws IllegalAccessException
     */
    private BasicData desenBasicData(BasicData obj, Field[] fields) throws IllegalAccessException {
        BasicData BasicData = new BasicData();
        for (int k = 0; k < fields.length; k++) {
            Field field = fields[k];
            field.setAccessible(true);

            if (field.getType() == Date.class) {
                Date value = (Date) field.get(obj);
                value.setTime(value.getTime() + 86400000);
                field.set(BasicData, value);
            } else {
                String value = (String) field.get(obj);
                field.set(BasicData, value.substring(0, 1));
            }
        }
        return BasicData;
    }
}
