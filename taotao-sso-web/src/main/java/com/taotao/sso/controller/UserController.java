package com.taotao.sso.controller;

import com.taotao.constant.RedisConstant;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.JedisClient;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
@Api(value = "User接口",tags = "UserController",description = "User接口")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/check/{param}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    @ApiOperation(value = "校验用户名，手机号，邮箱是否可用",notes = "校验用户名，手机号，邮箱是否可用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "用户信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "校验类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "callback", value = "jsonp类型", required = true, dataType = "String")
    })
    public Object cheakDate(@PathVariable String param, @PathVariable Integer type, String callback){
        TaotaoResult result = userService.getCheckDateResult(param,type);
        if(StringUtils.isNotBlank(callback)){
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
//            return callback+"("+JsonUtils.objectToJson(result)+")";
        }
        return JsonUtils.objectToJson(result);
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser){
        TaotaoResult result = userService.addUser(tbUser);
        return result;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String userName, String passWord, HttpServletRequest request, HttpServletResponse response){
        TaotaoResult result = userService.getTokenByNameAndPass(userName,passWord);
        String token = result.getData().toString();
        CookieUtils.setCookie(request,response, RedisConstant.TT_TOKEN, token);

        return result;
    }

    @RequestMapping(value = "/token/{token}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String token(@PathVariable String token,String callback){
        TaotaoResult result = userService.getUserByToken(token);
        if(StringUtils.isNotBlank(callback)){
//            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
//            mappingJacksonValue.setJsonpFunction(callback);
//            return mappingJacksonValue;
            return callback+"("+JsonUtils.objectToJson(result)+");";
        }
        return JsonUtils.objectToJson(result);
    }

    @RequestMapping(value = "/logout/{token}",method = RequestMethod.GET)
    public String logout(@PathVariable String token){
        TaotaoResult result = userService.deleteToken(token);

        return "login";
    }

}
