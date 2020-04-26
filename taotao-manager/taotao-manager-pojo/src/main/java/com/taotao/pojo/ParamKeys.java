package com.taotao.pojo;

import java.io.Serializable;

public class ParamKeys implements Serializable {
    private Integer id;
    private String paramName;
    private String groupId;
    private String paramGroup;
    private ItemGroup itemGroup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getParamGroup() {
        return paramGroup;
    }

    public void setParamGroup(String paramGroup) {
        this.paramGroup = paramGroup;
    }

    @Override
    public String toString() {
        return "ParamKeys{" +
                "id=" + id +
                ", paramName='" + paramName + '\'' +
                ", paramGroup='" + paramGroup + '\'' +
                '}';
    }
}
