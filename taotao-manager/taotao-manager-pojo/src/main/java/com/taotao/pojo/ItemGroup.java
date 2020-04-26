package com.taotao.pojo;

import java.io.Serializable;
import java.util.List;

public class ItemGroup implements Serializable {
    private Integer id;
    private String groupName;
    private Long itemCatId;
    private List<ParamKeys> paramKeys;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getItemCatId() {
        return itemCatId;
    }

    public void setItemCatId(Long itemCatId) {
        this.itemCatId = itemCatId;
    }

    public List<ParamKeys> getParamKeys() {
        return paramKeys;
    }

    public void setParamKeys(List<ParamKeys> paramKeys) {
        this.paramKeys = paramKeys;
    }

    @Override
    public String toString() {
        return "ItemGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", itemCatId=" + itemCatId +
                ", paramKeys=" + paramKeys +
                '}';
    }
}
