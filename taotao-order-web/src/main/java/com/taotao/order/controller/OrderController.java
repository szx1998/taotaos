package com.taotao.order.controller;

import com.taotao.constant.RedisConstant;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.*;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order-cart")
    public String showOrderCart(HttpServletRequest request, Model model){
        List<TbItem> cartList = getCartList(request);
        model.addAttribute("cartList",cartList);

        return "order-cart";
    }

    private List<TbItem> getCartList(HttpServletRequest request){
        String token = CookieUtils.getCookieValue(request, RedisConstant.TT_CART,true);

        if(StringUtils.isNotBlank(token)){
            return JsonUtils.jsonToList(token,TbItem.class);
        }
        return new ArrayList<TbItem>();
    }

    @RequestMapping("/create")
    public String addOrder(HttpServletRequest request, Model model, OrderInfo orderInfo, String payment, Integer paymentType){
        TbUser user = (TbUser) request.getAttribute("user");
        TaotaoResult result = orderService.addOrder(orderInfo.getOrderItems(),orderInfo.getOrderShipping(),payment,paymentType,user);
        String orderId = result.getData().toString();

        //Service返回订单号
        model.addAttribute("orderId",orderId);
        model.addAttribute("payment",payment);

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        model.addAttribute("date",dateTime.toString("yyyy-MM-dd"));
        return "success";
    }
}
