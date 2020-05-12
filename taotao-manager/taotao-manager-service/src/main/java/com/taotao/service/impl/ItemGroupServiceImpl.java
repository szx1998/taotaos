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
        if(groupList.size() <= 0){
            return TaotaoResult.build(500,"没有规格参数规格参数");
        }
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

    @Override
    public TaotaoResult addItemGroup(Long cId, String params) {
        String[] groups = params.split("clive");
        for(int i = 0; i < groups.length; i++){
            String[] keys = groups[i].split(",");
            int n = tbItemGroupMapper.addItemGroup(cId,keys[0]);
            if(n <= 0){
                return TaotaoResult.build(500,"添加规格参数组失败");
            }
            int id = tbItemGroupMapper.findItemGroupIdByIDAndName(cId,keys[0]);
            for(int j = 1; j < keys.length; j++){
                int m = tbItemGroupMapper.addItemKey(id,keys[j]);
                if(m <= 0){
                    return TaotaoResult.build(500,"添加规格参数项失败");
                }
            }
        }
        return TaotaoResult.build(200,"添加规格参数组成功");
    }
}
