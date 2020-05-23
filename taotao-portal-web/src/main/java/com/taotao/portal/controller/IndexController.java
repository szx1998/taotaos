package com.taotao.portal.controller;

import com.taotao.constant.RedisConstant;
import com.taotao.content.service.ItemContentService;
import com.taotao.pojo.Ad1Node;
import com.taotao.pojo.ItemCatResult;
import com.taotao.service.ItemCatService;
import com.taotao.service.JedisClient;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ItemCatService itemCatService;
    @Autowired
    private ItemContentService itemContentService;
    @Autowired
    private JedisClient jedisClient;

    @RequestMapping("/index")
    public List<Ad1Node> showIndex(Model model){
        List<Ad1Node> ad1Nodes = itemContentService.showAd1Node();
        String ad1 = JsonUtils.objectToJson(ad1Nodes);
        model.addAttribute("ad1",ad1);
        String visits = jedisClient.get(RedisConstant.PAGE_VISITS);
        if(StringUtils.isNotBlank(visits)){
            int i = Integer.parseInt(visits);
            i += 1;
            String s = Integer.toString(i);
            jedisClient.set(RedisConstant.PAGE_VISITS,s);
        }else{
            jedisClient.set(RedisConstant.PAGE_VISITS,"1");
            jedisClient.expire(RedisConstant.PAGE_VISITS,RedisConstant.PAGE_VISITS_TIME);
        }
        return ad1Nodes;
    }

    @RequestMapping("/itemCat/all")
    @ResponseBody
    public String showItemCat(){
        ItemCatResult result =itemCatService.showItemCat();
        String json = JsonUtils.objectToJson(result);
        return json;
    }
}
