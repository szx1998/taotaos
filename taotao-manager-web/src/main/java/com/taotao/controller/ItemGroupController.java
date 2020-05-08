package com.taotao.controller;

import com.taotao.pojo.TaotaoResult;
import com.taotao.service.ItemGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/itemGroup")
public class ItemGroupController {
    @Autowired
    private ItemGroupService itemGroupService;

    @RequestMapping("/showItemGroup")
    @ResponseBody
    public TaotaoResult showItemGroup(Long cId){
        TaotaoResult result = itemGroupService.showItemGroup(cId);
        return result;
    }

    @RequestMapping("/addGroup")
    @ResponseBody
    public TaotaoResult addItemGroup(@RequestParam("cId")Long cId,@RequestParam("params")String params){
        TaotaoResult result = itemGroupService.addItemGroup(cId,params);
        return result;
    }
}
