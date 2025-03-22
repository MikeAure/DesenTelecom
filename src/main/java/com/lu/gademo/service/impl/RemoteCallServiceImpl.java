package com.lu.gademo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dto.OFDMessage;
import com.lu.gademo.service.RemoteCallService;
import com.lu.gademo.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class RemoteCallServiceImpl implements RemoteCallService {
    private final RestTemplate restTemplate;
    private final Util util;  // 工具类，封装了getSM2Sign, getSM3Hash等方法

    public RemoteCallServiceImpl(RestTemplate restTemplate, Util util) {
        this.restTemplate = restTemplate;
        this.util = util;
    }

    @Override
    @Async("restTemplateExecutor")
    public void sendMultipartData(Path rawFilePath, Path desenFilePath, String fileType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("ofile", new FileSystemResource(rawFilePath.toFile()));
            parts.add("pfile", new FileSystemResource(desenFilePath.toFile()));
            parts.add("fileType", fileType);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

            // URL可以根据部署情况调整
            String url = "http://192.168.128.192:8470/Evaluation/ofd";
            ResponseEntity<Resource> response = restTemplate.postForEntity(url, requestEntity, Resource.class);
            log.info("multipart请求响应状态: {}", response.getStatusCode());
        } catch (RestClientException e) {
            log.error("发送multipart请求异常: {}", e.getMessage());
        }
    }

    @Override
    @Async("restTemplateExecutor")
    public void sendJsonMessage(Path desenFilePath) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 读取待签名和hash的文件内容
            byte[] fileBytes = Files.readAllBytes(desenFilePath);

            // 构建OFDMessage对象（根据实际情况设置对应字段）
            OFDMessage message = new OFDMessage();
            message.setRandomidentification("asldkfjlas;kdfj;aslkdf");
            message.setEvidenceID("232324234");
            message.setSystemIP(util.getIP());
            message.setDatasign(util.getSM2Sign(fileBytes));
            message.setDataHash(util.getSM3Hash(fileBytes));
            // 构建内部数据结构
            OFDMessage.Data data = new OFDMessage.Data();
            data.setChild_dataID("11111111111");
            data.setChild_dataPath("11111111111");
            data.setChild_systemID(22);
            data.setChild_systemIP("222");
            data.setParent_systemIP("11111222111111");
            data.setParent_systemID(22);
            data.setParent_dataID("333");
            data.setParent_dataPath("333");
            data.setSelf_dataID("33");
            data.setSelf_dataPath("333");
            data.setSelf_systemID(33);
            data.setSelf_systemIP("33333");
            data.setDataType(233);
            data.setGlobalID("1919810");
            data.setStatus(233);
            data.setMaxHops(5);
            message.setData(data);

            // 可打印完整消息用以调试

            HttpEntity<OFDMessage> requestEntity = new HttpEntity<>(message, headers);

            String url = "http://192.168.128.79:6005/save_data";
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("JSON请求响应状态: {}, 响应体: {}", response.getStatusCode(), response.getBody());

        } catch (Exception e) {
            log.error("发送JSON请求异常: {}", e.getMessage());
        }
    }
}
