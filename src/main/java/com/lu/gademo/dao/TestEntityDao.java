package com.lu.gademo.dao;

import com.lu.gademo.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityDao extends JpaRepository<TestEntity, Integer> {

}
