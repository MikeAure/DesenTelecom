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
//@Table(name = "sada_gdpi_click_dtl", schema = "userlogdtl")
public class SadaGdpiClickDtl {
    @Column(name = "sid")
    private Long sid;
    @Column(name = "f_srcip")
    private String fSrcip;
    @Column(name = "f_ad")
    private String fAd;
    @Column(name = "f_ts")
    private Long fTs;
    @Column(name = "f_url")
    private String fUrl;
    @Column(name = "f_ref")
    private String fRef;
    @Column(name = "f_ua")
    private String fUa;
    @Column(name = "f_dstip")
    private String fDstip;
    @Column(name = "f_cookie")
    private String fCookie;
    @Column(name = "f_src_port")
    private String fSrcPort;
    @Column(name = "f_json")
    private String fJson;
    @Column(name = "f_update_time")
    private LocalDateTime fUpdateTime;
    @Column(name = "f_dataid")
    private Long fDataid;

}