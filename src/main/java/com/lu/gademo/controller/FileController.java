package com.lu.gademo.controller;

import com.alibaba.excel.EasyExcel;
import com.lu.gademo.entity.BasicData;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.ga.Meeting;
import com.lu.gademo.entity.ga.RecvFilesEntity.ExcelEntity;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.service.FileStorageService;
import com.lu.gademo.service.impl.BasicDataAnalysisEventListener;
import com.lu.gademo.service.impl.FileStorageServiceImpl;
import com.lu.gademo.service.impl.MeetingAnalysisEventListener;
import com.lu.gademo.utils.*;
import com.mashape.unirest.http.JsonNode;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Basic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文件脱敏controller
 */
@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("File")
public class FileController extends BaseController {
    private AlgorithmsFactory algorithmsFactory;
    // 系统id
    private final RecvFileDesen officeFileDesen;
    private final Boolean readyState;
    // SendBackFileName
    private String sendBackFileName;
    // 脱敏完成情况
    private boolean sendBackFlag;
    // 图像格式
    private final List<String> imageType;
    // 视频格式
    private final List<String> videoType;
    // 音频格式
    private final List<String> audioType;

    private final FileService fileService;

    private final ExcelParamService excelParamService;

    private final FileStorageService fileStorageService;

    private final LogCollectUtil logCollectUtil;

//    private MeetingAnalysisEventListener meetingAnalysisEventListener;

