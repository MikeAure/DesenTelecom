package com.lu.gademo.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.effectEva.SendEvaReqDao;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.effectEva.SendEvaReq;
import com.lu.gademo.entity.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ruleCheck.SendRuleReq;
import com.lu.gademo.log.sendData;
import com.lu.gademo.service.FileService;
import com.lu.gademo.utils.DpUtil;
import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.DpUtilImpl;
import com.lu.gademo.utils.impl.UtilImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Component
public class FileServiceImpl implements FileService {
    @Autowired
    private SendEvaReqDao sendEvaReqDao;
    // 发送类
    @Autowired
    private sendData sendData;
    // 系统id
    private final int systemID = 0x31000000;
    // python命令
    String python ;
    // param  service
    @Resource
    private ExcelParamServiceImpl excelParamService;
    // 工具类
    Util util = new UtilImpl();

    // 脱敏执行主体
    String desenPerformer = "脱敏工具集";
    // 脱敏完成情况
    int desenCom = 0;
    // 脱敏对象大小
    int objectSize;

    Random ran = new Random();
    /**
     * excel文件脱敏
     */
    @Override
    public ResponseEntity<byte[]> dealImage(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException {
        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();

        String[] types = algName.split(",");
        algName = types[types.length - 1];
        System.out.println(algName);
        // python命令
        python = util.isLinux() ? "python3" : "python";
        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        // 文件名
        String rawFileName = time + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        //String rawFileName =  file.getOriginalFilename();
        // 脱敏前信息
//        String infoPre = new String(file.getBytes(), StandardCharsets.UTF_8);
        String infoPre = util.inputStreamToString(file.getInputStream());



        // 源文件保存路径
        //String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        String rawFilePath = Paths.get(currentPath, "raw_files", rawFileName).toString();
        // 保存源文件
        file.transferTo(new File(rawFilePath));
        // 脱敏前文件字节流
        byte[] rawFileBytes = Files.readAllBytes(Paths.get(rawFilePath));
        // 脱敏前文件大小
        Long rawFileSize = file.getSize();
        // 脱敏后文件信息
        //String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        String desenFileName = "desen_" + rawFileName;
        String desenFilePath = Paths.get(currentPath, "desen_files", "desen_"+rawFileName).toString();
        String desenParam = String.valueOf(params.charAt(params.length() - 1));

        String command;

        // 脱敏开始时间
        String startTime = util.getTime();
        long starttime = System.currentTimeMillis();
        if (algName.equals("dpImage")){
            // 脱敏程序路径
            //String desenApp = currentPath + File.separator + "image" + File.separator + "dpImage.py";
            String desenApp = Paths.get(currentPath, "image", "dpImage.py").toString();
            System.out.println(desenApp);
            System.out.println(rawFilePath);
            // 调用脱敏程序处理
            System.out.println("start desen");

            // 脱敏参数
            double[] param = new double[]{1.0, 0.5, 0.1};
            desenAlgParam.append(param[Integer.parseInt(desenParam)]);
            // 脱敏级别
            desenLevel.append(Integer.parseInt(desenParam) + 1);
            desenAlg.append(44);
            // 构造python命令
            command = python + " " + desenApp + " " + rawFilePath + " " + desenFilePath + " " + param[Integer.parseInt(desenParam)];
            // 根据当前操作系统类型决定是否使用conda环境
            if(!util.isLinux()){
//                String activateCommand = "conda activate dpimage";
                String conda = "conda run -n torch_env ";
                command = conda + command;

            }
            System.out.println(command);
            // 执行Python脚本
            Process pythonProcess = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            // 等待Python脚本执行完毕
            pythonProcess.waitFor();

        }
        // 非失真的医疗影像查询
        else if (algName.equals("retrieval")) {
            String desenAppPath = currentPath + File.separator + "image" + File.separator + "ImageRetrieval" + File.separator;
            String desenApp = desenAppPath + "src" + File.separator + "FUNC.py";
            String dataPath = Paths.get(currentPath, "image", "ImageRetrieval", "data").toString();
            System.out.println(desenApp);
            System.out.println(rawFilePath);
            System.out.println("start desen");

            command = python + " " + desenApp + " " + rawFilePath + " " + desenFilePath;
            if(!util.isLinux()){
//                String activateCommand = "conda activate dpimage";
                String conda = "conda run -n torch_env ";
                command = conda + command;

            }
            System.out.println(command);
            // 执行Python脚本
            Process pythonProcess = Runtime.getRuntime().exec(command, null, new File(currentPath));
            // 等待Python脚本执行完毕
            pythonProcess.waitFor();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(Files.readAllBytes(Paths.get(desenFilePath)));
        }
        else if (algName.equals("meanValueImage")){
            // 脱敏程序路径
            String desenApp = currentPath + File.separator + "image" + File.separator + algName + ".py";
            System.out.println(desenApp);
            System.out.println(rawFilePath);
            // 调用脱敏程序处理
            System.out.println("start desen");

            command = python + " " + desenApp + " " + rawFilePath + " " + desenFilePath + " " + desenParam;
            // 根据当前操作系统类型决定是否使用conda环境
            if(!util.isLinux()){
//                String activateCommand = "conda activate dpimage";
                String conda = "conda run -n torch_env ";
                command = conda + command;

            }
            System.out.println(command);
            desenAlg.append(40);
            // 脱敏参数
            int[] param = new int[]{9, 15, 21};
            desenAlgParam.append(param[Integer.parseInt(desenParam)]);
            // 脱敏级别
            desenLevel.append(Integer.parseInt(desenParam) + 1);
            // 调用脚本
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(); // 等待Python脚本执行完成
        }

        // 新增的图片交换RGB通道、图片视频添加RGB偏移值的对应Python脚本调用
        else if (algName.equals("image_exchange_channel") || algName.equals("image_add_color_offset")) {
            String desenApp = currentPath + File.separator + "image" + File.separator  + "channel_exchange.py";
            System.out.println(desenApp);
            System.out.println(rawFilePath);
            // 调用脱敏程序处理
            System.out.println("start desen");

            String[] desenParams = new String[] {"20", "50", "100"};
            command = python + " " + desenApp + " " + algName + " " + rawFilePath + " " + desenFilePath;
            // 根据当前操作系统类型决定是否使用conda环境
            if (!util.isLinux()) {
                String conda = "conda run -n torch_env ";
                command = conda + command;
            }
            if (algName.contains("add"))
            {
                desenAlgParam.append(desenParams[Integer.parseInt(desenParam)]);
                // 与Excel模板保持同步
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                command += " -p " + desenParams[Integer.parseInt(desenParam)];
                desenAlg.append("47");
                System.out.println(desenAlgParam.toString());
            } else {
                desenLevel.append("4");
                desenAlgParam.append("无参,");
                desenAlg.append("46");
            }
            System.out.println(command);

            // 调用脚本
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(); // 等待Python脚本执行完毕
        }
        else{
            // 脱敏程序路径
            String desenApp = currentPath + File.separator + "image" + File.separator  + "dealImage.py";
            System.out.println(desenApp);
            System.out.println(rawFilePath);
            // 调用脱敏程序处理
            System.out.println("start desen");
            command = python + " " + desenApp + " " + algName + " " + rawFilePath + " " + desenFilePath + " " + desenParam;
            if (!util.isLinux()) {
                String conda = "conda run -n torch_env ";
                command = conda + command;
            }
            System.out.println(command);

            switch (algName) {
                case "gaussian_blur" :
                {
                    desenAlg.append(44);
                    // 脱敏参数
                    double[] param = new double[]{3, 5, 8};
                    desenAlgParam.append(param[Integer.parseInt(desenParam)]);
                    // 脱敏级别
                    desenLevel.append(Integer.parseInt(desenParam) + 1);
                    break;
                }

                case "pixelate": {
                    desenAlg.append(42);
                    // 脱敏参数
                    double[] param = new double[]{5, 10, 15};
                    desenAlgParam.append(param[Integer.parseInt(desenParam)]);
                    // 脱敏级别
                    desenLevel.append(Integer.parseInt(desenParam) + 1);
                    break;
                }

                case "box_blur": {
                    desenAlg.append(43);
                    // 脱敏参数
                    double[] param = new double[]{2, 4, 8};
                    desenAlgParam.append(param[Integer.parseInt(desenParam)]);
                    // 脱敏级别
                    desenLevel.append(Integer.parseInt(desenParam) + 1);
                    break;
                }

                case "replace_region": {
                    desenAlg.append(43);
                    // 脱敏参数
                    int[][] param = new int[][]{{100, 100, 200, 200}, {50, 50, 300, 300},{25, 25, 400, 400}};
                    desenAlgParam.append(Arrays.toString(param[Integer.parseInt(desenParam)]));
                    // 脱敏级别
                    desenLevel.append(Integer.parseInt(desenParam) + 1);
                    break;
                }

            }

            // 调用脚本
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(); // 等待Python脚本执行完毕
        }

        // 结束时间
        long endtime = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endtime - starttime;
        System.out.println("脱敏用时" + executionTime + "ms");
        System.out.println("image desen finished");

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = 1;
        String gloID =  System.currentTimeMillis() + ran.nextInt() + "脱敏工具集";
        // 脱敏算法
        // 脱敏前类型
        desenInfoPreIden.append("image");
        // 脱敏后类型
        desenInfoAfterIden.append("image");
        // 对象大小
        objectSize = (int) file.getSize();
        // 脱敏意图
        desenIntention.append("对图像脱敏");
        // 脱敏要求
        desenRequirements.append("对图像脱敏");

        ObjectMapper objectMapper = new ObjectMapper();
        // 脱敏后信息
        FileInputStream fileInputStream = new FileInputStream(desenFilePath);

        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));
        Long desenFileSize = Files.size(Paths.get(desenFilePath));
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("image");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        // status 是固定的？
        String rawFileHash = util.getSM3Hash(rawFileBytes);
        String fileTitle = "脱敏工具集脱敏" + rawFileName + "文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(desenFileBytes);
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());
        // 发送方法
        /*Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });


        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setGlobalID(gloID);
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString());
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(rawFileBytes));
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(desenFileBytes));
        // set InfoPre rawFileName
        sendEvaReq.setDesenInfoPre(rawFileName);
        // set InfoAfter to desenFileName
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setFileType("image");
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setStatus("数据已脱敏");
        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        // 发送方法
        /*Thread evaThread = new Thread(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, infoPre.getBytes(), new byte[fileInputStream.available()], desenParam.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaThread.start();*/

        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFileName, rawFileBytes, desenFileBytes, desenParam.getBytes());
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(desenFileBytes));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
       /* Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        // 读取文件返回
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(desenFileBytes);
    }

    public static List<String> openExe(String cmd) {
        BufferedReader bufferReader = null;
        BufferedReader bufferReaderError = null;
        List<String> result = new LinkedList<>();
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            String line = null;
            bufferReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            bufferReaderError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = bufferReader.readLine()) != null  || (line = bufferReaderError.readLine()) != null) {
                result.add(line);
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public ResponseEntity<byte[]> dealExcel(MultipartFile file, String params, String sheetName) throws IOException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图:"列名+脱敏，"
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求: 列名+当前算法作用
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();

        // 读取excel文件
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        objectSize = (int) file.getSize();
        //脱敏参数处理,转为json
        ObjectMapper objectMapper = new ObjectMapper();
        List<ExcelParam> paramsData = objectMapper.readValue(params, new TypeReference<List<ExcelParam>>() {});
        // 保存参数
        excelParamService.deleteAll(sheetName + "_param");
        excelParamService.insertAll(sheetName + "_param", paramsData);

        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String time = String.valueOf(System.currentTimeMillis());

        //读取要脱敏的excel文件名
        String rawFileName = time + file.getOriginalFilename();
        String desenFileName = "desen_" + rawFileName;
        Long rawFileSize = file.getSize();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);

        // 保存源文件
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        file.transferTo(new File(rawFilePath));
        File desenDirectory = new File("desen_files");
        if (!desenDirectory.exists()) {
            desenDirectory.mkdir();
        }
        String desenFilePath = currentPath +  File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        System.out.println(desenFilePath);

        // 保存脱敏后文件
        // 脱敏文件路径
        FileOutputStream fileOutputStream = new FileOutputStream(desenFilePath);

        // 保存参数文件
        File paramDirectory = new File("desen_params");
        if (!paramDirectory.exists()) {
            paramDirectory.mkdir();
        }
        String paramsFilePath = currentPath + File.separator + "desen_params" + File.separator + "params"  + rawFileName.substring(0,rawFileName.lastIndexOf('.')) + ".txt";

        // 数据类型
        List<Integer> dataType = new ArrayList<>();
        // 工具类
        DpUtil dpUtil = new DpUtilImpl();
        // 数据行数
        int totalRowNum = sheet.getLastRowNum();
        // 字段名行
        Row fieldNameRow = sheet.getRow(0);
        // 列数
        int columnCount = fieldNameRow.getPhysicalNumberOfCells(); // 获取列数
        System.out.println("列数" + columnCount);
        // 脱敏开始时间
        String startTime = util.getTime();
        // 开始时间
        long desenStartTime = System.nanoTime();

        //  逐列处理
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            DataFormatter dataFormatter = new DataFormatter();
            // 取列名
            System.out.println(columnIndex);
            String columnName = fieldNameRow.getCell(columnIndex).toString();
            System.out.println(columnName);
            //本次操作脱敏参数
            ExcelParam param = null;
            // 遍历模板中的列名，找到匹配的脱敏参数
            for (ExcelParam paramsDatum : paramsData) {
                //System.out.println(paramsDatum.getColumnName().trim());
                if (columnName.trim().equals(paramsDatum.getColumnName().trim())) {
                    param = paramsDatum;
                }
            }

            if (param == null) {
                throw new IOException("Param is null");
            }

            System.out.println(param);
            dataType.add(param.getDataType());

            // 脱敏前信息类型标识
            desenInfoPreIden.append(columnName).append(",");
            desenInfoAfterIden.append(columnName).append(",");
            //System.out.println(param.getColumnName());

            // 读取脱敏级别
            desenLevel.append(param.getTmParam()).append(",");
            // 脱敏意图：列名+脱敏，
            desenIntention.append(param.getColumnName()).append("脱敏,");
            // 脱敏算法：添加算法对应的序号
            desenAlg.append(param.getK()).append(",");
            // 取列数据
            List<Object> objs = new ArrayList<>();
            // 从第一列开始取
            for (int rowIndex = 1; rowIndex <= totalRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    if (param.getDataType() == 3) {
                        objs.add(dataFormatter.formatCellValue(cell));
                    }
                    else{
                        objs.add(cell);
                    }
                }
            }

            // 日期处理
            if (param.getDataType() == 4 ) {
                System.out.println(objs.get(1));
                try {
                    List<Date> dates;
                    List<String> times;
                    // 加噪处理
                    // 基于差分隐私的日期加噪算法
                    if (param.getK() == 1) {
                        // 脱敏算法参数
                        Map<Integer , String> map = new HashMap<>();
                        map.put(0, "没有脱敏");
                        map.put(1, "0.1,");
                        map.put(2, "0.01,");
                        map.put(3, "0.001,");
                        // 添加脱敏参数
                        desenAlgParam.append(map.get(param.getTmParam()));
                        // 脱敏要求: 列名+当前算法作用
                        desenRequirements.append(param.getColumnName()).append("添加Laplace噪声,");
                        // 脱敏
                        dates = dpUtil.dpDate(objs, param.getTmParam());
                        // 写列数据
                        util.write2Excel(sheet, totalRowNum, columnIndex, dates);
                    }
                    // 匿名处理
                    // 使用日期分组置换算法
                    else if (param.getK() == 18){
                        // 脱敏算法参数
                        Map<Integer , String> map = new HashMap<>();
                        map.put(0, "没有脱敏");
                        map.put(1, "10,");
                        map.put(2, "30,");
                        map.put(3, "50,");
                        desenAlgParam.append(map.get(param.getTmParam()));
                        // 脱敏要求
                        desenRequirements.append(param.getColumnName()).append("进行分组置换,");
                        // 脱敏
                        dates = dpUtil.date_group_replace(objs, param.getTmParam());
                        // 写列数据
                        util.write2Excel(sheet, totalRowNum, columnIndex, dates);
                    }
                    else {
                        desenAlgParam.append("无参,");
                        // 脱敏要求:
                        desenRequirements.append(param.getColumnName()).append("取整处理,");
                        // 脱敏
                        times = dpUtil.floorTime(objs);
                        // 写列数据
                        util.write2Excel(sheet, totalRowNum, columnIndex, times);
                    }

                } catch (ParseException e) {
                    e.getStackTrace();
                }
            }

            //数值处理
            if (param.getDataType() == 0) {
                System.out.println(objs.size());
                List<Double> datas;

                // 基于拉普拉斯差分隐私的数值加噪算法
                if (param.getK() == 3) {
                    //差分隐私laplace噪声
                    // 脱敏算法参数
                    Map<Integer, String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "10,");
                    map.put(2, "1,");
                    map.put(3, "0.1,");
                    // 添加脱敏参数
                    desenAlgParam.append(map.get(param.getTmParam()));
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("添加差分隐私Laplace噪声,");
                    // 脱敏
                    datas = dpUtil.laplaceToValue(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                } else if (param.getK() == 4) {
                    Map<Integer , String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "10,");
                    map.put(2, "1,");
                    map.put(3, "0.1,");

                    desenAlgParam.append(map.get(param.getTmParam()));
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("添加差分隐私高斯噪声,");
                    // 脱敏
                    datas = dpUtil.gaussianToValue(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                } else if (param.getK() == 5) {
                    Map<Integer , String> map = new HashMap<>();
                    map.put(1, "2.0,");
                    map.put(2, "10.0,");
                    map.put(3, "20.0,");
                    desenAlgParam.append(map.get(param.getTmParam()));
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("添加随机均匀噪声,");
                    // 脱敏
                    datas = dpUtil.randomUniformToValue(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }else if (param.getK() == 6) {
                    Map<Integer , String> map = new HashMap<>();
                    map.put(1, "1.0,");
                    map.put(2, "5.0,");
                    map.put(3, "10.0,");
                    desenAlgParam.append(map.get(param.getTmParam()));
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("添加随机laplace噪声,");
                    // 脱敏
                    datas = dpUtil.randomLaplaceToValue(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);

                }else if (param.getK() == 7) {
                    Map<Integer , String> map = new HashMap<>();
                    map.put(1, "1.0,");
                    map.put(2, "5.0,");
                    map.put(3, "10.0,");
                    desenAlgParam.append(map.get(param.getTmParam()));
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("添加随机高斯噪声,");
                    // 脱敏
                    datas = dpUtil.randomGaussianToValue(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);

                }else if (param.getK() == 8) {
                    Map<Integer , String> map = new HashMap<>();
                    map.put(1, "2.3,");
                    map.put(2, "11.3,");
                    map.put(3, "23.1,");
                    desenAlgParam.append(map.get(param.getTmParam()));
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("数值偏移,");
                    // 脱敏
                    datas = dpUtil.valueShift(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);

                }else if (param.getK() == 9) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("数值取整,");
                    // 脱敏
                    List<Integer> list = dpUtil.floor(objs);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, list);

                }else if (param.getK() == 10) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("数值映射,");
                    // 脱敏
                    datas = dpUtil.valueMapping(objs);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);

                }
                else {

                    // 脱敏算法参数
                    Map<Integer , String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "10,");
                    map.put(2, "30,");
                    map.put(3, "50,");
                    desenAlgParam.append(map.get(param.getTmParam()));
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("进行分组置换,");
                    // 脱敏
                    datas = dpUtil.k_NumberCode(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }

            }

            //编码处理
            if (param.getDataType() == 1 ) {
                // 脱敏算法参数
                Map<Integer , String> map = new HashMap<>();
                map.put(0, "没有脱敏");
                map.put(1, "3.6,");
                map.put(2, "2,");
                map.put(3, "0.7,");
                desenAlgParam.append(map.get(param.getTmParam()));
                // 脱敏要求
                desenRequirements.append(param.getColumnName()).append("随机扰动,");
                // 脱敏
                List<String> dpedCode = dpUtil.dpCode(objs, param.getTmParam());
                // 写列数据
                util.write2Excel(sheet, totalRowNum, columnIndex, dpedCode);
            }

            //文本处理
            if (param.getDataType() == 3 ) {

                List<String> datas;
                if (param.getK() == 11) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("截断,");
                    // 脱敏
                    datas = dpUtil.truncation(objs);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                } else if (param.getK() == 13) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("抑制,");
                    // 脱敏
                    datas = dpUtil.suppressEmail(objs);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }else if (param.getK() == 14) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("抑制,");
                    // 脱敏
                    datas = dpUtil.addressHide(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }else if (param.getK() == 15) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("抑制,");
                    // 脱敏
                    datas = dpUtil.nameHide(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);

                }else if (param.getK() == 16) {
                    System.out.println(objs.get(0));
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("抑制,");
                    // 脱敏
                    datas = dpUtil.numberHide(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }
                else if (param.getK() == 17) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("置换,");
                    // 脱敏
                    datas = dpUtil.SHA512(objs);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }
                else if (param.getK() == 19) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("置换,");
                    // 脱敏
                    datas = dpUtil.passReplace(objs, param.getTmParam());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }
                else if (param.getK() == 20) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("置换,");
                    // 脱敏
                    datas = dpUtil.value_hide(objs);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }
                else if (param.getK() == 21) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("抑制,");
                    // 脱敏
                    datas = dpUtil.suppressAllIp(objs);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }
                else if (param.getK() == 22) {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(param.getColumnName()).append("抑制,");
                    // 脱敏
                    datas = dpUtil.suppressIpRandomParts(objs);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                }
            }
        }

        // 脱敏结束时间
        String endTime = util.getTime();

        // 结束时间
        long desenEndTime = System.nanoTime();
        System.out.println("脱敏总时间：" + (desenEndTime - desenStartTime)/10e6 + "ms");
        long oneTime = (desenEndTime - desenStartTime) / columnCount / (totalRowNum-1);
        // 打印单条运行时间
        System.out.println("单条运行时间：" + oneTime + "纳秒");
        // 一秒数据量
        System.out.println("每秒可处理条数:" + 10e9/oneTime);

        // 保存处理后的Excel数据到outputStream中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] newExcelData = byteArrayOutputStream.toByteArray();
        fileOutputStream.write(newExcelData);

        // 保存脱敏参数到文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paramsFilePath))) {
            for (ExcelParam p : paramsData) {
                // 将每个Person对象转换为字符串并写入文件
                String line = p.toString();
                writer.write(line);
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 插入数据库
        /*System.out.println(rawFilePath);
        System.out.println(outputPath);
        System.out.println("t_" + sheetName);
        System.out.println("p_" + sheetName);
        util.excelToMysql("t_" + sheetName, rawFilePath);
        util.excelToMysql("p_" + sheetName, outputPath);*/

        // 标志脱敏完成
        desenCom = 1;
        // 脱敏前信息
        // 将文件字节流转换为字符串
        String infoPre = util.inputStreamToString(inputStream);
        String gloID =  System.currentTimeMillis() + ran.nextInt() + "脱敏工具集";
        Long desenFileSize = Files.size(Paths.get(desenFilePath));

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("text");
        reqEvidenceSave.setEvidenceID(evidenceID);
        reqEvidenceSave.setMsgVersion(0x1000);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        String rawFileHash = util.getSM3Hash(infoPre.getBytes());
