package com.lu.gademo.dao.crm;

import com.lu.gademo.entity.crm.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTableDao extends JpaRepository<TestTable, Long> {

}
