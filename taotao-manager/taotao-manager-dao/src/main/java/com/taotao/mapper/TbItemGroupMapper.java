package com.taotao.mapper;


import com.taotao.pojo.ItemGroup;
import com.taotao.pojo.ParamKeys;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbItemGroupMapper {
    List<ItemGroup> findGroupByCId(Long itemCatId);

    List<ParamKeys> findKeyByGroupId(Integer groupId);

    int addItemGroup(@Param("itemCatId") Long itemCatId, @Param("groupName")String groupName);

    int findItemGroupIdByIDAndName(@Param("itemCatId") Long itemCatId, @Param("groupName")String groupName);

    int addItemKey(@Param("groupId")Integer groupId, @Param("paramName") String paramName);
}
