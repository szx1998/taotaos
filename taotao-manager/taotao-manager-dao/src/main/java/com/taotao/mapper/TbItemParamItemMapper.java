package com.taotao.mapper;

import org.apache.ibatis.annotations.Param;

public interface TbItemParamItemMapper {

    int addParameter(@Param("itemCatId")Long itemCatId, @Param("keyId")Integer keyId, @Param("value")String value);
}