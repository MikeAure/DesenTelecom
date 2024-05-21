package com.lu.gademo.entity.effectEva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "rec_eva_req_receipt")
public class RecEvaReqReceipt {

    @Id
    @Column(name = "certificateid")
    private String certificateID;
    @Basic
    @Column(name = "evarequestid")
    private String evaRequestID;
    @Basic
    @Column(name = "hash")
    private String hash;

}
