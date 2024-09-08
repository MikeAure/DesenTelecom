package com.lu.gademo.entity.ga;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "toolset_default_tool", schema = "ga")
public class ToolsetDefaultTool {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "toolsetName")
    private String toolsetName;

    @Column(name = "defaultAlgName")
    private String defaultAlgName;

}