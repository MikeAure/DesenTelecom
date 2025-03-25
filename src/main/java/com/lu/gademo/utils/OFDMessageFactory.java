package com.lu.gademo.utils;

import com.lu.gademo.dto.OFDMessage;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OFDMessageFactory {
    private final String systemId;
    private final int mainCmd;
    private final int subCmd;
    private final int msgVersion;
    private final LocalDateTime submitTime;

    private final int dataType;
    private final int status;
    private final int maxHops;

    private final String parentSystemId;
    private final String parentSystemIp;

    private final String selfSystemId;
    private final String selfSystemIp;

    private final String childSystemId;
    private final String childSystemIp;

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Util util;

    public OFDMessageFactory(
            @Value("${systemId.desenToolsetSystemId}") String selfSystemId,
            @Value("${desenToolSet.address}") String selfSystemIp,
            @Value("${evidenceSystem.submitEvidence.httpMainCommand}") int mainCmd,
            @Value("${evidenceSystem.submitEvidence.httpSubCommand}") int subCmd,
            @Value("${evidenceSystem.submitEvidence.httpMsgVersion}") int msgVersion,
            @Value("${evidenceSystem.submitEvidence.dataType}") int dataType,
            @Value("${evidenceSystem.submitEvidence.status}") int status,
            @Value("${evidenceSystem.submitEvidence.maxHops}") int maxHops,
            @Value("${systemId.categoryAndGradeSystemId}") String parentSystemId,
            @Value("${categoryAndGrade.address}") String parentSystemIp,
            @Value("${systemId.evaluationSystemId}") String childSystemId,
            @Value("${effectEva.address}") String childSystemIp,
            Util util
            ) {
        this.systemId = selfSystemId;
        this.mainCmd = mainCmd;
        this.subCmd = subCmd;
        this.msgVersion = msgVersion;
        this.submitTime = LocalDateTime.now();

        this.dataType = dataType;
        this.status = status;
        this.maxHops = maxHops;

        this.parentSystemId = parentSystemId;
        this.parentSystemIp = parentSystemIp;

        this.selfSystemId = selfSystemId;
        this.selfSystemIp = selfSystemIp;

        this.childSystemId = childSystemId;
        this.childSystemIp = childSystemIp;

        this.util = util;

    }

    public OFDMessage createOfdMessage() {

        OFDMessage.Data data = OFDMessage.Data.builder()
                .DataType(dataType)
                .status(status)
                .maxHops(maxHops)
                .parent_systemID(Long.decode(parentSystemId))
                .parent_systemIP(parentSystemIp)
                .self_systemID(Long.decode(selfSystemId))
                .self_systemIP(selfSystemIp)
                .child_systemID(Long.decode(childSystemId))
                .child_systemIP(childSystemIp)
                .build();
        return OFDMessage.builder()
                .systemID(Long.decode(systemId))
                .systemIP(selfSystemIp)
                .mainCMD(mainCmd)
                .subCMD(subCmd)
                .msgVersion(msgVersion)
                .submittime(DATE_FORMATTER.format(submitTime))
                .data(data)
                .build();
    }

    public OFDMessage createOfdMessage(String evidenceId, String globalId, String parentDataPath, String parentDataId,
                                       String selfDataPath, String selfDataId, String childDataPath, String childDataId,
                                       String randomIdentification) throws Exception {
        OFDMessage result = createOfdMessage();
        OFDMessage.Data dataInResult = result.getData();
        dataInResult.setGlobalID(globalId);
        dataInResult.setParent_dataPath(parentDataPath);
        dataInResult.setParent_dataID(parentDataId);
        dataInResult.setSelf_dataPath(selfDataPath);
        dataInResult.setSelf_dataID(selfDataId);
        dataInResult.setChild_dataPath(childDataPath);
        dataInResult.setChild_dataID(childDataId);

        String dataHash = util.getSM3Hash(dataInResult.toString().getBytes());
        String dataSign = util.getSM2Sign(dataInResult.toString().getBytes());

        result.setEvidenceID(evidenceId);
        result.setDataHash(dataHash);
        result.setDatasign(dataSign);
        result.setRandomidentification(randomIdentification);

        return result;

    }
}
