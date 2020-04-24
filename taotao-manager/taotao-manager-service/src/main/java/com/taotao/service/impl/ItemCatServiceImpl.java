package com.taotao.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.ZtreeResult;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<ZtreeResult> getZtreeResult(Long id) {
        List<TbItemCat> tbItemCats = tbItemCatMapper.findTbItemCatByParentId(id);
        List<ZtreeResult> results = new ArrayList<ZtreeResult>();
        for (TbItemCat tbItemCat: tbItemCats) {
            ZtreeResult ztreeResult = new ZtreeResult();
            ztreeResult.setId(tbItemCat.getId());
            ztreeResult.setName(tbItemCat.getName());
            ztreeResult.setIsParent(tbItemCat.getIsParent());
            results.add(ztreeResult);
        }
        return results;
    }
}
