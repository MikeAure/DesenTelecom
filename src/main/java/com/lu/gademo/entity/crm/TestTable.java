package com.lu.gademo.entity.crm;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "test_table")
public class TestTable {
    @Id
    @Column(name = "id")
    Long id;

    @Basic
    @Column(name = "name")
    String name;

    @Basic
    @Column(name = "age")
    Integer age;

    public TestTable(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public TestTable() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestTable)) return false;
        TestTable testTable = (TestTable) o;
        return Objects.equals(getId(), testTable.getId()) && Objects.equals(getName(), testTable.getName()) && Objects.equals(getAge(), testTable.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAge());
    }

    @Override
    public String toString() {
        return "TestTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