    @Autowired
    public FileController(AlgorithmsFactory algorithmsFactory, RecvFileDesen officeFileDesen, FileService fileService,
                          ExcelParamService excelParamService, FileStorageService fileStorageService, LogCollectUtil logCollectUtil) {
        this.algorithmsFactory = algorithmsFactory;
        this.officeFileDesen = officeFileDesen;
        this.fileService = fileService;
        this.excelParamService = excelParamService;
        this.fileStorageService = fileStorageService;
        this.readyState = true;
        this.sendBackFileName = "";
        this.sendBackFlag = false;
        this.imageType = Arrays.asList("jpg", "jpeg", "png");
        this.videoType = Arrays.asList("mp4", "avi");
        this.audioType = Arrays.asList("mp3", "wav");
        this.logCollectUtil = logCollectUtil;
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
    ) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(file);
        if (algName.equals("dpGraph")) {
            log.info("RawFileName: {}", fileStorageDetails.getRawFileName().replace(".txt", ".shp"));
            log.info("DesenFileName: {}", fileStorageDetails.getDesenFileName().replace(".txt", ".shp"));
        } else {
            log.info("RawFileName: {}", fileStorageDetails.getRawFileName());
            log.info("DesenFileName: {}", fileStorageDetails.getDesenFileName());
        }
        // 调用脱敏函数
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ResponseEntity.badRequest().body("Request file name is null".getBytes());
        }
        String fileType = getFileSuffix(fileName);
        if (algName.equals("dpGraph")) {
            log.info("File Type: " + "shp");
        } else {

            log.info("File Type: " + fileType);
        }


        log.info("AlgName: " + algName);
        log.info("Sheet: {}", sheet);
        log.info("Params: {}", params);

        // 判断数据模态
        if ("xlsx".equals(fileType)) {
            return fileService.dealExcel(fileStorageDetails, params, sheet, true);
        } else if (imageType.contains(fileType)) {
            return fileService.dealImage(fileStorageDetails, params, algName);
        } else if (videoType.contains(fileType)) {
            return fileService.dealVideo(fileStorageDetails, params, algName);
        } else if (audioType.contains(fileType)) {
            return fileService.dealAudio(fileStorageDetails, params, algName);
        } else {
            return fileService.dealGraph(fileStorageDetails, params);
        }

    }
    @PostMapping("desenShapeFile")
    public ResponseEntity<byte[]> desenShapeFile(@RequestPart("file") MultipartFile file,
                                                 @RequestParam("params") String params,
                                                 @RequestParam("algName") String algName) throws IOException, ParseException, ExecutionException, InterruptedException, TimeoutException {
        FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(file);
        return fileService.dealGraph(fileStorageDetails, params);
    }

    @PostMapping("/parseToShp")
    public ResponseEntity<Map<String, String>> parseToShp(@RequestParam("file") MultipartFile file) {
        try {
            // 上传目录
            String userDir = System.getProperty("user.dir");
            System.out.println("user.dir" + userDir);
            String uploadDir = userDir + File.separator + "graphtxt" + File.separator;
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存上传的 TXT 文件
            File txtFile = new File(uploadDir + file.getOriginalFilename());
            String originalFileName = file.getOriginalFilename();
            file.transferTo(txtFile);

            // 调用 Python 进行转换
            String txtFilePath = txtFile.getAbsolutePath();
            String shpFilePath = txtFilePath.replace(".txt", ".shp");
            convertShpToTxt(shpFilePath);

            // 读取转换后的文件并返回 Base64 编码
            Map<String, String> fileDataMap = new HashMap<>();
            String[] extensions = {".cpg", ".dbf", ".prj", ".shp", ".shx", ".txt"};

            for (String ext : extensions) {
                File convertedFile = new File(uploadDir + originalFileName.replace(".txt", ext));
                if (convertedFile.exists()) {
                    byte[] fileBytes = Files.readAllBytes(convertedFile.toPath());
                    String base64Content = Base64Utils.encodeToString(fileBytes);
                    fileDataMap.put(convertedFile.getName(), base64Content);
                }
            }

            return ResponseEntity.ok(fileDataMap);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "文件转换失败: " + e.getMessage()));
        }
    }

    public String convertShpToTxt(String shapefilePath) throws IOException {
        String python = CommandExecutor.getPythonCommand();
        String filePath = "D:\\Programming\\DesenTelecom\\graph\\poi.py";

        String txtFilePath = shapefilePath.replace(".shp", ".txt");
        String cmd = String.format("%s %s %s %s", python, filePath, shapefilePath, txtFilePath);
        CommandExecutor.openExe(cmd);

        return txtFilePath;
    }

    @PostMapping("desenSingleColumn")
    public ResponseEntity<byte[]> desenSingleColumn(@RequestPart("file") MultipartFile file,
                                                    @RequestParam("params") String params,
                                                    @RequestParam("algName") String algName) throws IOException, ParseException {
        log.info("Params: {}", params);
        log.info("AlgName: {}", algName);
        FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(file);
        return fileService.dealSingleColumnTextFile(fileStorageDetails, params, algName, false);
    }


    @PostMapping(value = {"replaceFaceVideo", "replaceFace", "removeBackground"})
    public ResponseEntity<byte[]> replaceFaceVideo(@RequestPart("file") MultipartFile file,
                                                   @RequestParam("params") String params,
                                                   @RequestParam("algName") String algName,
                                                   @RequestPart("sheet") MultipartFile sheet
    ) throws IOException, InterruptedException, SQLException, ExecutionException, TimeoutException {
        log.info("Params: {}", params);
        log.info("Sheet: {}", sheet);
        FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(file);
        FileStorageDetails sheetStorageDetails = fileStorageService.saveRawFile(sheet);

        switch (algName) {
            case "video_face_sub": {
                return fileService.replaceFaceVideo(fileStorageDetails, params, algName, sheetStorageDetails);
            }
            case "image_face_sub": {
                return fileService.replaceFace(fileStorageDetails, params, algName, sheetStorageDetails);
            }
            case "video_remove_bg": {
                return fileService.replaceVideoBackground(fileStorageDetails, params, algName, sheetStorageDetails);
            }
            default:
                throw new RuntimeException("algName error");
        }

    }

    @PostMapping(value = "desenText", produces = "application/json;charset=UTF-8")
    public String desenText(@RequestParam String textInput,
                            @RequestParam String textType,
                            @RequestParam String privacyLevel,
                            @RequestParam String algName) throws ParseException {
        log.info("PrivacyLevel: {}", privacyLevel);
        log.info("AlgName: {}", algName);
        return fileService.desenText(textInput, textType, privacyLevel, algName);

    }

    @GetMapping(value = "generateTextTestData")
    public ResponseEntity<Resource> generateTextTestData(@RequestParam("totalNumber") Integer totalNumber)
            throws IOException, ExecutionException, InterruptedException {
        log.info("开始生成失真文本算法测试文件");
        HttpHeaders headers = new HttpHeaders();
        FileStorageDetails fileStorageDetails = fileService.generateTextTestFile(totalNumber);
        log.info("RawFileName: {}", fileStorageDetails.getRawFileName());
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", fileStorageDetails.getRawFileName());
        Resource resource = new InputStreamResource(Files.newInputStream(fileStorageDetails.getRawFilePath()));
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @GetMapping(value = "downloadTextTestFile")
    public ResponseEntity<byte[]> downloadTextTestFile(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpSession httpSession = request.getSession();
        String fileName = httpSession.getAttribute("testTextFileName").toString();
        log.info("Download eigen vector file: " + fileName);
        Path filePath = fileStorageService.getRawFileDirectory().resolve(fileName);
        try {
            byte[] fileContent = Files.readAllBytes(filePath);
            headers.setContentDispositionFormData("attachment", fileName);
            return ResponseEntity.ok().headers(headers).body(fileContent);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Failed to read file.".getBytes());
        }
    }


    @PostMapping(value = "textFilePerformenceTest")
    public ResponseEntity<byte[]> textFilePerformenceTest(@RequestPart("file") MultipartFile file,
                                                          @RequestParam("params") String params,
                                                          @RequestParam("algName") String algName) {

        try {
            FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(file);
            return fileService.dealSingleColumnTextFile(fileStorageDetails, params, algName, false);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Failed to read file.".getBytes());
        } catch (ParseException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Failed to parse file.".getBytes());
        }
    }

    @ResponseBody
    @PostMapping(value = "saveExcelParams")
    public Result<String> saveExcelParams(@RequestPart("tableName") String tableName,
                                          @RequestPart("params") String params) {

        try {
            List<ExcelParam> excelParamList = logCollectUtil.jsonStringToParams(params);
            excelParamService.deleteAll(tableName + "_param");
            excelParamService.insertAll(tableName + "_param", excelParamList);
            return new Result<>(200, "ok", "Success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(400, "error", e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping(value="parseGraphShp")
    public Result<List<String>> parseGraphShp(@RequestPart("file") List<MultipartFile> files) throws IOException {
        FileStorageService fileStorageService = new FileStorageServiceImpl();
        FileStorageDetails fileStorageDetails =  fileStorageService.saveRawFile(files);

        if (fileStorageDetails.getRawFileName().isEmpty()) {
            return new Result<>(500, "error", null);
        }

        File file = new File(String.valueOf(fileStorageDetails.getRawFilePath()));

        Map<String, Object> params = new HashMap<>();
        params.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        String typeName = dataStore.getTypeNames()[0]; // 获取第一个图层
        SimpleFeatureCollection features = dataStore.getFeatureSource(typeName).getFeatures();

        List<String> elements = new ArrayList<>();
        List<String> result = new ArrayList<>();
        // 遍历特征集合
        try (SimpleFeatureIterator featureIterator = features.features()) {
            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();
                String poiString = (String) feature.getAttribute("id");
                long lineNum = (Long) feature.getAttribute("line");
                String typeCode = (String) feature.getAttribute("type_code");
//                System.out.println(lineNum  + " " + poiString + " " + typeCode);
                result.add(lineNum  + " " + poiString + " " + typeCode);
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (geometry != null) {

                    for (int i = 0; i < geometry.getNumPoints(); i++) {
                        double latitude = geometry.getCoordinates()[i].y;
                        int intLatitude = (int) latitude;
                        elements.add(String.valueOf(intLatitude));
                    }
                }
            }
        }

        dataStore.dispose();

        String rawData = String.join(",", elements);
        System.out.println(result);
        return new Result<List<String>>(200, "ok", result);
    }

    /**
     * 用于处理100w行规模的文件
     * @param file
     * @param params
     * @param algName
     * @param sheet
     * @return
     */
    @ResponseBody
    @PostMapping(value = "bigExcelDesen")
    public ResponseEntity<Resource> bigExcelDesen(@RequestPart("file") MultipartFile file,
                                            @RequestParam("params") String params,
                                            @RequestParam("algName") String algName,
                                            @RequestParam("sheet") String sheet
    ) throws IOException {
        FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(file);
        log.info("RawFileName: {}", fileStorageDetails.getRawFileName());
        log.info("DesenFileName: {}", fileStorageDetails.getDesenFileName());
        // 调用脱敏函数
        String fileName = file.getOriginalFilename();
        String fileType = getFileSuffix(fileName);
        log.info("File Type: " + fileType);
        log.info("AlgName: " + algName);
        log.info("Sheet: {}", sheet);
        List<ExcelParam> excelParamList = logCollectUtil.jsonStringToParams(params);
        Map<String, ExcelParam> config = excelParamList.parallelStream()
                .collect(Collectors.toMap(param -> param.getFieldName().trim(), Function.identity()));
        log.info("Raw File Path String: {}", fileStorageDetails.getRawFilePathString());
        EasyExcel.read(fileStorageDetails.getRawFilePathString(), Meeting.class,
                new MeetingAnalysisEventListener(algorithmsFactory, config, fileStorageDetails.getDesenFilePathString()))
                .sheet().doRead();
        excelParamList.clear();
        config.clear();

        Resource resource = new FileSystemResource(fileStorageDetails.getDesenFilePath());
//        fileStorageDetails.setRawFileBytes(new byte[0]);
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpheaders.setContentDispositionFormData("attachment", fileStorageDetails.getDesenFileName());
        excelParamList = null;
        config = null;
        fileStorageDetails = null;
        System.gc();
        return ResponseEntity.ok().headers(httpheaders).body(resource);

    }

    @ResponseBody
    @PostMapping(value = "bigExcelDesen2")
    public ResponseEntity<Resource> bigExcelDesen2(@RequestPart("file") MultipartFile file,
                                                  @RequestParam("params") String params,
                                                  @RequestParam("algName") String algName,
                                                  @RequestParam("sheet") String sheet
    ) throws IOException {
        FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(file);
        log.info("RawFileName: {}", fileStorageDetails.getRawFileName());
        log.info("DesenFileName: {}", fileStorageDetails.getDesenFileName());
        // 调用脱敏函数
        String fileName = file.getOriginalFilename();
        String fileType = getFileSuffix(fileName);
        log.info("File Type: " + fileType);
        log.info("AlgName: " + algName);
        log.info("Sheet: {}", sheet);
        List<ExcelParam> excelParamList = logCollectUtil.jsonStringToParams(params);
        Map<String, ExcelParam> config = excelParamList.parallelStream()
                .collect(Collectors.toMap(param -> param.getFieldName().trim(), Function.identity()));
        log.info("Raw File Path String: {}", fileStorageDetails.getRawFilePathString());
        EasyExcel.read(fileStorageDetails.getRawFilePathString(), BasicData.class,
                        new BasicDataAnalysisEventListener(algorithmsFactory, config, fileStorageDetails.getDesenFilePathString()))
                .sheet().doRead();
        excelParamList.clear();
        config.clear();

        Resource resource = new FileSystemResource(fileStorageDetails.getDesenFilePath());
//        fileStorageDetails.setRawFileBytes(new byte[0]);
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpheaders.setContentDispositionFormData("attachment", fileStorageDetails.getDesenFileName());
        excelParamList = null;
        config = null;
        fileStorageDetails = null;
        System.gc();
        return ResponseEntity.ok().headers(httpheaders).body(resource);

    }

    // 接收信工所Office文档
    @ResponseBody
    @PostMapping(value = "recvFileDesen", produces = "application/json;charset=UTF-8")
    Result<Object> recvFileDesen(@NotNull @RequestPart("file") MultipartFile file) {

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return new Result<>(400, "error", "Request file name is null");
        }

        String fileType = getFileSuffix(fileName);
        List<String> officeFileTypes = Arrays.asList("xlsx", "docx", "pptx");

        if (!officeFileTypes.contains(fileType)) {
            return new Result<>(400, "error", "File type not supported.");
        }
        try {
            return new Result<>(200, "ok", officeFileDesen.desenRecvFile(file));

        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(400, "error", e.getMessage());
        }

    }



    //    @GetMapping(value = "recvOnLineTaxiFile")
//    @ResponseBody
    public ResponseEntity<byte[]> recvOnLineTaxiFile() {
        List<ExcelParam> excelParamList = excelParamService.getParamsByTableName("onlinetaxi2" + "_param");
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

    @ExceptionHandler({InterruptedException.class, ExecutionException.class, TimeoutException.class, IOException.class})
    public ResponseEntity<String> handleFileProcessingExceptions(Exception ex) {
        String errorMsg = "处理文件时出错: ";
        if (ex instanceof TimeoutException) {
            errorMsg += "等待处理结果超时\n";
        } else if (ex instanceof InterruptedException) {
            errorMsg += "处理中断\n";
        } else if (ex instanceof ExecutionException) {
            errorMsg += "执行异常\n";
        } else if (ex instanceof  IOException) {
            errorMsg += "文件读写异常\n";
        }
        errorMsg += ex.getMessage();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(errorMsg);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
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


