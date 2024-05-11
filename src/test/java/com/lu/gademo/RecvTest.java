package com.lu.gademo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dao.templateParam.onlineTaxi2ParamDao;
import com.lu.gademo.entity.RecvFilesEntity.ExcelEntity;
import com.lu.gademo.entity.templateParam.onlineTaxi2Param;
import com.lu.gademo.utils.RecvFiles;
import com.mashape.unirest.http.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.List;
import java.io.File;


@SpringBootTest
public class RecvTest {
    @Resource
    private onlineTaxi2ParamDao onlineTaxi2ParamDao;


    @Test
    public void recvExcel() throws Exception{
        JsonNode responseBody = RecvFiles.recvJson("http://10.198.37.14:30080/sourceDataController/getAllSourceData");
        System.out.println(responseBody.toString());
        List<ExcelEntity> excelEntityList = RecvFiles.parseJsonToEntities(responseBody);
        for (ExcelEntity element : excelEntityList) {
            System.out.println(element);
        }
        RecvFiles.createExcelFile(excelEntityList, "D:\\test.xlsx");
    }

    public void generateExcel() throws Exception {

    }

    @Test
    public void readJson() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode rootNode = objMapper.readTree(new File("D:\\test.json"));

        String data = rootNode.get("data").toString();
        String globalID = rootNode.get("data").get("globalID").toString();

        System.out.println(data);
        System.out.println(globalID);


    }

    @Test
    public void getColumnName() {
        StringBuilder stringBuilder = new StringBuilder();
        List<onlineTaxi2Param> list = onlineTaxi2ParamDao.findAll();
        for (onlineTaxi2Param element : list){
            stringBuilder.append(element.toString());
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
    }
}
