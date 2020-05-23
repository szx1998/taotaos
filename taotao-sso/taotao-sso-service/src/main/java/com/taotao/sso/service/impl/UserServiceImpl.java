package com.taotao.sso.service.impl;

import com.taotao.constant.RedisConstant;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.JedisClient;
import com.taotao.sso.service.UserService;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult getCheckDateResult(String param, Integer type) {
        int i = 0;
        if(type == 1){
            i = tbUserMapper.checkUserName(param);
        }else if(type == 2){
            i = tbUserMapper.checkPhone(param);
        }else if(type == 3){
            i = tbUserMapper.checkEmail(param);
        }else{
            return TaotaoResult.build(500,"请输入合法的数字");
        }
        if(i!=0){
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult addUser(TbUser tbUser) {
        if(StringUtils.isBlank(tbUser.getUserName())){
            return TaotaoResult.build(500,"用户名不能为空");
        }
        if(StringUtils.isBlank(tbUser.getPassWord())){
            return TaotaoResult.build(500,"密码不能为空");
        }
        if(StringUtils.isBlank(tbUser.getPhone())){
            return TaotaoResult.build(500,"联系电话不能为空");
        }
        if(StringUtils.isBlank(tbUser.getEmail())){
            return TaotaoResult.build(500,"邮箱不能为空");
        }
        TaotaoResult result = getCheckDateResult(tbUser.getUserName(), 1);
        if(!(boolean)result.getData()){
            return TaotaoResult.build(500,"用户名已存在");
        }
        if(StringUtils.isNotBlank(tbUser.getPhone())){
            result = getCheckDateResult(tbUser.getPhone(), 2);
            if(!(boolean)result.getData()){
                return TaotaoResult.build(500,"联系电话已存在");
            }
        }
        if(StringUtils.isNotBlank(tbUser.getEmail())){
            result = getCheckDateResult(tbUser.getEmail(), 3);
            if(!(boolean)result.getData()){
                return TaotaoResult.build(500,"邮箱已存在");
            }
        }

        Date date = new Date();
        tbUser.setCreated(date);
        tbUser.setUpdated(date);
        tbUser.setStatus(0);
        //md5加密
        String md5Pass = DigestUtils.md5DigestAsHex(tbUser.getPassWord().getBytes());
        tbUser.setPassWord(md5Pass);
        int i = tbUserMapper.addUser(tbUser);
        if(i == 0){
            return TaotaoResult.build(500,"注册失败");
        }

        jedisClient.incr(RedisConstant.USER_NEW_ADD);

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult getTokenByNameAndPass(String userName, String passWord) {
        String token = UUID.randomUUID().toString();
        token = token.replace("-", "");
        int rand = (int) (Math.random()*100)+1;
        TbUser tbUser = tbUserMapper.findUserByUserNameAndPassWord(userName,DigestUtils.md5DigestAsHex(passWord.getBytes()));
        if(tbUser==null){
            jedisClient.set(RedisConstant.USER_INFO+":"+ token,"null");
            jedisClient.expire(RedisConstant.USER_INFO + ":" + token, RedisConstant.USER_SHORT_EXPIRE+rand);
            return TaotaoResult.build(500,"用户名或密码有误,请重新输入","null");
        }

        if(tbUser.getStatus() == 0){
            tbUserMapper.upDateStatus(1,tbUser.getId());
            jedisClient.incr(RedisConstant.PAGE_VISITSX);
        }

        tbUser.setPassWord(null);
        jedisClient.set(RedisConstant.USER_INFO+":"+ token, JsonUtils.objectToJson(tbUser));
        jedisClient.expire(RedisConstant.USER_INFO + ":" + token, RedisConstant.USER_SESSION_EXPIRE+rand);

        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        String json = jedisClient.get(RedisConstant.USER_INFO + ":" + token);
        if (StringUtils.isBlank(json)){
            return TaotaoResult.build(500,"用户登录已经过期，请重新登录");
        }
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
        return TaotaoResult.ok(tbUser);
    }

    @Override
    public TaotaoResult deleteToken(String token) {
        Long result = jedisClient.del(RedisConstant.USER_INFO + ":" + token);
        if (result <= 0){
            TaotaoResult.build(500,"退出失败");
        }
        return TaotaoResult.ok();
    }

}
