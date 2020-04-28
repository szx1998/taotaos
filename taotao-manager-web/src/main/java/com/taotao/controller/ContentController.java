package com.taotao.controller;

import com.taotao.pojo.LayuiResult;
import com.taotao.content.service.ItemContentService;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.ZtreeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ItemContentService contentService;

    @RequestMapping("/showContentZtree")
    @ResponseBody
    public List<ZtreeResult> showContentZtree(@RequestParam(value = "id" ,defaultValue = "0") Long id){
        List<ZtreeResult> results = contentService.getZtreeResult(id);
        return results;
    }

    @RequestMapping("/showContentTable")
    @ResponseBody
    public LayuiResult showContentTable(Long categoryId, Integer page,Integer limit){
        LayuiResult result = contentService.findContentByCategoryId(categoryId,page,limit);
        return result;
    }

    @RequestMapping("/deleteContentByCategoryId")
    @ResponseBody
    public LayuiResult deleteContentByCategoryId(@RequestBody List<TbContent> tbContents, @RequestParam(value = "page" ,defaultValue = "1")Integer page,@RequestParam(value = "limit" ,defaultValue = "10")Integer limit){
        LayuiResult result = contentService.deleteContentByCategoryId(tbContents,page,limit);
        return result;
    }
}
