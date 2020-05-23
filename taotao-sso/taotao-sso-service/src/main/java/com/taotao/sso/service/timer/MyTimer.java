package com.taotao.sso.service.timer;

import com.taotao.constant.RedisConstant;
import com.taotao.mapper.TbUserMapper;
import com.taotao.sso.service.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class MyTimer {

    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    public void showTimer(){
        tbUserMapper.upDateStatusAll(0);
        jedisClient.set(RedisConstant.PAGE_VISITSX,"0");
        jedisClient.expire(RedisConstant.PAGE_VISITSX,RedisConstant.PAGE_VISITS_TIME);
        jedisClient.set(RedisConstant.USER_NEW_ADD,"0");
        jedisClient.expire(RedisConstant.USER_NEW_ADD,RedisConstant.PAGE_VISITS_TIME);
        System.out.println("定时器已触发"+new Date());
    }
}
