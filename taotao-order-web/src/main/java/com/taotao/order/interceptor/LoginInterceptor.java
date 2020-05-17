package com.taotao.order.interceptor;

import com.taotao.constant.RedisConstant;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = CookieUtils.getCookieValue(httpServletRequest, RedisConstant.TT_TOKEN,true);
        String url = httpServletRequest.getRequestURL().toString();
        if(StringUtils.isBlank(token)){
            httpServletResponse.sendRedirect(RedisConstant.SSO_LOGIN_URL+"?redirectUrl="+url);
            return false;
        }

        TaotaoResult result = userService.getUserByToken(token);
        if(result.getStatus() != 200){
            httpServletResponse.sendRedirect(RedisConstant.SSO_LOGIN_URL+"?redirectUrl="+url);
            return false;
        }

        //走到这里，代表用户已经登陆
        TbUser tbUser = (TbUser) result.getData();
        httpServletRequest.setAttribute("user",tbUser);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
