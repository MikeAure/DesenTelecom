package com.lu.gademo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dao.effectEva.SendEvaReqDao;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.effectEva.RecEvaResultInv;
import com.lu.gademo.entity.effectEva.SendEvaReq;
import com.lu.gademo.service.DesensitizationService;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.utils.Util;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DesensitizationServiceImpl implements DesensitizationService {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    Util util;
    // 效果评测Dao
    @Autowired
    private SendEvaReqDao sendEvaReqDao;
    @Autowired
    private ExcelParamService excelParamService;
    @Autowired
    private FileService fileService;

    private Path rawFileDirectory = Paths.get("raw_files");


    @Override
    public void redesen(RecEvaResultInv recEvaResultInv) throws Exception {
        String desenInfoAfterID = recEvaResultInv.getDesenInfoAfterID();
        SendEvaReq evaReq = sendEvaReqDao.findByDesenInfoAfterIden(desenInfoAfterID);
        // 字段名列表
        String[] attributeNameList = evaReq.getDesenInfoPreIden().split(",");
        String rawFileName = evaReq.getDesenInfoPre();
        String desenFileName = evaReq.getDesenInfoAfter();
        String[] desenAlgList = evaReq.getDesenAlg().split(",");
        String[] desenLevelList = evaReq.getDesenAlg().split(",");
        String templateName = evaReq.getFileType();

        List<ExcelParam> originExcelParam = excelParamService.getParams(templateName);
        for (int i = 0; i < originExcelParam.size(); i++) {
            // 根据日志修改脱敏级别
            originExcelParam.get(i).setTmParam(Integer.parseInt(desenLevelList[i]));
        }
        String jsonParamString = objectMapper.writeValueAsString(originExcelParam);
        File rawFile = rawFileDirectory.resolve(rawFileName).toFile();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String recFileName = timeStamp + "_" + rawFileName.split("_")[1];
        System.out.println(recFileName);
//        Path recFile = rawFileDirectory.resolve(recFileName);
//        Files.copy(rawFile, recFile);
//        File file = new File("/path/to/file");
        FileItem fileItem = new DiskFileItem(recFileName, Files.probeContentType(rawFile.toPath()), false,
                rawFile.getName(), (int) rawFile.length(), rawFile.getParentFile());
        try {
            InputStream input = new FileInputStream(rawFile);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);
            // Or faster..
            // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
        } catch (IOException ex) {
            // do something.
        }
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        fileService.dealExcel(multipartFile, jsonParamString, templateName, false);
    }
}
