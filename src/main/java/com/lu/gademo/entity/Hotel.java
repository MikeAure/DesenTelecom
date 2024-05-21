package com.lu.gademo.entity;

import com.lu.gademo.entity.support.BaseEntity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "t_hotel")
public class Hotel extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String xxbh;

    private String khbh;
    private String zjlxmc;
    private String gmffid;
    private String xm;
    private String xb;
    private String mzdm;
    //@JsonFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date birthday; //时间
    private String jzdxzqhdm;
    private String jzdxxdz;
    private String hjxzqhdm;
    private String hjdqhnxxdz;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column
    private Date rzsj;
    private String rzfjh;
    private String rzblrxm;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column
    private Date tfsj;
    private String tfblrxm;
    private String ldbm;
    private String ldmc;
    private String ldxzqhdm;
    private String lddzmc;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column
    private Date ywyksj; //时间
    private String sspcsxzqbdm;
    private String scpcsmc;
    private String lxdh;
    private String fddbrxm;
    private String sjklyxzqdm;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column
    private Date zhsj;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column
    private Date rksj;

    //@Column(columnDefinition="tinyint(1) default 1")
    private String sfzxpdbs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXxbh() {
        return xxbh;
    }

    public void setXxbh(String xxbh) {
        this.xxbh = xxbh;
    }

    public String getKhbh() {
        return khbh;
    }

    public void setKhbh(String khbh) {
        this.khbh = khbh;
    }

    public String getZjlxmc() {
        return zjlxmc;
    }

    public void setZjlxmc(String zjlxmc) {
        this.zjlxmc = zjlxmc;
    }

    public String getGmffid() {
        return gmffid;
    }

    public void setGmffid(String gmffid) {
        this.gmffid = gmffid;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getMzdm() {
        return mzdm;
    }

    public void setMzdm(String mzdm) {
        this.mzdm = mzdm;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getJzdxzqhdm() {
        return jzdxzqhdm;
    }

    public void setJzdxzqhdm(String jzdxzqhdm) {
        this.jzdxzqhdm = jzdxzqhdm;
    }

    public String getJzdxxdz() {
        return jzdxxdz;
    }

    public void setJzdxxdz(String jzdxxdz) {
        this.jzdxxdz = jzdxxdz;
    }

    public String getHjxzqhdm() {
        return hjxzqhdm;
    }

    public void setHjxzqhdm(String hjxzqhdm) {
        this.hjxzqhdm = hjxzqhdm;
    }

    public String getHjdqhnxxdz() {
        return hjdqhnxxdz;
    }

    public void setHjdqhnxxdz(String hjdqhnxxdz) {
        this.hjdqhnxxdz = hjdqhnxxdz;
    }

    public Date getRzsj() {
        return rzsj;
    }

    public void setRzsj(Date rzsj) {
        this.rzsj = rzsj;
    }

    public String getRzfjh() {
        return rzfjh;
    }

    public void setRzfjh(String rzfjh) {
        this.rzfjh = rzfjh;
    }

    public String getRzblrxm() {
        return rzblrxm;
    }

    public void setRzblrxm(String rzblrxm) {
        this.rzblrxm = rzblrxm;
    }

    public Date getTfsj() {
        return tfsj;
    }

    public void setTfsj(Date tfsj) {
        this.tfsj = tfsj;
    }

    public String getTfblrxm() {
        return tfblrxm;
    }

    public void setTfblrxm(String tfblrxm) {
        this.tfblrxm = tfblrxm;
    }

    public String getLdbm() {
        return ldbm;
    }

    public void setLdbm(String ldbm) {
        this.ldbm = ldbm;
    }

    public String getLdmc() {
        return ldmc;
    }

    public void setLdmc(String ldmc) {
        this.ldmc = ldmc;
    }

    public String getLdxzqhdm() {
        return ldxzqhdm;
    }

    public void setLdxzqhdm(String ldxzqhdm) {
        this.ldxzqhdm = ldxzqhdm;
    }

    public String getLddzmc() {
        return lddzmc;
    }

    public void setLddzmc(String lddzmc) {
        this.lddzmc = lddzmc;
    }

    public Date getYwyksj() {
        return ywyksj;
    }

    public void setYwyksj(Date ywyksj) {
        this.ywyksj = ywyksj;
    }

    public String getSspcsxzqbdm() {
        return sspcsxzqbdm;
    }

    public void setSspcsxzqbdm(String sspcsxzqbdm) {
        this.sspcsxzqbdm = sspcsxzqbdm;
    }

    public String getScpcsmc() {
        return scpcsmc;
    }

    public void setScpcsmc(String scpcsmc) {
        this.scpcsmc = scpcsmc;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getFddbrxm() {
        return fddbrxm;
    }

    public void setFddbrxm(String fddbrxm) {
        this.fddbrxm = fddbrxm;
    }

    public String getSjklyxzqdm() {
        return sjklyxzqdm;
    }

    public void setSjklyxzqdm(String sjklyxzqdm) {
        this.sjklyxzqdm = sjklyxzqdm;
    }

    public Date getZhsj() {
        return zhsj;
    }

    public void setZhsj(Date zhsj) {
        this.zhsj = zhsj;
    }

    public Date getRksj() {
        return rksj;
    }

    public void setRksj(Date rksj) {
        this.rksj = rksj;
    }

    public String getSfzxpdbs() {
        return sfzxpdbs;
    }

    public void setSfzxpdbs(String sfzxpdbs) {
        this.sfzxpdbs = sfzxpdbs;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", xxbh='" + xxbh + '\'' +
                '}';
    }
}
