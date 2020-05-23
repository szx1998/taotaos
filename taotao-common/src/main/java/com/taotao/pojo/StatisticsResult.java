package com.taotao.pojo;

import java.io.Serializable;

public class StatisticsResult implements Serializable {
    private String name;
    private Integer value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StatisticsResult{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
