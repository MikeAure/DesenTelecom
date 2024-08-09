package com.lu.gademo.dao.crm;

import com.lu.gademo.entity.crm.CustomerMsg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerMsgDao extends JpaRepository<CustomerMsg, Long> {
}