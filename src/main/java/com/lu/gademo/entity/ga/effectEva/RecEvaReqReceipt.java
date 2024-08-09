package com.lu.gademo.entity.ga.effectEva;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "rec_eva_req_receipt")
// 脱敏效果评测请求收据
public class RecEvaReqReceipt {
    @Id
    @Column(name = "certificateid")
    @NonNull
    private String certificateID;
    @Basic
    @Column(name = "evarequestid")
    private String evaRequestID;
    @Basic
    @Column(name = "hash")
    private String hash;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        RecEvaReqReceipt that = (RecEvaReqReceipt) o;
        return getCertificateID() != null && Objects.equals(getCertificateID(), that.getCertificateID());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
