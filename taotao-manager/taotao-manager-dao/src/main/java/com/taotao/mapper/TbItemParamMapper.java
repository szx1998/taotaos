package com.taotao.mapper;

import com.taotao.pojo.TbItemParamGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemParamMapper {

    int addParameter(@Param("itemId") Long itemId, @Param("keyId") Integer keyId, @Param("value") String value);
    
    List<TbItemParamGroup> findTbItemGroupByItemId(@Param("itemId") Long itemId);

}