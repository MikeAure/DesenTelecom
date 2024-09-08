package com.lu.gademo.entity.dataplatform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
//@Entity
//@Table(name = "sada_gdpi_click_dtl", schema = "userlogdtl")
public class SadaGdpiClickDtl {

    private Long sid;
    private String fSrcip;
    private String fAd;
    private Long fTs;
    private String fUrl;
    private String fRef;
    private String fUa;
    private String fDstip;
    private String fCookie;
    private String fSrcPort;
    private String fJson;
    private LocalDateTime fUpdateTime;
    private Long fDataid;

}