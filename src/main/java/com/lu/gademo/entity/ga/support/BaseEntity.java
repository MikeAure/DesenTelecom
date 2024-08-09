package com.lu.gademo.entity.ga.support;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

//序列化
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -250118731239275742L;

}