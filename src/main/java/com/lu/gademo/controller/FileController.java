package com.lu.gademo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dao.TestEntityDao;
import com.lu.gademo.dao.effectEva.SendEvaReqDao;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.RecvFilesEntity.ExcelEntity;
import com.lu.gademo.entity.templateParam.onlineTaxi2Param;
import com.lu.gademo.log.sendData;
import com.lu.gademo.service.FileService;
import com.lu.gademo.service.impl.ExcelParamServiceImpl;
import com.lu.gademo.utils.DpUtil;
import com.lu.gademo.utils.RecvFileDesen;
import com.lu.gademo.utils.RecvFiles;
import com.lu.gademo.utils.impl.DpUtilImpl;
import com.mashape.unirest.http.JsonNode;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.lu.gademo.dao.templateParam.onlineTaxi2ParamDao;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

/**
 * 文件脱敏controller
 */
@Slf4j
@Controller
@RequestMapping("/File")
public class FileController extends BaseController {
    // 系统id
    private final int systemID = 0x31000000;
    @Autowired
    private SendEvaReqDao sendEvaReqDao;
    // 发送类
    @Autowired
    private sendData sendData;

    @Autowired
    RecvFileDesen recvFileDesen;
    // param  service
    @Resource
    private ExcelParamServiceImpl excelParamService;

    @Resource
    private TestEntityDao testEntityDao;

    @Autowired
    private onlineTaxi2ParamDao onlineTaxi2ParamDao;

    Boolean readyState = true;

    // SendBackFileName
    String sendBackFileName = "";
    // 脱敏完成情况

    boolean sendBackFlag = false;

    ObjectMapper mapper = new ObjectMapper();
    Random ran = new Random();
    // 图像格式
    List<String> imageType = Arrays.asList("jpg", "jpeg", "png");
    // 视频格式
    List<String> videoType = Arrays.asList("mp4", "avi");
    // 音频格式
    List<String> audioType = Arrays.asList("mp3", "wav");

    @Autowired
    private FileService fileService;

