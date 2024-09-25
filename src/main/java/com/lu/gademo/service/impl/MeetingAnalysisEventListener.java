package com.lu.gademo.service.impl;

import cn.hutool.core.annotation.AliasFor;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.Meeting;
import com.lu.gademo.utils.AlgorithmInfo;
import com.lu.gademo.utils.AlgorithmsFactory;
import com.lu.gademo.utils.DSObject;
import freemarker.template.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lu.gademo.service.impl.FileServiceImpl.getDsList;


public class MeetingAnalysisEventListener extends AnalysisEventListener<Meeting>  {

    private static final int BATCH_SIZE = 10000;
    private final List<Meeting> patch;
    private final ExcelWriter excelWriter;
    private final WriteSheet writeSheet;
    private final AlgorithmsFactory algorithmsFactory;
    private final Map<String, ExcelParam> config;
    private final ThreadLocal<SimpleDateFormat> sdf;

    public MeetingAnalysisEventListener(AlgorithmsFactory algorithmsFactory, Map<String, ExcelParam> config, String desenFilePath) {
        this.patch = new ArrayList<>(BATCH_SIZE);
        this.excelWriter = EasyExcel.write(desenFilePath, Meeting.class).build();
        this.writeSheet = EasyExcel.writerSheet("Sheet1").build();
        this.algorithmsFactory = algorithmsFactory;
        this.config = config;
        this.sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public void invoke(Meeting data, AnalysisContext context) {
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
            this.excelWriter.close();
        } catch (IllegalAccessException e) {
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
        Field[] fields = Meeting.class.getDeclaredFields();

        for (Field item : fields) {
//            System.out.println(item.getName());
            map.put(item.getName(), new ArrayList<>());
        }
        // 清洗数据
        for (Meeting obj : this.patch) {
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
        List<Meeting> desenResult = getDesenResult(maskedData, fields);
        System.out.println(desenResult.get(0).getHysj());
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

    private List<Meeting> getDesenResult(Map<String, List<Object>> desenData, Field[] fields) throws IllegalAccessException {
        List<Meeting> result = new ArrayList<>();
        System.out.println("yhm size: " + desenData.get("yhm").size());
        for (int i = 0; i < desenData.get("yhm").size(); i++) {
            Meeting meeting = new Meeting();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == Date.class) {
                    Date value = (Date) desenData.get(field.getName()).get(i);
                    field.set(meeting, value);
                } else {
                    String value = (String) desenData.get(field.getName()).get(i);
                    field.set(meeting, value);
                }
            }
            result.add(meeting);
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
    private Meeting desenMeeting(Meeting obj, Field[] fields) throws IllegalAccessException {
        Meeting meeting = new Meeting();
        for (int k = 0; k < fields.length; k++) {
            Field field = fields[k];
            field.setAccessible(true);

            if (field.getType() == Date.class) {
                Date value = (Date) field.get(obj);
                value.setTime(value.getTime() + 86400000);
                field.set(meeting, value);
            } else {
                String value = (String) field.get(obj);
                field.set(meeting, value.substring(0, 1));
            }
        }
        return meeting;
    }
}
