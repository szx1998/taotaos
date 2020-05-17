package com.taotao.cart.controller;

import com.taotao.constant.RedisConstant;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/add/{itemId}")
    public String addCart(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        List<TbItem> tbItems = getCartList(request);
        boolean flag = false;
        for(TbItem tbItem : tbItems){
            if(tbItem.getId() == itemId.longValue()){
                tbItem.setNum(tbItem.getNum()+num);
                flag = true;
                break;
            }
        }
        if(!flag){
            TbItem item = itemService.findTbItemById(itemId);
            String image = item.getImage();
            if(StringUtils.isNoneBlank(image)){
                String[] split = image.split("http://");
                String str = "http://" + split[1];
                item.setImage(str);
            }
            item.setNum(num);
            tbItems.add(item);
        }

        String cookie = CookieUtils.getCookieValue(request, RedisConstant.TT_TOKEN);
        CookieUtils.setCookie(request,response,RedisConstant.TT_CART,JsonUtils.objectToJson(tbItems),RedisConstant.CART_EXPIRE,true);

        return "cartSuccess";
    }

    @RequestMapping("/cart")
    public String showCartList(HttpServletRequest request, Model model){
        List<TbItem> cartList = getCartList(request);
        model.addAttribute("cartList",cartList);
        int sum = 0;
        for(TbItem tbItem : cartList){
            sum += tbItem.getNum();
        }
        model.addAttribute("sum",sum);
        return "cart";
    }
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateNum(@PathVariable Long itemId,@PathVariable Integer num, HttpServletRequest request, HttpServletResponse response, Model model){
        List<TbItem> cartList = getCartList(request);
        for(TbItem cart : cartList){
            if(cart.getId() == itemId.longValue()){
                cart.setNum(num);
            }
        }
        int sum = 0;
        for(TbItem tbItem : cartList){
            sum += tbItem.getNum();
        }
        model.addAttribute("sum",sum);
        CookieUtils.setCookie(request,response,RedisConstant.TT_CART,JsonUtils.objectToJson(cartList),RedisConstant.CART_EXPIRE,true);

        return TaotaoResult.ok();
    }

    @RequestMapping("/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        List<TbItem> cartList = getCartList(request);
        for(int i = 0; i < cartList.size();i++){
            TbItem item = cartList.get(i);
            if(item.getId() == itemId.longValue()){
                cartList.remove(item);
                break;
            }
        }
        CookieUtils.setCookie(request,response,RedisConstant.TT_CART,JsonUtils.objectToJson(cartList),RedisConstant.CART_EXPIRE,true);
        return "redirect:/cart/cart.html";
    }

    private List<TbItem> getCartList(HttpServletRequest request){
        String cookie = CookieUtils.getCookieValue(request, RedisConstant.TT_CART,true);
        if(StringUtils.isNotBlank(cookie)){
            return JsonUtils.jsonToList(cookie, TbItem.class);
        }
        return new ArrayList<TbItem>();
    }
}
