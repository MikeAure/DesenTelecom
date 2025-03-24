package com.lu.gademo.service.impl;

import com.lu.gademo.dto.FileInfoDto;
import com.lu.gademo.dto.OFDMessage;
import com.lu.gademo.dto.SendToClass4Dto;
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

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class RemoteCallServiceImpl implements RemoteCallService {
    private final RestTemplate restTemplate;
    private final Util util;  // 工具类，封装了getSM2Sign, getSM3Hash等方法


    public RemoteCallServiceImpl(RestTemplate restTemplate,
                                 Util util) {
        this.restTemplate = restTemplate;
        this.util = util;
    }

    @Override
    @Async("restTemplateExecutor")
    public CompletableFuture<Resource> sendMultipartData(Path rawFilePath, Path desenFilePath,
                                                         FileInfoDto fileType, String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("ofile", new FileSystemResource(rawFilePath.toFile()));
            parts.add("pfile", new FileSystemResource(desenFilePath.toFile()));

            // 针对fileType部分创建一个专用的HttpEntity，并设置ContentType为application/json
            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<FileInfoDto> jsonPart = new HttpEntity<>(fileType, jsonHeaders);
            parts.add("params", jsonPart);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

            // URL可以根据部署情况调整
            ResponseEntity<Resource> response = restTemplate.postForEntity(url, requestEntity, Resource.class);
            log.info("multipart请求响应状态: {}", response.getStatusCode());

            return CompletableFuture.completedFuture(response.getBody());

        } catch (RestClientException e) {
            log.error("发送multipart请求异常: {}", e.getMessage());
            CompletableFuture<Resource> future = new CompletableFuture<>();
            future.completeExceptionally(e);
        }
        return null;
    }

    @Override
    @Async("restTemplateExecutor")
    public CompletableFuture<String> sendCirculationLog(OFDMessage ofdMessage, String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 可打印完整消息用以调试
            HttpEntity<OFDMessage> requestEntity = new HttpEntity<>(ofdMessage, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("JSON请求响应状态: {}, 响应体: {}", response.getStatusCode(), response.getBody());
            return CompletableFuture.completedFuture(response.getBody());
        } catch (Exception e) {
            log.error("发送JSON请求异常: {}", e.getMessage());
            CompletableFuture<Resource> future = new CompletableFuture<>();
            future.completeExceptionally(e);
        }
        return null;
    }

    @Override
    @Async("restTemplateExecutor")
    public CompletableFuture<String> sendLevels(SendToClass4Dto sendToClass4Dto, String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 可打印完整消息用以调试
            HttpEntity<SendToClass4Dto> requestEntity = new HttpEntity<>(sendToClass4Dto, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("JSON请求响应状态: {}, 响应体: {}", response.getStatusCode(), response.getBody());
            return CompletableFuture.completedFuture(response.getBody());
        } catch (Exception e) {
            log.error("发送JSON请求异常: {}", e.getMessage());
            CompletableFuture<Resource> future = new CompletableFuture<>();
            future.completeExceptionally(e);
        }
        return null;
    }
}