//        String rawFileSign;
//        try {
//            rawFileSign = util.sm2Sign(infoPre.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String fileTitle = "脱敏工具集脱敏"+rawFileName+"文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(gloID);
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        // 关键字：原始文件名+所有列名
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        // 脱敏对象（原始文件）hash
        submitEvidenceLocal.setFileHASH(rawFileHash);
        // 文件签名 = 文件hash？
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        // 脱敏前信息id（hash）
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(new String(newExcelData, StandardCharsets.UTF_8).getBytes());
        // 脱敏后信息id（hash）
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());

        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setGlobalID(gloID);
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString());
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(file.getBytes()));
        sendEvaReq.setFileType(sheetName);
        sendEvaReq.setFileSuffix("xlsx");
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(newExcelData));
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        // set infopre
        sendEvaReq.setDesenInfoPre(rawFileName);
        // set infoafter
        sendEvaReq.setDesenInfoAfter(desenFileName);
        // 设置脱敏意图
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setStatus("数据已脱敏");

        byte[] rawFileBytes = file.getBytes();
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        // 发送方法
        /*Thread evaThread = new Thread(() -> sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, outputPath, infoPre.getBytes(), newExcelData, params.getBytes()));
        evaThread.start();*/

        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, rawFileBytes, desenFileBytes, params.getBytes());
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(newExcelData));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
        /*Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        executorService.shutdown();

        // 关闭工作簿和流
        fileOutputStream.close();
        workbook.close();
        inputStream.close();
        // 返回excel给前端
        File processedExcelFile = new File(desenFilePath);
        byte[] processedExcelBytes = org.apache.commons.io.FileUtils.readFileToByteArray(processedExcelFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFilePath); // 设置文件名

        return new ResponseEntity<>(processedExcelBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> dealVideo(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException {

        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();


        // python命令
        python = util.isLinux()? "python3" : "python";
        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        // 脱敏前信息
        String infoPre = new String(file.getBytes(), StandardCharsets.UTF_8);
        // 文件名
        String rawFileName = time + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        //String rawFileName =  file.getOriginalFilename();

        // 源文件保存路径
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        Long rawFileSize = file.getSize();
        // 保存源文件
        file.transferTo(new File(rawFilePath));

        // 脱敏程序路径
        String desenApp = currentPath + File.separator + "video" + File.separator + "desenVideo.py";
        System.out.println(desenApp);
        System.out.println(rawFilePath);
        // 脱敏后
        String time2 = String.valueOf(System.currentTimeMillis());
        String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        String desenFileName = "desen_" + rawFileName;

        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        String command;
        long starttime;
        long endtime;
        long executionTime;
        String startTime;

//        String conda = "conda run -n privacy_protection ";
        command = python + " " + desenApp + " " + algName + " " + rawFilePath + " " + desenFilePath + " " + desenParam;
        // 根据当前操作系统类型决定是否使用conda环境
        if(!util.isLinux()){
            String conda = "conda run -n torch_env ";
            command = conda + command;
        }
        System.out.println(command);
        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        starttime = System.currentTimeMillis();
        // 调用脚本
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor(); // 等待Python脚本执行完毕
        // 结束时间
        endtime = System.currentTimeMillis();
        // 脱敏耗时
        executionTime = endtime - starttime;
        System.out.println("脱敏用时" + executionTime + "ms");
        System.out.println("Python脚本执行完毕，退出代码：");

//        System.out.println("脱敏文件存放路径");
//        System.out.println(desenFilePath);

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = 1;
        switch (algName) {
            case "meanValueVideo" : {
                desenAlg.append(50);
                int[] paramsTemp = new int[] {9, 15, 21};
                desenAlgParam.append(paramsTemp[Integer.parseInt(desenParam)]);
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                break;
            }
            case "gaussian_blur_video" : {
                desenAlg.append(51);
                int[] paramsTemp = new int[] {3, 5, 8};
                desenAlgParam.append(paramsTemp[Integer.parseInt(desenParam)]);
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                break;
            }
            case "pixelate_video" : {
                desenAlg.append(52);
                int[] paramsTemp = new int[] {5, 10, 15};
                desenAlgParam.append(paramsTemp[Integer.parseInt(desenParam)]);
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                break;
            }
            case "box_blur_video" : {
                desenAlg.append(53);
                int[] paramsTemp = new int[] {2, 4, 8};
                desenAlgParam.append(paramsTemp[Integer.parseInt(desenParam)]);
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                break;
            }
            case "video_add_color_offset " : {
                desenAlg.append(54);
                int[] paramsTemp = new int[] {20, 50, 100};
                desenAlgParam.append(paramsTemp[Integer.parseInt(desenParam)]);
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                break;
            }

        }
//         // 脱敏算法
//        desenAlg.append(7);
//        // 脱敏参数
//        int[] param = (algName.contains("color_offset"))? new int[]{5, 10, 20} : new int[]{20, 50 ,100};
//        desenAlgParam.append(param[Integer.parseInt(desenParam)]);
//        // 脱敏级别
//        desenLevel.append(Integer.parseInt(desenParam));
        // 脱敏前类型
        desenInfoPreIden.append("video");
        // 脱敏后类型
        desenInfoAfterIden.append("video");
        // 对象大小
        objectSize = (int) file.getSize();
        // 脱敏意图
        desenIntention.append("对视频脱敏");
        // 脱敏要求
        desenRequirements.append("对视频脱敏");

        byte[] rawFileBytes = file.getBytes();
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));

        ObjectMapper objectMapper = new ObjectMapper();

        // 脱敏后信息
        //String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        //FileInputStream fileInputStream = new FileInputStream(desenFilePath);
        FileInputStream fileInputStream = new FileInputStream(rawFilePath);
        Long desenFileSize = Files.size(Paths.get(desenFilePath));

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String test = new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8);
        System.out.println("Test from fileinputstream: " + test);
        String evidenceID = util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("video");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        String rawFileHash = util.getSM3Hash(infoPre.getBytes());
        String fileTitle = "脱敏工具集脱敏"+rawFileName+"文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg.toString() + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        //submitEvidenceLocal.setDataHash();
        //submitEvidenceLocal.setRandomidentification();
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8).getBytes());
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());

        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();

        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        //System.out.println(sendEvaReq.getEvaRequestId());
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString().substring(0,desenInfoPreIden.length()-1));
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(infoPre.getBytes()));
//        sendEvaReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(new byte[fileInputStream.available()]));
//        sendEvaReq.setDesenInfoAfter(util.getSM3Hash(new byte[fileInputStream.available()]));
        //System.out.println(new String(newExcelData, StandardCharsets.UTF_8));
        sendEvaReq.setDesenInfoAfter(desenFilePath);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setFileType("video");
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setStatus("数据已脱敏");
        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        effectEvaContent.put("oneTime", executionTime + "ms");
        // 发送方法
        /*Thread evaThread = new Thread(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, infoPre.getBytes(), new byte[fileInputStream.available()], desenParam.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaThread.start();*/
        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, rawFileBytes, desenFileBytes, desenParam.getBytes());
        });


        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
        /*Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        // 读取文件返回
        byte[] dealedVideoBytes = Files.readAllBytes(Paths.get(desenFilePath));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=video.mp4");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("video/mp4")).body(dealedVideoBytes);
    }

    @Override
    public ResponseEntity<byte[]> dealAudio(MultipartFile file, String params, String algName, String sheet) throws IOException, SQLException, InterruptedException {

        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();

        // python命令
        python = util.isLinux()? "python3" : "python";
        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        // 脱敏前信息
        String infoPre = new String(file.getBytes(), StandardCharsets.UTF_8);
        // 文件名
        String rawFileName = time + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Long rawFileSize = file.getSize();

        // 源文件,脱敏文件保存路径
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        String desenFileName = "desen_" + rawFileName;

        // 保存源文件
        file.transferTo(new File(rawFilePath));

        // 脱敏程序路径
        String desenApp = currentPath + File.separator + "audio" + File.separator + "desenAudio.py";
        System.out.println(desenApp);
        System.out.println(rawFilePath);
        // 调用脱敏程序处理
        String[] p = params.split(",");
        String desenParam = p[p.length - 1];
        String[] q = sheet.split(",");
//        System.out.println(q);
        String block_num = q[q.length - 1];
        System.out.println(block_num);

        String command = python + " "  + desenApp + " " + algName + " " + rawFilePath + " " + desenFilePath + " " + desenParam + " " + block_num;
        // 根据当前操作系统类型决定是否使用conda环境
        if(!util.isLinux()){
            String conda = "conda run -n torch_env ";
            command = conda + command;

        }
        System.out.println(command);
        // 开始时间
        // 脱敏开始时间
        String startTime = util.getTime();
        long starttime = System.currentTimeMillis();

        // 调用脚本
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        // 捕获脚本执行失败的异常，并返回给掐断
        while ((line = reader.readLine()) != null) {
            System.out.println("Python Output: " + line);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            if (line.contains("Error")) {
                return new ResponseEntity<>("Python script executes failed".getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .headers(headers)
//                        .body("Python script executes failed".getBytes());
            }
        }
        process.waitFor(); // 等待Python脚本执行完毕
        // 结束时间
        long endtime = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endtime - starttime;
        System.out.println("脱敏用时" + executionTime + "ms");
        System.out.println("Python脚本执行完毕，退出代码：");

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = 1;

        switch (algName){
            case "dpAudio": {
                // 脱敏算法
                desenAlg.append(71);
                // 脱敏参数
                double[] param = new double[]{5, 1, 0.2};
                desenAlgParam.append(param[Integer.parseInt(desenParam)]);
                // 脱敏级别
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                break;
            }

            case "remove_audio":{
                // 脱敏算法
                desenAlg.append(72);
                desenAlgParam.append("无参");
                // 脱敏级别
                desenLevel.append(4);
                break;
            }

            case "voice_replace":{
                // 脱敏算法
                desenAlg.append(73);
                desenAlgParam.append("无参");
                // 脱敏级别
                desenLevel.append(4);
                break;
            }

            case "apply_audio_effects":{
                // 脱敏算法
                desenAlg.append(74);
                // 脱敏参数
                double[][] param = new double[][]{{-3, 0.7, -5, 5}, {2, 0.8, -10, 10}, {5, 0.9, -20, 20}};
                desenAlgParam.append(Arrays.toString(param[Integer.parseInt(desenParam)]));
                // 脱敏级别
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                break;
            }

            case "audio_reshuffle":{
                // 脱敏算法
                desenAlg.append(75);
                // 脱敏参数
                double[] param = new double[]{5, 10, 15};
                desenAlgParam.append(param[Integer.parseInt(desenParam)]);
                // 脱敏级别
                desenLevel.append(Integer.parseInt(desenParam) + 1);
                break;
            }
        }

        Long desenFileSize = Files.size(Paths.get(desenFilePath));


        // 脱敏前类型
        desenInfoPreIden.append("audio");
        // 对象大小
        objectSize = (int) file.getSize();
        // 脱敏意图
        desenIntention.append("对音频脱敏");
        // 脱敏要求
        desenRequirements.append("对声纹脱敏");

        ObjectMapper objectMapper = new ObjectMapper();

        // 脱敏后信息
        FileInputStream fileInputStream = new FileInputStream(desenFilePath);
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        byte[] rawFileBytes = file.getBytes();
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes());
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("audio");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        String rawFileHash = util.getSM3Hash(infoPre.getBytes());
        String fileTitle = "脱敏工具集脱敏"+rawFileName+"文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        //submitEvidenceLocal.setDataHash();
        //submitEvidenceLocal.setRandomidentification();
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8).getBytes());
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());


        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });


        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        sendEvaReq.setEvidenceID(evidenceID);
        //System.out.println(sendEvaReq.getEvaRequestId());
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString().substring(0,desenInfoPreIden.length()-1));
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(infoPre.getBytes()));
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setFileType("audio");
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setStatus("数据已脱敏");

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        effectEvaContent.put("oneTime", executionTime + "ms");
        // 发送方法
       /* Thread evaThread = new Thread(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, infoPre.getBytes(), new byte[fileInputStream.available()], desenParam.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaThread.start();*/
        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, rawFileBytes, desenFileBytes, desenParam.getBytes());
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
       /* Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();


        // 从处理后的音频文件读取数据
        File processedAudioFile = new File(desenFilePath);
        byte[] processedAudioBytes = org.apache.commons.io.FileUtils.readFileToByteArray(processedAudioFile);

        // 设置HTTP响应头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFilePath);

        // 返回处理后的音频文件数据给前端
        return new ResponseEntity<>(processedAudioBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> dealGraph(MultipartFile file, String params) throws IOException, SQLException, InterruptedException {

        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();


        // python命令
        python = util.isLinux()? "python3" : "python";
        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        // 脱敏前信息
        String infoPre = new String(file.getBytes(), StandardCharsets.UTF_8);
        // 文件名
        String rawFileName = time + file.getOriginalFilename();
        // 文件名后缀
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        // 原始文件大小
        Long rawFileSize = file.getSize();

        // 源文件保存路径
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        // 脱敏后文件
        String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        String desenFileName = "desen_" + rawFileName;
        // 保存源文件
        file.transferTo(new File(rawFilePath));

        // 脱敏程序路径
        String desenApp = currentPath + File.separator + "graph" + File.separator + "desenGraph.py";
        System.out.println(desenApp);
        System.out.println(rawFilePath);
        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        String command = python + " " + desenApp + " " + rawFilePath + " " + desenFilePath + " " + desenParam;

        // 根据当前操作系统类型决定是否使用conda环境
        if(!util.isLinux()){
            String conda = "conda run -n torch_env ";
            command = conda + command;

        }
        System.out.println(command);
        // 开始时间
        // 脱敏开始时间
        String startTime = util.getTime();
        long starttime = System.currentTimeMillis();
        // 调用脚本
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor(); // 等待Python脚本执行完毕
        // 结束时间
        long endtime = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endtime - starttime;
        System.out.println("脱敏用时" + executionTime + "ms");
        System.out.println("Python脚本执行完毕，退出代码：");

        // 脱敏结束时间
        String endTime = util.getTime();
        Long desenFileSize = Files.size(Paths.get(desenFilePath));
        // 标志脱敏完成
        desenCom = 1;
        byte[] rawFileBytes = file.getBytes();
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));
        // 脱敏算法
        desenAlg.append(60);
        // 脱敏参数
        double[] param = new double[]{ 5.0, 1.0, 0.2};
        desenAlgParam.append(param[Integer.parseInt(desenParam)]);
        // 脱敏级别
        desenLevel.append(Integer.parseInt(params));
        // 脱敏前类型
        desenInfoPreIden.append("graph");
        // 脱敏后类型
        desenInfoAfterIden.append("graph");
        // 对象大小
        objectSize = (int) file.getSize();
        // 脱敏意图
        desenIntention.append("对图形脱敏");
        // 脱敏要求
        desenRequirements.append("对图形脱敏");

        ObjectMapper objectMapper = new ObjectMapper();
        // 脱敏文件流
        FileInputStream fileInputStream = new FileInputStream(desenFilePath);

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes());
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("graph");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        String rawFileHash = util.getSM3Hash(infoPre.getBytes());
        String fileTitle = "脱敏工具集脱敏"+rawFileName+"文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg.toString() + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        //submitEvidenceLocal.setDataHash();
        //submitEvidenceLocal.setRandomidentification();
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8).getBytes());
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());

        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });


        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.substring(0,desenInfoPreIden.length()-1));
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(infoPre.getBytes()));
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setFileType("graph");
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setStatus("数据已脱敏");

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        effectEvaContent.put("oneTime", executionTime + "ms");
        // 发送方法
       /* Thread evaThread = new Thread(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, infoPre.getBytes(), new byte[fileInputStream.available()], desenParam.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaThread.start();*/
        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFileName, rawFileBytes, desenFileBytes, desenParam.getBytes());
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        // 返回数据给前端
        File processedExcelFile = new File(desenFilePath);
        byte[] processedExcelBytes = org.apache.commons.io.FileUtils.readFileToByteArray(processedExcelFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFilePath); // 设置文件名

        return new ResponseEntity<>(processedExcelBytes, headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> dealCsv(MultipartFile file, String params, String algName) throws SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, IOException {

        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();

        // 读取文件
        InputStream inputStream = file.getInputStream();

        // python命令
        python = util.isLinux() ? "python3" : "python";

        objectSize = (int) file.getSize();
        //脱敏参数处理,转为json
        ObjectMapper objectMapper = new ObjectMapper();
        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String time = String.valueOf(System.currentTimeMillis());

        //读取要脱敏的文件名
        String rawFileName = time + file.getOriginalFilename();
        //String rawFileName =  file.getOriginalFilename();

        // 脱敏前信息
        String infoPre = new String(file.getBytes(), StandardCharsets.UTF_8);

        // 保存源文件
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        file.transferTo(new File(rawFilePath));
        Long rawFileSize = file.getSize();
        File desenDirectory = new File("desen_files");
        if (!desenDirectory.exists()) {
            desenDirectory.mkdir();
        }

        // 脱敏文件路径
        String desenFilePath = currentPath +  File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        String desenFileName = "desen_" + rawFileName;
        System.out.println(desenFilePath);

        // 脱敏程序路径
        String command;
        String desenApp = currentPath + File.separator + "generalization" + File.separator + algName + ".py";
        System.out.println(desenApp);
        System.out.println(rawFilePath);

        // 脱敏参数
        String privacyLevel = String.valueOf(params.charAt(params.length() - 1));
        int[] k_anonymity_param = new int[]{5, 10, 20};
        int[] l_diversity_param = new int[]{2, 4, 6};
        double[] t_closeness_param = new double[] {0.6, 0.4, 0.2};
        String algParam ;
        switch (algName) {
            case "k_anonymity":
                algParam = String.valueOf(k_anonymity_param[Integer.parseInt(privacyLevel)]);
                break;
            case "l_diversity":
                algParam = String.valueOf(l_diversity_param[Integer.parseInt(privacyLevel)]);
                break;
            case "t_closeness":
                algParam = String.valueOf(t_closeness_param[Integer.parseInt(privacyLevel)]);
                break;

            default:
                algParam = String.valueOf(k_anonymity_param[Integer.parseInt(privacyLevel)]);
        }
        desenAlgParam.append(algParam);
        // 脱敏级别
        desenLevel.append(privacyLevel);

        // 调用脱敏程序处理
        System.out.println("start desen");
        command = python + " " + desenApp + " " + rawFilePath + " " + desenFilePath + " " + algParam;

        // 根据当前操作系统类型决定是否使用conda环境
        if(!util.isLinux()){
            String conda = "conda run -n torch_env ";
            command = conda + command;

        }

        System.out.println(command);
        desenAlg.append(7);

        // 脱敏开始时间
        String startTime = util.getTime();
        // 开始时间
        long desenStartTime = System.nanoTime();

        // 调用脚本
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor(); // 等待Python脚本执行完毕

        // 脱敏结束时间
        String endTime = util.getTime();

        // 结束时间
        long desenEndTime = System.nanoTime();
        System.out.println("脱敏总时间：" + (desenEndTime - desenStartTime)/10e6 + "ms");

        // 标志脱敏完成
        desenCom = 1;
        byte[] rawFileBytes = file.getBytes();
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));
        // 脱敏前类型
        desenInfoPreIden.append("csv文件");
        // 脱敏后类型
        desenInfoAfterIden.append("csv文件");
        // 对象大小
        objectSize = (int) file.getSize();
        // 脱敏意图
        desenIntention.append("对csv文件脱敏");
        // 脱敏要求
        desenRequirements.append("对csv文件脱敏");

        // 脱敏后信息
        FileInputStream fileInputStream = new FileInputStream(desenFilePath);
        // 脱敏后文件大小
        Long desenFileSize = Files.size(Paths.get(desenFilePath));
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        // 存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes());
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("text");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        String rawFileHash = util.getSM3Hash(infoPre.getBytes());
        String fileTitle = "脱敏工具集脱敏"+rawFileName+"文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8).getBytes());
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());

        // 发送方法
        /*Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString());
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(infoPre.getBytes()));
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setFileType("text");
        sendEvaReq.setFileSuffix("csv");
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setStatus("数据已脱敏");
        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        // 发送方法
       /* Thread evaThread = new Thread(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, outputPath, infoPre.getBytes(), new byte[fileInputStream.available()], params.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaThread.start();*/
        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFileName, rawFileBytes, desenFileBytes, algParam.getBytes());
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
       /* Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        // 关闭工作簿和流
        inputStream.close();
        // 返回excel给前端
        File processedExcelFile = new File(desenFilePath);
        byte[] processedExcelBytes = org.apache.commons.io.FileUtils.readFileToByteArray(processedExcelFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFilePath); // 设置文件名

        return new ResponseEntity<>(processedExcelBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> dealSingleExcel(MultipartFile file, String params, String algName) throws IOException, ParseException {

        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();

        // 文件类型
        StringBuilder desenFileType = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();

        System.out.println(params);
        System.out.println(algName);
        int param = Integer.parseInt(String.valueOf(params.charAt(params.length() - 1)));

        // 读取excel文件
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        objectSize = (int) file.getSize();
        //脱敏参数处理,转为json
        ObjectMapper objectMapper = new ObjectMapper();

        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String time = String.valueOf(System.currentTimeMillis());

        //读取要脱敏的excel文件名
        String rawFileName = time + file.getOriginalFilename();
        String desenFileName = "desen_" + rawFileName;
        Long rawFileSize = file.getSize();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        //String rawFileName =  file.getOriginalFilename();

        // 保存源文件
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        file.transferTo(new File(rawFilePath));
        File desenDirectory = new File("desen_files");
        if (!desenDirectory.exists()) {
            desenDirectory.mkdir();
        }
        String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        System.out.println(desenFilePath);


        // 保存脱敏后文件
        // 脱敏文件路径
        FileOutputStream fileOutputStream = new FileOutputStream(desenFilePath);

        // 保存参数文件
      /*  File paramDirectory = new File("desen_params");
        if (!paramDirectory.exists()) {
            paramDirectory.mkdir();
        }
        String paramsFilePath = currentPath + File.separator + "desen_params" + File.separator + "params" + rawFileName.substring(0, rawFileName.lastIndexOf('.')) + ".txt";*/

        // 数据类型
        List<Integer> dataType = new ArrayList<>();
        // 工具类
        DpUtil dpUtil = new DpUtilImpl();
        // 数据行数
        int lastRowNum = sheet.getLastRowNum();
        // 字段名行
        Row fieldRow = sheet.getRow(0);
        // 列数
        int columnCount = fieldRow.getPhysicalNumberOfCells(); // 获取列数
        System.out.println("列数" + columnCount);
        // 脱敏开始时间
        String startTime = util.getTime();
        // 开始时间
        long desenStartTime = System.nanoTime();

        //  逐列处理
        DataFormatter dataFormatter = new DataFormatter();
        for (int colIndex = 0; colIndex < columnCount; colIndex++) {

            // 取列名
            System.out.println(colIndex);
            String colName = fieldRow.getCell(colIndex).toString();
            System.out.println(colName);

            // 脱敏前信息类型标识
            desenInfoPreIden.append(colName);
            desenInfoAfterIden.append(colName);
            //System.out.println(param.getColumnName());
            // 读取脱敏级别
            desenLevel.append(param);

            // 脱敏意图
            desenIntention.append(colName).append("脱敏,");
            desenFileType.append("singlecolexcel/" + colName);

            /*// 脱敏算法
            if (param != null) {
                desenAlg.append(param.getK()).append(",");
            }*/

            // 取列数据
            List<Object> objs = new ArrayList<>();
            for (int j = 1; j <= lastRowNum; j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    Cell cell = row.getCell(colIndex);
                    objs.add(cell);
                   /* if (param.getDataType() == 3) {
                        objs.add(dataFormatter.formatCellValue(cell));
                    }
                    else{
                        objs.add(cell);
                    }*/
                }
            }
            switch (algName.trim()) {
                case "dpDate": {
                    // 脱敏算法参数
                    Map<Integer, String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "0.1");
                    map.put(2, "0.01");
                    map.put(3, "0.001");
                    desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    desenRequirements.append(colName).append("添加Laplace噪声,");

                    List<Date> dates = dpUtil.dpDate(objs, param);
                    desenAlg.append(1);
                    util.write2Excel(sheet, lastRowNum, colIndex, dates);
                    break;
                }
                case "dpCode": {

                    // 脱敏算法参数
                    Map<Integer, String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "3.6");
                    map.put(2, "2");
                    map.put(3, "0.7");
                    desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    desenRequirements.append(colName).append("随机扰动,");

                    List<String> datas = dpUtil.dpCode(objs, param);
                    desenAlg.append(2);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "laplaceToValue": {
                    // 脱敏算法参数
                    Map<Integer, String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "10");
                    map.put(2, "1");
                    map.put(3, "0.1");
                    desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    desenRequirements.append(colName).append("添加差分隐私Laplace噪声,");

                    List<Double> datas = dpUtil.laplaceToValue(objs, param);
                    desenAlg.append(3);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "gaussianToValue": {
                    Map<Integer, String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "10");
                    map.put(2, "1");
                    map.put(3, "0.1");

                    desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    desenRequirements.append(colName).append("添加差分隐私高斯噪声,");

                    List<Double> datas = dpUtil.gaussianToValue(objs, param);
                    desenAlg.append(4);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "randomUniformToValue": {

                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "2.0");
                    map.put(2, "10.0");
                    map.put(3, "20.0");
                    desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    desenRequirements.append(colName).append("添加随机均匀噪声,");

                    List<Double> datas = dpUtil.randomUniformToValue(objs, param);
                    desenAlg.append(5);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "randomLaplaceToValue": {

                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "1.0");
                    map.put(2, "5.0");
                    map.put(3, "10.0");
                    desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    desenRequirements.append(colName).append("添加随机laplace噪声,");
                    List<Double> datas = dpUtil.randomLaplaceToValue(objs, param);
                    desenAlg.append(6);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "randomGaussianToValue": {
                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "1.0");
                    map.put(2, "5.0");
                    map.put(3, "10.0");
                    desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    desenRequirements.append(colName).append("添加随机高斯噪声,");

                    List<Double> datas = dpUtil.randomGaussianToValue(objs, param);
                    desenAlg.append(7);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "valueShift": {
                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "2.3");
                    map.put(2, "11.3");
                    map.put(3, "23.1");
                    desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    desenRequirements.append(colName).append("数值偏移,");

                    List<Double> datas = dpUtil.valueShift(objs, param);
                    desenAlg.append(8);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "floor": {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("数值取整,");

                    List<Integer> datas = dpUtil.floor(objs);
                    desenAlg.append(9);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "valueMapping": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("数值映射,");

                    List<Double> datas = dpUtil.valueMapping(objs);
                    desenAlg.append(10);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "truncation": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("截断,");

                    List<String> datas = dpUtil.truncation(objs);
                    desenAlg.append(11);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "floorTime": {

                    List<String> datas = dpUtil.floorTime(objs);
                    desenAlg.append(12);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "suppressEmail": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("抑制,");

                    List<String> datas = dpUtil.suppressEmail(objs);
                    desenAlg.append(13);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "addressHide": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("抑制,");

                    List<String> datas = dpUtil.addressHide(objs, param);
                    desenAlg.append(14);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "nameHide": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("抑制,");

                    List<String> datas = dpUtil.nameHide(objs, param);
                    desenAlg.append(15);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "numberHide": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("抑制,");

                    List<String> datas = dpUtil.numberHide(objs, param);
                    desenAlg.append(16);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "SHA512": {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("置换,");
                    desenAlg.append(17);
                    List<String> datas = dpUtil.SHA512(objs);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "passReplace": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("置换,");
                    List<String> datas = dpUtil.passReplace(objs, param);
                    desenAlg.append(19);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "value_hide": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("置换,");

                    List<String> datas = dpUtil.value_hide(objs);
                    desenAlg.append(20);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "suppressAllIp": {
                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("抑制,");
                    List<String> datas = dpUtil.suppressAllIp(objs);
                    desenAlg.append(21);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
                case "suppressIpRandomParts": {

                    desenAlgParam.append("无参,");
                    // 脱敏要求
                    desenRequirements.append(colName).append("抑制,");
                    List<String> datas = dpUtil.suppressIpRandomParts(objs);
                    desenAlg.append(22);
                    util.write2Excel(sheet, lastRowNum, colIndex, datas);
                    break;
                }
            }
        }

        // 脱敏结束时间
        String endTime = util.getTime();

        // 结束时间
        long desenEndTime = System.nanoTime();
        System.out.println("脱敏总时间：" + (desenEndTime - desenStartTime) / 10e6 + "ms");
        long oneTime = (desenEndTime - desenStartTime) / columnCount / (lastRowNum - 1);
        // 打印单条运行时间
        System.out.println("单条运行时间：" + oneTime + "纳秒");
        // 一秒数据量
        System.out.println("每秒可处理条数:" + 10e9 / oneTime);

        // 保存处理后的Excel数据到outputStream中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] newExcelData = byteArrayOutputStream.toByteArray();
        fileOutputStream.write(newExcelData);

        // 标志脱敏完成
        desenCom = 1;
        byte[] rawFileBytes = file.getBytes();
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));
        // 脱敏前信息
        String infoPre = util.inputStreamToString(inputStream);
        Long desenFileSize = Files.size(Paths.get(desenFilePath));
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("text");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal sendEvidenceLocal = new SubmitEvidenceLocal();
        sendEvidenceLocal.setSystemID(systemID);
        sendEvidenceLocal.setSystemIP(util.getIP());
        sendEvidenceLocal.setMainCMD(0x0003);
        sendEvidenceLocal.setSubCMD(0x0031);
        sendEvidenceLocal.setMsgVersion(0x3110);

        String rawFileHash = util.getSM3Hash(infoPre.getBytes());
        String fileTitle = "脱敏工具集脱敏" + rawFileName + "文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg.toString() + "脱敏" + rawFileName + "文件存证记录";
        sendEvidenceLocal.setEvidenceID(evidenceID);
        sendEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        sendEvidenceLocal.setFileTitle(fileTitle);
        sendEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        sendEvidenceLocal.setFileKeyword(fileKeyword);
        sendEvidenceLocal.setDesenAlg(desenAlg.toString());
        sendEvidenceLocal.setFileSize(objectSize);
        sendEvidenceLocal.setFileHASH(rawFileHash);
        sendEvidenceLocal.setFileSig(rawFileHash);
        sendEvidenceLocal.setDesenPerformer(desenPerformer);
        sendEvidenceLocal.setDesenCom(desenCom);
        sendEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(new String(newExcelData, StandardCharsets.UTF_8).getBytes());
        sendEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        sendEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        sendEvidenceLocal.setDesenIntention(desenIntention.toString());
        sendEvidenceLocal.setDesenControlSet(desenControlSet);
        sendEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        sendEvidenceLocal.setDesenPerformStartTime(startTime);
        sendEvidenceLocal.setDesenPerformEndTime(endTime);
        sendEvidenceLocal.setDesenLevel(desenLevel.toString());

        // 发送方法
        /*Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, sendEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        //System.out.println(sendEvaReq.getEvaRequestId());
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString());
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(infoPre.getBytes()));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(newExcelData));
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setFileType("text");
        sendEvaReq.setFileSuffix("xlsx");
        sendEvaReq.setStatus("数据已脱敏");
        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));

        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, rawFileBytes, desenFileBytes, params.getBytes());
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(newExcelData));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
       /* Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/

        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        executorService.shutdown();

        // 关闭工作簿和流
        fileOutputStream.close();
        workbook.close();
        inputStream.close();
        // 返回excel给前端
        File processedExcelFile = new File(desenFilePath);
        byte[] processedExcelBytes = org.apache.commons.io.FileUtils.readFileToByteArray(processedExcelFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFilePath); // 设置文件名

        return new ResponseEntity<>(processedExcelBytes, headers, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<byte[]> replaceVideoBackground(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException {
        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();

        python = util.isLinux()? "python3" : "python";

        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }

        // 脱敏前信息
        String infoPre = new String(file.getBytes(), StandardCharsets.UTF_8);
        // 文件名
        String rawFileName = time + file.getOriginalFilename();
        // 原始文件大小
        Long rawFileSize = file.getSize();
        // 原始文件名后缀
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);

        String imageFileName = time + sheet.getOriginalFilename();
        //String rawFileName =  file.getOriginalFilename();

        // 源文件保存路径
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        String rawBgPath = currentPath + File.separator + "raw_files" + File.separator + imageFileName;

        // 保存源文件
        file.transferTo(new File(rawFilePath));
        sheet.transferTo(new File(rawBgPath));

        // 脱敏程序路径
        String desenApp = currentPath + File.separator + "video" + File.separator + "substitude_background.py";
//        System.out.println("desen app path: " + desenApp);
//        System.out.println("raw video path: " + rawFilePath);
//        System.out.println("bg image path: " + rawBgPath);
        // 脱敏后
        String time2 = String.valueOf(System.currentTimeMillis());
        String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        String desenFileName = "desen_" + rawFileName;
        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        String command;
        long starttime;
        long endtime;
        long executionTime;
        String startTime;
        command = python + " " + desenApp + " " + algName + " " + rawFilePath + " " + desenFilePath + " "  + rawBgPath + " " + desenParam;
        // 根据当前操作系统类型决定是否使用conda环境
        if(!util.isLinux()){
            String conda = "conda run -n torch_env ";
            command = conda + command;
        }
        System.out.println(command);
        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        starttime = System.currentTimeMillis();
        // 调用脚本
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor(); // 等待Python脚本执行完毕
        // 结束时间
        endtime = System.currentTimeMillis();
        // 脱敏耗时
        executionTime = endtime - starttime;
        System.out.println("脱敏用时" + executionTime + "ms");
        System.out.println("Python脚本执行完毕，退出代码：");

//        System.out.println("脱敏文件存放路径");
//        System.out.println(desenFilePath);

        // 脱敏结束时间
        String endTime = util.getTime();
        Long desenFileSize = Files.size(Paths.get(desenFilePath));
        // 标志脱敏完成
        desenCom = 1;
        // 脱敏算法
        desenAlg.append(55);
        // 脱敏参数
        int[] param = (algName.contains("color_offset"))? new int[]{5, 10, 20} : new int[]{20, 50 ,100};
        desenAlgParam.append("无参");
        // 脱敏级别
        desenLevel.append(Integer.parseInt(desenParam));
        // 脱敏前类型
        desenInfoPreIden.append("video");
        // 脱敏后类型
        desenInfoAfterIden.append("video");
        // 对象大小
        objectSize = (int) file.getSize();
        // 脱敏意图
        desenIntention.append("对视频脱敏");
        // 脱敏要求
        desenRequirements.append("对视频脱敏");

        ObjectMapper objectMapper = new ObjectMapper();

        // 脱敏后信息
        //String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        //FileInputStream fileInputStream = new FileInputStream(desenFilePath);
        FileInputStream fileInputStream = new FileInputStream(rawFilePath);

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("video");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        String rawFileHash = util.getSM3Hash(infoPre.getBytes());
        String fileTitle = "脱敏工具集脱敏"+rawFileName+"文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg.toString() + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        //submitEvidenceLocal.setDataHash();
        //submitEvidenceLocal.setRandomidentification();
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8).getBytes());
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());

        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });


        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString().substring(0,desenInfoPreIden.length()-1));
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(infoPre.getBytes()));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setFileType("video");
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setStatus("数据已脱敏");
        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        effectEvaContent.put("oneTime", executionTime + "ms");
        // 发送方法
        /*Thread evaThread = new Thread(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, infoPre.getBytes(), new byte[fileInputStream.available()], desenParam.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaThread.start();*/
        Future<?> future_effect = executorService.submit(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, infoPre.getBytes(), new byte[fileInputStream.available()], desenParam.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
        /*Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        byte[] dealtVideoBytes = Files.readAllBytes(Paths.get(desenFilePath));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=video.mp4");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("video/mp4")).body(dealtVideoBytes);

    }

    @Override
    public ResponseEntity<byte[]> replaceFace(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException {
        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();

        System.out.println("replaceFace params: " + params);
        System.out.println("replaceFace algName: " + algName);

        python = util.isLinux()? "python3" : "python";

        // 当前路径
        File directory = new File("");
        String currentPath = Paths.get("./").toAbsolutePath().normalize().toString();
        System.out.println("currentPath: " + currentPath);
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        Path rawDirectory = Paths.get("./raw_files");
        if (!Files.exists(rawDirectory)) {
            Files.createDirectory(rawDirectory);
        }
        System.out.println("raw_files path: " + rawDirectory.normalize().toAbsolutePath().toString());
        // 脱敏前信息
        String infoPre = new String(file.getBytes(), StandardCharsets.UTF_8);
        // 文件名
        String rawFileName = time + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Long rawFileSize = file.getSize();
        // 背景图片名
        String imageFileName = time + sheet.getOriginalFilename();
        //String rawFileName =  file.getOriginalFilename();

        // 源文件保存路径
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;
        // 目标图片保存路径
        String rawFacePath = currentPath + File.separator + "raw_files" + File.separator + imageFileName;

        // 保存源文件
        file.transferTo(new File(rawFilePath));
        sheet.transferTo(new File(rawFacePath));

        // 脱敏程序路径
        String desenAppDir = currentPath + File.separator + "image" + File.separator + "FaceReplace" + File.separator;
        String desenApp = desenAppDir + "image_inference.py";
        System.out.println("desen app path: " + desenApp);
        System.out.println("raw video path: " + rawFilePath);
        System.out.println("bg image path: " + rawFacePath);
        // 脱敏后
        String time2 = String.valueOf(System.currentTimeMillis());
        String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        String desenFileName = "desen_" + rawFileName;

        // 调用脱敏程序处理
        desenAlgParam.append("无参,");
        String desenParam = desenAlgParam.toString();
        String command;
        long starttime;
        long endtime;
        long executionTime;
        String startTime;
        command = python + " " + desenApp + " " + rawFilePath + " " + rawFacePath + " " + desenFilePath;

        // 根据当前操作系统类型决定是否使用conda环境
        if(!util.isLinux()){
            String conda = "conda run -n torch_env ";
            command = conda + command;
        }
        System.out.println(command);
        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        starttime = System.currentTimeMillis();

        // 调用脚本
        Process process;
        process = Runtime.getRuntime().exec(command, null, new File(desenAppDir));

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        // 捕获脚本执行失败的异常，并返回给掐断
        while ((line = reader.readLine()) != null) {
            System.out.println("Python Output: " + line);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            if (line.contains("Error")) {
                return new ResponseEntity<>("Python script executes failed".getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .headers(headers)
//                        .body("Python script executes failed".getBytes());
            }
        }

        process.waitFor(); // 等待Python脚本执行完毕
        // 结束时间
        endtime = System.currentTimeMillis();
        // 脱敏耗时
        executionTime = endtime - starttime;
        System.out.println("脱敏用时" + executionTime + "ms");
        System.out.println("Python脚本执行完毕，退出代码：");

//        System.out.println("脱敏文件存放路径");
//        System.out.println(desenFilePath);

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = 1;
        // 脱敏算法
        desenAlg.append(48);
        // 脱敏前类型
        desenInfoPreIden.append("image");
        // 脱敏后类型
        desenInfoAfterIden.append("image");
        // 对象大小
        objectSize = (int) file.getSize();
        // 脱敏意图
        desenIntention.append("对图像脱敏");
        // 脱敏要求
        desenRequirements.append("对图像脱敏");
        // 脱敏后文件大小
        Long desenFileSize = Files.size(Paths.get(desenFilePath));

        ObjectMapper objectMapper = new ObjectMapper();
        // 脱敏后信息
        FileInputStream fileInputStream = new FileInputStream(desenFilePath);

        // 脱敏前文件字节流
        byte[] rawFileBytes = Files.readAllBytes(Paths.get(rawFilePath));
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("image");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        // status是固定的？
        String rawFileHash = util.getSM3Hash(rawFileBytes);
        String fileTitle = "脱敏工具集脱敏" + rawFileName + "文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(desenFileBytes);
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());
        // 发送方法
        /*Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });


        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString().substring(0,desenInfoPreIden.length()-1));
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(infoPre.getBytes()));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setFileType("image");
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setStatus("数据已脱敏");
        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        // 发送方法
        /*Thread evaThread = new Thread(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, infoPre.getBytes(), new byte[fileInputStream.available()], desenParam.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaThread.start();*/

        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, rawFileBytes, desenFileBytes, desenParam.getBytes());
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(desenFileBytes));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
       /* Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();
//
        HttpHeaders headers = new HttpHeaders();
        if (rawFileName.contains("jpg")){
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (rawFileName.contains("png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        }
        return ResponseEntity.ok()
                .headers(headers).body(desenFileBytes);
    }

    @Override
    public ResponseEntity<byte[]> replaceFaceVideo( MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException{

        // 脱敏前信息类型标识
        StringBuilder desenInfoPreIden = new StringBuilder();
        // 脱敏后信息类型标识
        StringBuilder desenInfoAfterIden = new StringBuilder();
        // 脱敏意图
        StringBuilder desenIntention = new StringBuilder();
        //List<String> desenIntention = new ArrayList<>();
        // 脱敏要求
        StringBuilder desenRequirements = new StringBuilder();
        //List<String> desenRequirements = new ArrayList<>();
        // 脱敏控制集合
        String desenControlSet = "densencontrolset";
        //List<String> desenControlSet = new ArrayList<>();
        // 脱敏参数
        StringBuilder desenAlgParam = new StringBuilder();
        //List<String> desenAlgParam = new ArrayList<>();
        // 脱敏级别
        StringBuilder desenLevel = new StringBuilder();
        //List<Integer> desenLevel = new ArrayList<>();
        // 脱敏算法
        StringBuilder desenAlg = new StringBuilder();
        //List<Integer> desenAlg = new ArrayList<>();

        python = util.isLinux()? "python3" : "python";

        // 当前路径
        File directory = new File("");
        String currentPath = Paths.get("./").toAbsolutePath().normalize().toString();
        System.out.println("currentPath: " + currentPath);
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        Path rawDirectory = Paths.get("./raw_files");
        if (!Files.exists(rawDirectory)) {
            Files.createDirectory(rawDirectory);
        }
        System.out.println("raw_files path: " + rawDirectory.toAbsolutePath().toString());
        // 脱敏前信息
        String infoPre = new String(file.getBytes(), StandardCharsets.UTF_8);
        // 文件名
        String rawFaceName = time + sheet.getOriginalFilename();
        String rawFileName = time + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf("."));
        //String rawFileName =  file.getOriginalFilename();

        // 源文件保存路径
        String rawFacePath = currentPath + File.separator + "raw_files" + File.separator + rawFaceName;
        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;

        // 保存源文件
        file.transferTo(new File(rawFilePath));
        sheet.transferTo(new File(rawFacePath));
        Long rawFileSize = sheet.getSize();
        // 脱敏程序路径
        String desenAppDir = currentPath + File.separator + "image" + File.separator + "FaceReplace" + File.separator;
        String desenApp = desenAppDir + "video_inference.py";
        System.out.println("desen app path: " + desenApp);
        System.out.println("raw image path: " + rawFacePath);
        System.out.println("raw video path: " + rawFilePath);
        // 脱敏后
        String time2 = String.valueOf(System.currentTimeMillis());
        String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        String desenFileName = "desen_" + rawFileName;
        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        String command;
        long starttime;
        long endtime;
        long executionTime;
        String startTime;
        command = python + " " + desenApp + " " + rawFilePath  + " " +  rawFacePath + " " + desenFilePath;
        // 根据当前操作系统类型决定是否使用conda环境
        if(!util.isLinux()){
            String conda = "conda run -n torch_env ";
            command = conda + command;
        }
        System.out.println(command);
        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        starttime = System.currentTimeMillis();
//        conda += "--cwd " + desenAppDir + " ";

        // 调用脚本
        Process process = Runtime.getRuntime().exec(command, null, new File(desenAppDir));

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        // 捕获脚本执行失败的异常，并返回给掐断
        while ((line = reader.readLine()) != null) {
            System.out.println("Python Output: " + line);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            if (line.contains("Error")) {
                return new ResponseEntity<>("Python script executes failed".getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        process.waitFor(); // 等待Python脚本执行完毕
        // 结束时间
        endtime = System.currentTimeMillis();
        // 脱敏耗时
        executionTime = endtime - starttime;
        System.out.println("脱敏用时" + executionTime + "ms");
        System.out.println("Python脚本执行完毕，退出代码：");

        Long desenFileSize = Files.size(Paths.get(desenFilePath));
//        System.out.println("脱敏文件存放路径");
//        System.out.println(desenFilePath);

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = 1;

        // rawFileBytes
        byte[] rawFileBytes = file.getBytes();
        byte[] desenFileBytes = Files.readAllBytes(Paths.get(desenFilePath));

        // 脱敏算法
        desenAlg.append(56);
        // 脱敏参数
        int[] param = (algName.contains("color_offset"))? new int[]{5, 10, 20} : new int[]{20, 50 ,100};
        desenAlgParam.append("无参,");
        // 脱敏级别
        desenLevel.append(Integer.parseInt(desenParam));
        // 脱敏前类型
        desenInfoPreIden.append("video");
        // 脱敏后类型
        desenInfoAfterIden.append("video");
        // 对象大小
        objectSize = (int) file.getSize();
        // 脱敏意图
        desenIntention.append("对视频脱敏");
        // 脱敏要求
        desenRequirements.append("对视频脱敏");

        ObjectMapper objectMapper = new ObjectMapper();

        // 脱敏后信息
        //String desenFilePath = currentPath + File.separator + "desen_files" + File.separator + "desen_" + rawFileName;
        //FileInputStream fileInputStream = new FileInputStream(desenFilePath);
        FileInputStream fileInputStream = new FileInputStream(rawFilePath);

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(0x0001);
        reqEvidenceSave.setSubCMD(0x0031);
        reqEvidenceSave.setObjectSize(objectSize);
        reqEvidenceSave.setObjectMode("video");
        reqEvidenceSave.setEvidenceID(evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(0x0003);
        submitEvidenceLocal.setSubCMD(0x0031);
        submitEvidenceLocal.setMsgVersion(0x3110);

        String rawFileHash = util.getSM3Hash(infoPre.getBytes());
        String fileTitle = "脱敏工具集脱敏"+rawFileName+"文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg.toString() + "脱敏" + rawFileName + "文件存证记录";
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        String fileKeyword = rawFileName + desenInfoPreIden;
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(objectSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        submitEvidenceLocal.setDesenCom(desenCom);
        //submitEvidenceLocal.setDataHash();
        //submitEvidenceLocal.setRandomidentification();
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        String desenFileHash = util.getSM3Hash(new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8).getBytes());
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());

        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });


        // 效果评测系统
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setGlobalID(System.currentTimeMillis() + ran.nextInt() + "脱敏工具集");
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString().substring(0,desenInfoPreIden.length()-1));
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(infoPre.getBytes()));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setFileType("video");
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setStatus("数据已脱敏");
        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        effectEvaContent.put("oneTime", executionTime + "ms");
        // 发送方法
        /*Thread evaThread = new Thread(() -> {
            try {
                sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, infoPre.getBytes(), new byte[fileInputStream.available()], desenParam.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaThread.start();*/
        Future<?> future_effect = executorService.submit(() -> {

            sendData.send2EffectEva(effectEvaContent, sendEvaReq, rawFileName, desenFilePath, rawFileBytes, desenFileBytes, desenParam.getBytes());

        });


        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(util.getSM3Hash((new String(new byte[fileInputStream.available()], StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(new byte[fileInputStream.available()]));
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(infoPre.getBytes()));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        sendRuleReq.setDesenCom(desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
        // 发送
        /*Thread ruleThread = new Thread(() -> sendData.send2RuleCheck(ruleCheckContent, sendRuleReq));
        ruleThread.start();*/
        Future<?> future_rule = executorService.submit(() -> {
            sendData.send2RuleCheck(ruleCheckContent, sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        byte[] dealtVideoBytes = Files.readAllBytes(Paths.get(desenFilePath));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=video.mp4");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("video/mp4")).body(dealtVideoBytes);
    }

}

