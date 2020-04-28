package com.taotao.content.service.impl;

import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.content.service.ItemContentService;
import com.taotao.pojo.ZtreeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemContentServiceImpl implements ItemContentService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<ZtreeResult> getZtreeResult(Long id) {
        List<ZtreeResult> results = new ArrayList<ZtreeResult>();
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.findTbContentByParentId(id);
        for(TbContentCategory tbContentCategorie : tbContentCategories){
            ZtreeResult ztreeResult = new ZtreeResult();
            ztreeResult.setName(tbContentCategorie.getName());
            ztreeResult.setIsParent(tbContentCategorie.getIsParent());
            ztreeResult.setId(tbContentCategorie.getId());
            results.add(ztreeResult);
        }
        System.out.println(results);
        return results;
    }
}
