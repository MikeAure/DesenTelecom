package com.lu.gademo.controller;

import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.RecvFilesEntity.ExcelEntity;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.service.impl.FileStorageService;
import com.lu.gademo.utils.AlgorithmsFactory;
import com.lu.gademo.utils.RecvFileDesen;
import com.lu.gademo.utils.RecvFiles;
import com.lu.gademo.utils.Result;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
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
@CrossOrigin("*")
@RequestMapping("/File")
public class FileController extends BaseController {
    AlgorithmsFactory algorithmsFactory;
    // 系统id
    RecvFileDesen officeFileDesen;
    Boolean readyState;
    // SendBackFileName
    String sendBackFileName;
    // 脱敏完成情况
    boolean sendBackFlag;
    // 图像格式
    List<String> imageType;
    // 视频格式
    List<String> videoType;
    // 音频格式
    List<String> audioType;

    private FileService fileService;
    // param  service

    private ExcelParamService excelParamService;

    @Autowired
    public FileController(AlgorithmsFactory algorithmsFactory, RecvFileDesen officeFileDesen, FileService fileService, ExcelParamService excelParamService) {
        this.algorithmsFactory = algorithmsFactory;
        this.officeFileDesen = officeFileDesen;
        this.fileService = fileService;
        this.excelParamService = excelParamService;
        this.readyState = true;
        this.sendBackFileName = "";
        this.sendBackFlag = false;
        this.imageType = Arrays.asList("jpg", "jpeg", "png");
        this.videoType = Arrays.asList("mp4", "avi");
        this.audioType = Arrays.asList("mp3", "wav");

    }

    private String getFileSuffix(String fileName) {
        return fileName.split("\\.")[fileName.split("\\.").length - 1];
    }

    /**
     * 参数分别为脱敏文件、脱敏参数
     */
    @PostMapping("desenFile")
    public ResponseEntity<byte[]> desenFile(@RequestPart("file") MultipartFile file,
                                            @RequestParam("params") String params,
                                            @RequestParam("algName") String algName,
                                            @RequestParam("sheet") String sheet
    ) throws IOException, InterruptedException, SQLException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        FileStorageService fileStorageService = new FileStorageService();
        FileStorageDetails fileStorageDetails = fileStorageService.saveRawFile(file);
        log.info("RawFileName: {}", fileStorageDetails.getRawFileName());
        log.info("DesenFileName: {}", fileStorageDetails.getDesenFileName());
        // 调用脱敏函数
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ResponseEntity.badRequest().body("Request file name is null".getBytes());
        }

        String fileType = getFileSuffix(fileName);
        log.info("File Type: " + fileType);
        log.info("AlgName: " + algName);

        // 判断数据模态
        if ("xlsx".equals(fileType)) {
            return fileService.dealExcel(fileStorageDetails, params, sheet, true);
        } else if (imageType.contains(fileType)) {
            return fileService.dealImage(file, params, algName);
        } else if (videoType.contains(fileType)) {
            return fileService.dealVideo(file, params, algName);
        } else if (audioType.contains(fileType)) {
            return fileService.dealAudio(file, params, algName, sheet);
        } else if ("csv".equals(fileType)) {
            return fileService.dealCsv(file, params, algName);
        } else {
            return fileService.dealGraph(file, params);
        }

    }

    @PostMapping("desenSingleExcel")
    public ResponseEntity<byte[]> desenSingleColumnExcel(@RequestPart("file") MultipartFile file,
                                                         @RequestParam("params") String params,
                                                         @RequestParam("algName") String algName) throws IOException, ParseException {

        return fileService.dealSingleExcel(file, params, algName);
    }


    @PostMapping(value = {"replaceFaceVideo", "replaceFace", "removeBackground"})
    public ResponseEntity<byte[]> replaceFaceVideo(@RequestPart("file") MultipartFile file,
                                                   @RequestParam("params") String params,
                                                   @RequestParam("algName") String algName,
                                                   @RequestPart("sheet") MultipartFile sheet
    ) throws IOException, InterruptedException, SQLException {
        switch (algName) {
            case "video_face_sub": {
                return fileService.replaceFaceVideo(file, params, algName, sheet);
            }
            case "image_face_sub": {
                return fileService.replaceFace(file, params, algName, sheet);
            }
            case "video_remove_bg": {
                return fileService.replaceVideoBackground(file, params, algName, sheet);
            }
            default:
                throw new RuntimeException("algName error");
        }

    }

    @ResponseBody
    @RequestMapping(value = "desenText", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenText(@RequestParam String textInput,
                            @RequestParam String textType,
                            @RequestParam String privacyLevel,
                            @RequestParam String algName) throws ParseException {
        return fileService.desenText(textInput, textType, privacyLevel, algName);

    }


    // 接收信工所Office文档
    @PostMapping(value = "recvFileDesen", produces = "application/json;charset=UTF-8")
    @ResponseBody
    ResponseEntity<Result<Object>> recvFileDesen(@NotNull @RequestPart("file") MultipartFile file) {

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ResponseEntity.ok().body(new Result<>("error", "Request file name is null"));
        }

        String fileType = getFileSuffix(fileName);
        List<String> officeFileTypes = Arrays.asList("xlsx", "docx", "pptx");

        if (!officeFileTypes.contains(fileType)) {
            return ResponseEntity.ok().body(new Result<>("error", "File type not supported."));
        }
        try {
            return ResponseEntity.ok().body(new Result<>("ok", officeFileDesen.desenRecvFile(file)));

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok().body(new Result<>("error", e.getMessage()));
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
                if (element.getAttributeName().equals("乘客性别")) {
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
        } else {
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


