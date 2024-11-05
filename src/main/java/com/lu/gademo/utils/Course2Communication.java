package com.lu.gademo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ClassificationResult;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.mapper.ga.TypeAlgoMappingDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.scp.client.ScpClient;
import org.apache.sshd.scp.client.ScpClientCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Course2Communication {
    // 远程服务器信息
    private final String remoteHost; // 远程服务器地址
    private final String userName;
    private final String password;
    private final String remoteFilePath;  // 远程文件路径


    @Autowired
    public Course2Communication(@Value("${course2.host}") String remoteHost,
                                @Value("${course2.username}") String userName,
                                @Value("${course2.password}") String password,
                                @Value("${course2.remotePath}") String remoteFilePath) {
        this.remoteHost = remoteHost;
        this.userName = userName;
        this.password = password;
        this.remoteFilePath = remoteFilePath;
    }


    public void downloadFileFromRemote(Path targetFilePath) throws IOException {
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
}
