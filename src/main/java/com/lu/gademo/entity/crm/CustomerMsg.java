package com.lu.gademo.entity.crm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer_msg", schema = "crm")
public class CustomerMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUST_ID", nullable = false)
    private Long id;

    @Column(name = "CUST_ADDR", length = 500)
    private String custAddr;

    @Column(name = "CUST_AREA_GRADE", length = 30)
    private String custAreaGrade;

    @Column(name = "CUST_CONTROL_LEVEL", length = 30)
    private String custControlLevel;

    @Column(name = "CUST_NAME", nullable = false, length = 250)
    private String custName;

    @Column(name = "CUST_TYPE", length = 30)
    private String custType;

    @Column(name = "CUST_GROUP", length = 10)
    private String custGroup;

    @Column(name = "MOBILE_PHONE", length = 30)
    private String mobilePhone;

    @Column(name = "CERT_TYPE", length = 30)
    private String certType;

    @Column(name = "CERT_NUM", length = 100)
    private String certNum;

    @Column(name = "FAX", length = 50)
    private String fax;

    @Column(name = "E_MAIL", length = 50)
    private String eMail;

    @Column(name = "POST_CODE", length = 20)
    private String postCode;

    @Column(name = "CREATE_STAFF", nullable = false)
    private Long createStaff;

    @Column(name = "CREATE_DATE")
    private Instant createDate;

    @Column(name = "UPDATE_STAFF")
    private Long updateStaff;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @Column(name = "STATUS_DATE")
    private LocalDateTime statusDate;

    @Column(name = "STATUS_CD", length = 10)
    private String statusCd;

    @Column(name = "REMARK")
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerMsg)) return false;
        CustomerMsg that = (CustomerMsg) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCustAddr(), that.getCustAddr()) && Objects.equals(getCustAreaGrade(), that.getCustAreaGrade()) && Objects.equals(getCustControlLevel(), that.getCustControlLevel()) && Objects.equals(getCustName(), that.getCustName()) && Objects.equals(getCustType(), that.getCustType()) && Objects.equals(getCustGroup(), that.getCustGroup()) && Objects.equals(getMobilePhone(), that.getMobilePhone()) && Objects.equals(getCertType(), that.getCertType()) && Objects.equals(getCertNum(), that.getCertNum()) && Objects.equals(getFax(), that.getFax()) && Objects.equals(eMail, that.eMail) && Objects.equals(getPostCode(), that.getPostCode()) && Objects.equals(getCreateStaff(), that.getCreateStaff()) && Objects.equals(getCreateDate(), that.getCreateDate()) && Objects.equals(getUpdateStaff(), that.getUpdateStaff()) && Objects.equals(getUpdateDate(), that.getUpdateDate()) && Objects.equals(getStatusDate(), that.getStatusDate()) && Objects.equals(getStatusCd(), that.getStatusCd()) && Objects.equals(getRemark(), that.getRemark());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCustAddr(), getCustAreaGrade(), getCustControlLevel(), getCustName(), getCustType(), getCustGroup(), getMobilePhone(), getCertType(), getCertNum(), getFax(), eMail, getPostCode(), getCreateStaff(), getCreateDate(), getUpdateStaff(), getUpdateDate(), getStatusDate(), getStatusCd(), getRemark());
    }

    @Override
    public String toString() {
        return "CustomerMsg{" +
                "id=" + id +
                ", custAddr='" + custAddr + '\'' +
                ", custAreaGrade='" + custAreaGrade + '\'' +
                ", custControlLevel='" + custControlLevel + '\'' +
                ", custName='" + custName + '\'' +
                ", custType='" + custType + '\'' +
                ", custGroup='" + custGroup + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", certType='" + certType + '\'' +
                ", certNum='" + certNum + '\'' +
                ", fax='" + fax + '\'' +
                ", eMail='" + eMail + '\'' +
                ", postCode='" + postCode + '\'' +
                ", createStaff=" + createStaff +
                ", createDate=" + createDate +
                ", updateStaff=" + updateStaff +
                ", updateDate=" + updateDate +
                ", statusDate=" + statusDate +
                ", statusCd='" + statusCd + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}