package com.lu.gademo.entity.jmtLogStock;

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "t_user_info", schema = "jmt_log_stock")
public class TUserInfo {

    private Integer id;

    private String userId;

    private Integer authFlag;

    private String wxOpenId;

    private String name;

    private Integer sex;

    private Date birthday;

    private Integer inBeijing;

    private Integer statusType;

    private String createUser;

    private Date createTime;

    private String createApp;

    private String modifyUser;

    private Date modifyTime;

    private String modifyApp;

    private Integer delStat;

    private Integer versionNo;

    private Date lastLogin;

}