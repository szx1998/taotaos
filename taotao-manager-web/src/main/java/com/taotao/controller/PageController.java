package com.taotao.controller;

import com.taotao.constant.RedisConstant;
import com.taotao.service.ItemService;
import com.taotao.service.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class PageController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private JedisClient jedisClient;

    @RequestMapping("/")
    public String showIndex(Model model){
        int num = itemService.fingUserAll();
        model.addAttribute("user_num",num);
        String visits = jedisClient.get(RedisConstant.PAGE_VISITSX);
        model.addAttribute("user_login_status",visits);
        String user_new_add = jedisClient.get(RedisConstant.USER_NEW_ADD);
        if (user_new_add == null){
            jedisClient.set(RedisConstant.USER_NEW_ADD,"0");
            user_new_add = jedisClient.get(RedisConstant.USER_NEW_ADD);
        }
        model.addAttribute("user_new_add",user_new_add);
        return "index";
    }
}