    /**
     * 参数分别为脱敏文件、脱敏参数
     */
    @PostMapping("desenFile")
    public ResponseEntity<byte[]> desenFile(@RequestPart("file") MultipartFile file,
                                            @RequestParam("params") String params,
                                            @RequestParam("algName") String algName,
                                            @RequestParam("sheet") String sheet
    ) throws IOException, InterruptedException, SQLException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // 调用脱敏函数
        String fileName = file.getOriginalFilename();
        System.out.println(file.getOriginalFilename());
        // 获取文件后缀
        String[] names = new String[0];
        if (fileName != null) {
            names = fileName.split("\\.");
        }
        //System.out.println(Arrays.toString(names));
        String fileType = names[names.length - 1];
        System.out.println(fileType);
        System.out.println(sheet);
        // 判断数据模态
        if ("xlsx".equals(fileType)) {
            System.out.println("excel");
            return fileService.dealExcel(file, params, sheet);
        } else if (imageType.contains(fileType)) {
            System.out.println("image");
            return fileService.dealImage(file, params, algName);
        } else if (videoType.contains(fileType)) {
            System.out.println("video");
            return fileService.dealVideo(file, params, algName);
        } else if (audioType.contains(fileType)) {
            System.out.println("audio");
            return fileService.dealAudio(file, params, algName, sheet);
        } else if ("csv".equals(fileType)) {
            System.out.println("csv");
            return fileService.dealCsv(file, params, algName);
        } else {
            System.out.println("graph");
            return fileService.dealGraph(file, params);
        }

    }

    @GetMapping("/upload")
    public String showUpload() {
        return "welcome";
    }

    @PostMapping("/result")
    public String result(@RequestPart("file") MultipartFile file, @RequestPart("file2") MultipartFile file2) {
        String fileName = file.getOriginalFilename();
        System.out.println(file.getOriginalFilename());
        // 获取文件后缀
        String[] names = new String[0];
        if (fileName != null) {
            names = fileName.split("\\.");
        }
        //System.out.println(Arrays.toString(names));
        String fileType = names[names.length - 1];
        System.out.println(fileType);

        return "file";
    }

    @ResponseBody
    @RequestMapping(value = "/desenText", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenText(@RequestParam String textInput,
                            @RequestParam String textType,
                            @RequestParam String privacyLevel,
                            @RequestParam String algName) throws ParseException {
        DpUtil dpUtil = new DpUtilImpl();
        int param = 1;
        System.out.println(algName);
        System.out.println(textInput);
        System.out.println(textType);
        System.out.println(privacyLevel);
        String result = null;
        if (!privacyLevel.isEmpty()) {
            param = Integer.parseInt(privacyLevel);
        }
        textInput = textInput.trim();
        switch (algName.trim()) {
            case "dpDate":
                List<Object> date = new ArrayList<>();
                date.add(textInput);
                result = dpUtil.dpDate(date, param).get(0) + "";
                break;
            case "addressHide":
                List<Object> address = new ArrayList<>();
                address.add(textInput);
                result = dpUtil.addressHide(address, param).get(0);
                break;
            case "numberHide":
                List<Object> number = new ArrayList<>();
                number.add(textInput);
                result = dpUtil.numberHide(number, param).get(0);
                break;
            case "laplaceToValue": {
                double val = Double.parseDouble(textInput);
                List<Object> value = new ArrayList<>();
                value.add(val);
                result = dpUtil.laplaceToValue(value, param).get(0) + "";
                break;
            }
            case "nameHide":
                List<Object> name = new ArrayList<>();
                name.add(textInput);
                result = dpUtil.nameHide(name, param).get(0);
                break;
            case "dpCode":
                String[] aaa = textInput.trim().split(",");
                List<Object> codes = new ArrayList<>(Arrays.asList(aaa));
                StringBuilder sb = new StringBuilder();
                List<String> result_b = dpUtil.dpCode(codes, param);
                for (String a : result_b) {
                    sb.append(a).append(",");
                }
                result = sb.substring(0, sb.length() - 1);
                break;
            case "passReplace": {
                List<Object> pass = new ArrayList<>();
                pass.add(textInput);
                result = dpUtil.passReplace(pass, param).get(0);
                break;
            }
            case "gaussianToValue": {
                double val = Double.parseDouble(textInput);
                List<Object> value = new ArrayList<>();
                value.add(val);
                result = dpUtil.gaussianToValue(value, param).get(0) + "";
                break;
            }
            case "randomLaplaceToValue": {
                double val = Double.parseDouble(textInput);
                List<Object> value = new ArrayList<>();
                value.add(val);
                result = dpUtil.randomLaplaceToValue(value, param).get(0) + "";
                break;
            }
            case "randomUniformToValue": {
                double val = Double.parseDouble(textInput);
                List<Object> value = new ArrayList<>();
                value.add(val);
                result = dpUtil.randomUniformToValue(value, param).get(0) + "";
                break;
            }
            case "randomGaussianToValue": {
                double val = Double.parseDouble(textInput);
                List<Object> value = new ArrayList<>();
                value.add(val);
                result = dpUtil.randomGaussianToValue(value, param).get(0) + "";
                break;
            }
            case "valueShift": {
                double val = Double.parseDouble(textInput);
                List<Object> value = new ArrayList<>();
                value.add(val);
                result = dpUtil.valueShift(value, param).get(0) + "";
                break;
            }
            case "truncation": {
                List<Object> pass = new ArrayList<>();
                pass.add(textInput);
                result = dpUtil.truncation(pass).get(0);
                break;
            }
            case "floorTime": {
                List<Object> pass = new ArrayList<>();
                pass.add(textInput);
                result = dpUtil.floorTime(pass).get(0);
                break;
            }
            case "floor": {
                double val = Double.parseDouble(textInput);
                List<Object> value = new ArrayList<>();
                value.add(val);
                result = dpUtil.floor(value).get(0) + "";
                break;
            }
            case "suppressEmail": {
                List<Object> pass = new ArrayList<>();
                pass.add(textInput);
                result = dpUtil.suppressEmail(pass).get(0);
                break;
            }
            case "value_hide": {
                List<Object> pass = new ArrayList<>();
                pass.add(textInput);
                result = dpUtil.value_hide(pass).get(0);
                break;
            }
            case "SHA512": {
                List<Object> pass = new ArrayList<>();
                pass.add(textInput);
                result = dpUtil.SHA512(pass).get(0);
                break;
            }
            case "valueMapping": {
                double val = Double.parseDouble(textInput);
                List<Object> value = new ArrayList<>();
                value.add(val);
                result = dpUtil.valueMapping(value).get(0) + "";
                break;
            }
            case "suppressAllIp": {
                List<Object> pass = new ArrayList<>();
                pass.add(textInput);
                result = dpUtil.suppressAllIp(pass).get(0);
                break;
            }
            case "suppressIpRandomParts": {
                List<Object> pass = new ArrayList<>();
                pass.add(textInput);
                result = dpUtil.suppressIpRandomParts(pass).get(0);
                break;
            }
        }
        System.out.println(result);

        return result;
    }

    @PostMapping("desenSingleExcel")
    public ResponseEntity<byte[]> desenSingleExcel(@RequestPart("file") MultipartFile file,
                                                   @RequestParam("params") String params,
                                                   @RequestParam("algName") String algName) throws IOException, ParseException {

        return fileService.dealSingleExcel(file, params, algName);
    }

    @PostMapping("removeBackground")
    public ResponseEntity<byte[]> removeBackground( @RequestPart("file") MultipartFile file,
                                                    @RequestParam("params") String params,
                                                    @RequestParam("algName") String algName,
                                                    @RequestParam("sheet") MultipartFile sheet
    ) throws IOException, InterruptedException, SQLException {
        return fileService.replaceVideoBackground(file, params, algName, sheet);
    }

    @PostMapping("replaceFace")
    public ResponseEntity<byte[]> replaceFace( @RequestPart("file") MultipartFile file,
                                                    @RequestParam("params") String params,
                                                    @RequestParam("algName") String algName,
                                                    @RequestParam("sheet") MultipartFile sheet
    ) throws IOException, InterruptedException, SQLException {
        return fileService.replaceFace(file, params, algName, sheet);
    }

    @PostMapping("replaceFaceVideo")
    public ResponseEntity<byte[]> replaceFaceVideo( @RequestPart("file") MultipartFile file,
                                                    @RequestParam("params") String params,
                                                    @RequestParam("algName") String algName,
                                                    @RequestParam("sheet") MultipartFile sheet
    ) throws IOException, InterruptedException, SQLException {
        return fileService.replaceFaceVideo(file, params, algName, sheet);
    }

    @GetMapping(value = "recvOnLineTaxiFile")
    @ResponseBody
    public ResponseEntity<byte[]> recvOnLineTaxiFile() {
        List<ExcelParam> excelParamList = excelParamService.getParams("onlinetaxi2" + "_param");
        String time = String.valueOf(System.currentTimeMillis());
        String rawFileName = "raw_" + time + ".xlsx";
        String rawFilePath = Paths.get("./raw_files").normalize().toAbsolutePath() + "\\" + rawFileName;
        byte[] result = new byte[2048];
        System.out.println(rawFilePath);
        Path rawFileDirectory = Paths.get(rawFilePath).getParent();
        try {

            if (!rawFileDirectory.toFile().exists()) {
                Files.createDirectory(rawFileDirectory);
            }

            JsonNode responseBody = RecvFiles.recvJson("http://10.198.37.14:30080/sourceDataController/getAllSourceData");
            System.out.println(responseBody.toString());
            List<ExcelEntity> excelEntityList = RecvFiles.parseJsonToEntities(responseBody);
            for (ExcelEntity element : excelEntityList) {
                if (element.getAttributeName().equals("乘客性别")){
                    String gender = element.getInfoContent();
                    if (gender.equals("男")) {
                        element.setInfoContent("M");
                    } else {
                        element.setInfoContent("F");
                    }
                }

                System.out.println(element);
            }
            // 保存Excel文件
            RecvFiles.createExcelFile(excelEntityList, rawFilePath);

            // 更新数据库参数信息
            for (ExcelParam element : excelParamList) {
                String columnName = element.getColumnName();
                for (ExcelEntity recvElement : excelEntityList) {
                    if (recvElement.getAttributeName().equals(columnName)) {
                        int newTmLevel = Integer.parseInt(recvElement.getDataLevelResult());
                        if (newTmLevel == 4) newTmLevel = 3;
                        element.setTmParam(newTmLevel);
                        break;
                    }
                }
            }

            // 显示更新后的参数
            for (ExcelParam element : excelParamList) {
                System.out.println(element);
            }

            // 更新到数据库
            excelParamService.deleteAll("onlinetaxi2" + "_param");
            excelParamService.insertAll("onlinetaxi2" + "_param", excelParamList);

            result = Files.readAllBytes(Paths.get(rawFilePath));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("error".getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        sendBackFlag = true;

        HttpHeaders headers = new HttpHeaders();
        sendBackFileName = rawFileName;
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", rawFileName); // 设置文件名

        return new ResponseEntity<>(result, headers, HttpStatus.OK);

    }

    @GetMapping(value = "getDesenFile", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDesenFile() {
        if (sendBackFileName.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "error");
            errorResponse.put("data", "Failed to read Excel file.");
            return ResponseEntity.status(500).body(errorResponse);
        }

        File excelFileDirectory = new File("./desen_files");
        // 确保路径指向的是一个目录
        if (excelFileDirectory.isDirectory()) {
            // 使用文件名过滤器来搜索特定的文件
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    // 检查文件名是否包含特定的搜索字符串并且是Excel文件
                    return name.contains(sendBackFileName) && name.endsWith(".xlsx");
                }
            };

            // 使用过滤器搜索目录
            String[] matchingFiles = excelFileDirectory.list(filter);

            // 如果找到一个或多个匹配的文件，则认为文件存在
            if (matchingFiles != null && matchingFiles.length > 0) {
                // 置空
                sendBackFileName = "";
                File excelFile = new File(excelFileDirectory, matchingFiles[0]);
                String excelFilePath = excelFile.getAbsolutePath();
                System.out.println(excelFilePath);

                if (excelFile.exists()) {
                    try (
                        FileInputStream fileInputStream = new FileInputStream(excelFile);
                         Workbook workbook = new XSSFWorkbook(fileInputStream)
                    ) {
                        Sheet sheet = workbook.getSheetAt(0);
                        List<Map<String, Object>> excelData = new ArrayList<>();
                        Row headerRow = sheet.getRow(0);

                        for (Row row : sheet) {
                            if (row.getRowNum() == 0) continue; // 跳过标题行
                            Map<String, Object> rowData = new HashMap<>();
                            for (Cell cell : row) {
                                Cell headerCell = headerRow.getCell(cell.getColumnIndex());
                                rowData.put(headerCell.toString(), cell.toString());
                            }
                            excelData.add(rowData);
                        }

                        Map<String, Object> response = new HashMap<>();
                        response.put("message", "success");
                        response.put("data", excelData);
                        return ResponseEntity.ok(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("message", "error");
                        errorResponse.put("data", "Failed to read Excel file.");
                        return ResponseEntity.status(500).body(errorResponse);
                    }
                } else {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("message", "error");
                    errorResponse.put("data", "");
                    return ResponseEntity.ok(errorResponse);
                }
            }

        } else {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "error");
            errorResponse.put("data", "Failed to read Excel file.");
            return ResponseEntity.status(500).body(errorResponse);
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "error");
        errorResponse.put("data", "Failed to read Excel file.");
        return ResponseEntity.status(500).body(errorResponse);

    }

    @GetMapping(value = "daoTest")
    @ResponseBody
    String daoTest() {
        StringBuilder stringBuilder = new StringBuilder();
        List<onlineTaxi2Param> list = onlineTaxi2ParamDao.findAll();
        for (onlineTaxi2Param element : list){
            stringBuilder.append(element.toString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @GetMapping(value = "fileDesenRequest")
    @ResponseBody
    ResponseEntity<Map<String, Object>> fileDesenRequest() {
        Map<String, Object> response = new HashMap<>();

        if (readyState) {
            response.put("message", "ok");
            response.put("data", "");
        }
        else {
            response.put("message", "error");
            response.put("data", "system is not ready");
        }

        return ResponseEntity.ok().body(response);
    }

//    @PostMapping(value = "fileDesen")
//    @ResponseBody
//    ResponseEntity<Map<String, Object>> fileDesen(@RequestPart("config") Config config,
//                                                  @NotNull @RequestPart("file") MultipartFile file) {
//        Map<String, Object> response = new HashMap<>();
//
//        String params = config.getParams();
//        String algName = config.getAlgName();
//        String sheet = config.getSheet();
//
//        System.out.println(file.getSize());
//        System.out.println(file.getOriginalFilename());
//
//        // 调用脱敏函数
//        String fileName = file.getOriginalFilename();
//        System.out.println(file.getOriginalFilename());
//        // 获取文件后缀
//        String[] names = new String[0];
//        if (fileName != null) {
//            names = fileName.split("\\.");
//        }
//        //System.out.println(Arrays.toString(names));
//        String fileType = names[names.length - 1];
//        System.out.println(fileType);
//        System.out.println(sheet);
//
//        byte[] responseData;
//        // 判断数据模态
//        try {
//            if ("xlsx".equals(fileType)) {
//                System.out.println("excel");
//                responseData =  fileService.dealExcel(file, params, sheet).getBody();
//            } else if (imageType.contains(fileType)) {
//                System.out.println("image");
//                responseData = fileService.dealImage(file, params, algName).getBody();
//            } else if (videoType.contains(fileType)) {
//                System.out.println("video");
//                responseData =  fileService.dealVideo(file, params, algName).getBody();
//            } else if (audioType.contains(fileType)) {
//                System.out.println("audio");
//                responseData =  fileService.dealAudio(file, params, algName, sheet).getBody();
//            } else if ("csv".equals(fileType)) {
//                System.out.println("csv");
//                responseData =  fileService.dealCsv(file, params, algName).getBody();
//            } else {
//                System.out.println("graph");
//                responseData =  fileService.dealGraph(file, params).getBody();
//            }
//        } catch(Exception e) {
//            response.put("message", "error");
//            response.put("data", e.getMessage());
//            return ResponseEntity.ok().body(response);
//        }
//
//        response.put("message", "ok");
//        response.put("data", responseData);
//        return ResponseEntity.ok().body(response);
//
//
//    }

    @PostMapping(value = "recvFileDesen")
    @ResponseBody
    ResponseEntity<Map<String, Object>> fileDesen(@NotNull @RequestPart("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileType = fileName.split("\\.")[fileName.split("\\.").length - 1];
        Path currentPath = Paths.get(".");
        Path rawFilePath = Paths.get(currentPath + "/raw_files" + "/" + fileName);
        Path desenFilePath = Paths.get(currentPath + "/desen_files" + "/desen_" + fileName);
        List<String> officeFileTypes = Arrays.asList("xlsx", "docx", "pptx");

        if (!officeFileTypes.contains(fileType)) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "error");
            errorResponse.put("data", "File type not supported.");
            return ResponseEntity.ok().body(errorResponse);
        }
        try {
            file.transferTo(rawFilePath.toFile());
            recvFileDesen.desenRecvFile(rawFilePath, desenFilePath);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "ok");
            successResponse.put("data", Files.readAllBytes(desenFilePath));
            return ResponseEntity.ok().body(successResponse);

        } catch (Exception e){
            log.error(e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "error");
            errorResponse.put("data", e.getMessage());
            return ResponseEntity.ok().body(errorResponse);
        }

    }

}


