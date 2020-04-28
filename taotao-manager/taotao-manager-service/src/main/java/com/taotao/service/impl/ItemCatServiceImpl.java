package com.taotao.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.*;
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

    @Override
    public ItemCatResult showItemCat() {
        ItemCatResult result = new ItemCatResult();
        result.setData(findItemCatList(0L));
        return result;
    }

    private List<?> findItemCatList(Long parentId){
        int count = 0;
        List list = new ArrayList();
        List<TbItemCat> tbItemCats = tbItemCatMapper.findTbItemCatByParentId(parentId);

        for(TbItemCat tbItemCat : tbItemCats){
            ItemCat itemCat = new ItemCat();
            if (tbItemCat.getIsParent()){
                if (tbItemCat.getParentId() == 0){
                    itemCat.setN("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
                    itemCat.setU("/products/"+tbItemCat.getId()+".html");
                    itemCat.setI(findItemCatList(tbItemCat.getId()));
                }else{
                    itemCat.setN(tbItemCat.getName());
                    itemCat.setU("/products/"+tbItemCat.getId()+".html");
                    itemCat.setI(findItemCatList(tbItemCat.getId()));
                }
                list.add(itemCat);
                count++;
                if(parentId == 0 && count >= 14){
                    break;
                }
            }else{
                list.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
            }
        }

        return list;
    }
}
