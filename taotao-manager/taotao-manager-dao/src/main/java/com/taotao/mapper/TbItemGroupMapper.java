package com.taotao.mapper;


import com.taotao.pojo.ItemGroup;
import com.taotao.pojo.ParamKeys;

import java.util.List;

public interface TbItemGroupMapper {
    List<ItemGroup> findGroupByCId(Long itemCatId);

    List<ParamKeys> findKeyByGroupId(Integer groupId);
}
