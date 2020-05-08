package com.taotao.search.controller;

import com.taotao.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller

public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String showSearch(@RequestParam("q") String query, @RequestParam(value = "page",defaultValue = "1")Integer page, Model model){

        try {
            String str = new String(query.getBytes("iso-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SearchResult result = searchService.findItemSearch(query,page);
        model.addAttribute("query",query);
        model.addAttribute("totalPage",result.getTotalPages());
        model.addAttribute("itemList",result.getItemList());
        model.addAttribute("totalCount",result.getTotalCount());
        model.addAttribute("page",page);
        return "search";
    }
}
