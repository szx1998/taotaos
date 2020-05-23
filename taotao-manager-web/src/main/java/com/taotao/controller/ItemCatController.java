package com.taotao.controller;

import com.taotao.pojo.StatisticsResult;
import com.taotao.pojo.ZtreeResult;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/itemCat")
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/showZtree")
    @ResponseBody
    public List<ZtreeResult> showZtree(@RequestParam(value = "id" ,defaultValue = "0") Long id){
        List<ZtreeResult> results = itemCatService.getZtreeResult(id);
        return results;
    }

    @RequestMapping("/statisticsItem")
    @ResponseBody
    public List<StatisticsResult> statisticsItem(){
        List<StatisticsResult> results = itemCatService.getStatisticList();
        return results;
    }

}
