package com.lu.gademo.entity.crm;

import lombok.Data;
import java.util.List;

/**
 * 用于存储课题二类别名称与算法名称之间的关系
 */

@Data
public class TypeWithAlgoNames {
    private String typeName;
    private List<String> algNames;
}
