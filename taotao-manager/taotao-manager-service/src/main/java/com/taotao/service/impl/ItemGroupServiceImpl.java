package com.taotao.service.impl;

import com.taotao.mapper.TbItemGroupMapper;
import com.taotao.pojo.ItemGroup;
import com.taotao.pojo.ParamKeys;
import com.taotao.pojo.TaotaoResult;
import com.taotao.service.ItemGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemGroupServiceImpl implements ItemGroupService {
    @Autowired
    TbItemGroupMapper tbItemGroupMapper;

    @Override
    public TaotaoResult showItemGroup(Long cId) {
        List<ItemGroup> groupList = tbItemGroupMapper.findGroupByCId(cId);
        for (ItemGroup group: groupList) {
            List<ParamKeys> keys = tbItemGroupMapper.findKeyByGroupId(group.getId());
            group.setParamKeys(keys);
        }
        TaotaoResult result = new TaotaoResult();
        result.setStatus(200);
        result.setMsg("有规格参数");
        result.setData(groupList);

        return result;
    }
}
