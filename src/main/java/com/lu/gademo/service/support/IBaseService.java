package com.lu.gademo.service.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

//封装通用的查询方法
public interface IBaseService<T, ID extends Serializable> {
    T find(ID id);

    List<T> findAll();

    List<T> findList(ID[] ids);

    List<T> findList(Iterable<ID> ids);

    Page<T> findAll(Pageable pageable);

    Page<T> findAll(Specification<T> spec, Pageable pageable);

    long count();

    long count(Specification<T> spec);

    boolean exists(ID id);

    void save(T entity);

    void save(Iterable<T> entitys);

    T update(T entity);

    void delete(ID id);

    void deleteByIds(@SuppressWarnings("unchecked") ID... ids);

    void delete(T[] entitys);

    void delete(Iterable<T> entitys);

    void delete(T entity);

    List<T> findList(Specification<T> spec, Sort sort);

}