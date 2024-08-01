package com.lu.gademo.dao.split;

import com.lu.gademo.entity.split.SendSplitDesenData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendSplitDesenDataDao extends JpaRepository<SendSplitDesenData, String> {

}
