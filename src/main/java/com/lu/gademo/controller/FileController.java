package com.lu.gademo.controller;

import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.RecvFilesEntity.ExcelEntity;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.utils.*;
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
    @Autowired
    private FileService fileService;
    @Autowired
    AlgorithmsFactory algorithmsFactory;
    // 系统id
    @Autowired
    RecvFileDesen recvFileDesen;
    // param  service
    @Autowired
    private ExcelParamService excelParamService;
    Boolean readyState = true;
    // SendBackFileName
    String sendBackFileName = "";
    // 脱敏完成情况
    boolean sendBackFlag = false;
    // 图像格式
    List<String> imageType = Arrays.asList("jpg", "jpeg", "png");
    // 视频格式
    List<String> videoType = Arrays.asList("mp4", "avi");
    // 音频格式
    List<String> audioType = Arrays.asList("mp3", "wav");


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
        log.info(fileType);
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

    @ResponseBody
    @RequestMapping(value = "/desenText", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenText(@RequestParam String textInput,
                            @RequestParam String textType,
                            @RequestParam String privacyLevel,
                            @RequestParam String algName) throws ParseException {
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
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
        switch (algName.trim()) {
            case "dpDate":
            case "addressHide":
            case "numberHide":
            case "nameHide":
            case "passReplace":
            case "truncation":
            case "floorTime":
            case "suppressEmail":
            case "value_hide":
            case "SHA512":
            case "suppressAllIp":
            case "suppressIpRandomParts":
            {
                List<String> input = Collections.singletonList(textInput);
                DSObject rawData = new DSObject(input);
                result = algorithmInfo.execute(rawData, param).getList().get(0).toString();
                break;
            }

// {
//                List<String> input = Collections.singletonList(textInput);
//                DSObject rawData = new DSObject(input);
//                result = algorithmInfo.execute(rawData).getList().get(0).toString();
//                break;
//            }
            case "laplaceToValue":
            case "gaussianToValue":
            case "randomLaplaceToValue":
            case "randomUniformToValue":
            case "randomGaussianToValue":
            case "valueShift":
            case "floor":
            case "valueMapping": {
                double val = Double.parseDouble(textInput);
                DSObject rawVal = new DSObject(Collections.singletonList(val));
                result = algorithmInfo.execute(rawVal, param).getList().get(0) + "";
                break;
            }
            case "dpCode":
                String[] aaa = textInput.trim().split(",");
                DSObject codes = new DSObject(Arrays.asList(aaa));
                StringBuilder sb = new StringBuilder();
                List<?> result_b = algorithmInfo.execute(codes, param).getList();
                for (Object a : result_b) {
                    sb.append(a.toString()).append(",");
                }
                result = sb.substring(0, sb.length() - 1);
                break;

        }
        System.out.println(result);
        return result;
    }

    @PostMapping("desenSingleExcel")
    public ResponseEntity<byte[]> desenSingleColumnExcel(@RequestPart("file") MultipartFile file,
                                                         @RequestParam("params") String params,
                                                         @RequestParam("algName") String algName) throws IOException, ParseException {

        return fileService.dealSingleExcel(file, params, algName);
    }

//    @PostMapping("removeBackground")
//    public ResponseEntity<byte[]> removeBackground( @RequestPart("file") MultipartFile file,
//                                                    @RequestParam("params") String params,
//                                                    @RequestParam("algName") String algName,
//                                                    @RequestParam("sheet") MultipartFile sheet
//    ) throws IOException, InterruptedException, SQLException {
//        return fileService.replaceVideoBackground(file, params, algName, sheet);
//    }
//
//    @PostMapping("replaceFace")
//    public ResponseEntity<byte[]> replaceFace( @RequestPart("file") MultipartFile file,
//                                                    @RequestParam("params") String params,
//                                                    @RequestParam("algName") String algName,
//                                                    @RequestParam("sheet") MultipartFile sheet
//    ) throws IOException, InterruptedException, SQLException {
//        return fileService.replaceFace(file, params, algName, sheet);
//    }

    @PostMapping(value = {"replaceFaceVideo", "replaceFace", "removeBackground"})
    public ResponseEntity<byte[]> replaceFaceVideo( @RequestPart("file") MultipartFile file,
                                                    @RequestParam("params") String params,
                                                    @RequestParam("algName") String algName,
                                                    @RequestParam("sheet") MultipartFile sheet
    ) throws IOException, InterruptedException, SQLException {
        switch (algName) {
            case "video_face_sub":{
                return fileService.replaceFaceVideo(file, params, algName, sheet);
            }
            case "image_face_sub":{
                return fileService.replaceFace(file, params, algName, sheet);
            }
            case "video_remove_bg":{
                return fileService.replaceVideoBackground(file, params, algName, sheet);
            }
            default:
                throw new RuntimeException("algName error");
        }

    }

    // 接收信工所Office文档
    @PostMapping(value = "recvFileDesen", produces = "application/json;charset=UTF-8")
    @ResponseBody
    ResponseEntity<Map<String, Object>> recvFileDesen(@NotNull @RequestPart("file") MultipartFile file) {

        String fileName = file.getOriginalFilename();
        String fileType = fileName.split("\\.")[fileName.split("\\.").length - 1];
        List<String> officeFileTypes = Arrays.asList("xlsx", "docx", "pptx");

        if (!officeFileTypes.contains(fileType)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "error");
            errorResponse.put("data", "File type not supported.");
            return ResponseEntity.ok().body(errorResponse);
        }
        try {
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "ok");
            successResponse.put("data", recvFileDesen.desenRecvFile(file));
            return ResponseEntity.ok().body(successResponse);

        } catch (Exception e){
            log.error(e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "error");
            errorResponse.put("data", e.getMessage());
            return ResponseEntity.ok().body(errorResponse);
        }

    }

//    @GetMapping(value = "recvOnLineTaxiFile")
//    @ResponseBody
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
            log.error(e.getMessage());
            return new ResponseEntity<>("error".getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        sendBackFlag = true;

        HttpHeaders headers = new HttpHeaders();
        sendBackFileName = rawFileName;
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", rawFileName); // 设置文件名

        return new ResponseEntity<>(result, headers, HttpStatus.OK);

    }

//    @GetMapping(value = "getDesenFile", produces = "application/json;charset=UTF-8")
//    @ResponseBody
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

//    @GetMapping(value = "fileDesenRequest")
//    @ResponseBody
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

}


