package com.lu.gademo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.scp.client.ScpClient;
import org.apache.sshd.scp.client.ScpClientCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Course2Communication {
    // 远程服务器信息
    private final String remoteHost; // 远程服务器地址
    private final String userName;
    private final String password;
    private final int port;
    private final String url;

    @Autowired
    public Course2Communication(@Value("${course2.host}") String remoteHost,
                                @Value("${course2.username}") String userName,
                                @Value("${course2.password}") String password,
                                @Value("${course2.port}") int port
    ) {
        this.remoteHost = remoteHost;
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.url = "http://" + remoteHost + ":" + this.port + "/categoryAndLevel";

    }

    public void downloadFileFromRemote(String remoteFilePath, Path targetFilePath) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();

        // 跳过主机密钥检查
        client.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
        client.start();

        try (ClientSession session = client.connect(userName, remoteHost, 22)
                .verify(7, TimeUnit.SECONDS).getSession()) {

            // 添加密码认证
            session.addPasswordIdentity(password);
            session.auth().verify(5, TimeUnit.SECONDS);

            // 创建 SCPClient
            ScpClientCreator creator = ScpClientCreator.instance();
            ScpClient scpClient = creator.createScpClient(session);

            // 下载文件
            scpClient.download(remoteFilePath, targetFilePath);
            log.info("远程分类分级文件下载成功: {}", targetFilePath.toString());

        } catch (Exception e) {
            log.error("SCP 文件传输过程中出现错误: ", e);
            throw new IOException("SCP 文件下载失败", e);
        } finally {
            client.stop();
        }
    }

    public void writeJsonToFile(String sceneName) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity(this.url + "/" + sceneName, String.class);
        String jsonString = result.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        Path localFilePath = Paths.get(sceneName + ".json");

        try(BufferedWriter writer = Files.newBufferedWriter(localFilePath)){
            String dataResult = jsonNode.get("data").toPrettyString();
            if (StringUtils.isBlank(dataResult) || dataResult.equals("null")) {
                log.error("获取分类分级数据失败");
                throw new IOException("获取分类分级数据失败");
            }
            writer.write(dataResult);
            log.info("分类分级文件写入成功: {}", localFilePath.toString());
        } catch (IOException e) {
            log.error("分类分级文件写入失败: ", e);
            throw new IOException("分类分级文件写入失败");
        }
    }
}
