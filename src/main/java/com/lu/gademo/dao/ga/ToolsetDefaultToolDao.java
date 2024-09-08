package com.lu.gademo.dao.ga;

import com.lu.gademo.entity.ga.ToolsetDefaultTool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolsetDefaultToolDao extends JpaRepository<ToolsetDefaultTool, Long> {
    ToolsetDefaultTool findByToolsetName(String toolsetName);
}