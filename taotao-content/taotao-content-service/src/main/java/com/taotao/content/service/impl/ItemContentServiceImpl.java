package com.taotao.content.service.impl;

import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.*;
import com.taotao.content.service.ItemContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemContentServiceImpl implements ItemContentService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Autowired
    private TbContentMapper tbContentMapper;

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

    @Override
    public LayuiResult findContentByCategoryId(Long categoryId, Integer page, Integer limit) {
        LayuiResult result = new LayuiResult();
        result.setCode(0);
        result.setMsg("");
        int count = tbContentMapper.findContentByCount(categoryId);
        result.setCount(count);
        List<TbContent> data = tbContentMapper.findContentByPage(categoryId,(page-1)*limit,limit);
        result.setData(data);
        return result;
    }

    @Override
    public LayuiResult deleteContentByCategoryId(List<TbContent> tbContents, Integer page, Integer limit) {
        LayuiResult result = new LayuiResult();
        int i = tbContentMapper.deleteContentByCategoryId(tbContents);
        if(i<=0){
            return result;
        }
        Long categoryId = tbContents.get(0).getCategoryId();
        result.setCode(0);
        result.setMsg("");
        int count = tbContentMapper.findContentByCount(categoryId);
        if(count <= 0){
            return result;
        }
        result.setCount(count);
        List<TbContent> data = tbContentMapper.findContentByPage(categoryId,(page-1)*limit,limit);
        result.setData(data);
        return result;
    }

    @Override
    public LayuiResult addContent(TbContent tbContent, Integer page, Integer limit) {
        LayuiResult result = new LayuiResult();
        result.setCode(0);
        result.setMsg("");

        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.addContent(tbContent);
        Long categoryId = tbContent.getCategoryId();
        int count = tbContentMapper.findContentByCount(categoryId);
        result.setCount(count);
        List<TbContent> data = tbContentMapper.findContentByPage(categoryId,(page-1)*limit,limit);
        result.setData(data);
        return result;
    }

    @Override
    public List<Ad1Node> showAd1Node() {
        List<Ad1Node> ad1Nodes = new ArrayList<Ad1Node>();
        List<TbContent> contents = tbContentMapper.findContentByPage(89L, 0, 10);
        for (TbContent tbContent: contents ) {
            Ad1Node ad1Node = new Ad1Node();
            ad1Node.setSrcB(tbContent.getPic2());
            ad1Node.setHeight(240);
            ad1Node.setAlt(tbContent.getTitleDesc());
            ad1Node.setWidth(670);
            ad1Node.setSrc(tbContent.getPic());
            ad1Node.setHref(tbContent.getUrl());
            ad1Node.setHeightB(240);
            ad1Node.setWidthB(670);
            ad1Nodes.add(ad1Node);
        }

        return ad1Nodes;
    }
}
