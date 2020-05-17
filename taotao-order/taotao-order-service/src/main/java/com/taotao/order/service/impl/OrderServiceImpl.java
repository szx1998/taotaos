package com.taotao.order.service.impl;

import com.taotao.constant.RedisConstant;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.order.service.JedisClient;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult addOrder(List<TbOrderItem> orderItemList, TbOrderShipping orderShipping, String payment, Integer paymentType, TbUser tbUser) {
        TbOrder tbOrder = new TbOrder();
        if(jedisClient.exists(RedisConstant.ORDER_GET_KEY)){
            //设置初值
            jedisClient.set(RedisConstant.ORDER_GET_KEY,RedisConstant.ORDER_ID_BEGIN);
        }
        //设置orderId自增长+1
        String orderId = jedisClient.incr(RedisConstant.ORDER_GET_KEY).toString();

        tbOrder.setOrderId(orderId);
        //邮费
        tbOrder.setPostFee("0");
        //状态
        tbOrder.setStatus(1);
        Date date = new Date();
        tbOrder.setCreateTime(date);
        tbOrder.setUpdateTime(date);
        //设置用户id
        tbOrder.setUserId(tbUser.getId());
        //设置用户昵称
        tbOrder.setBuyerNick(tbUser.getUserName());
        //添加order表
        int i = tbOrderMapper.addOrder(tbOrder);
        if(i <= 0){
            return TaotaoResult.build(500,"订单生成失败");
        }

        //添加订单项表
        for(TbOrderItem orderItem : orderItemList){
            String orderItemId = jedisClient.incr(RedisConstant.ORDER_ITEM_ID_GEN_KEY).toString();
            orderItem.setOrderId(orderItemId);
            orderItem.setId(orderItemId);
            int j = tbOrderMapper.addOrderItem(orderItem);
            if(j <= 0){
                return TaotaoResult.build(500,"订单生成失败");
            }
        }

        //设置订单号
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);

        int x = tbOrderMapper.addOrderShipping(orderShipping);

        if(x <= 0){
            return TaotaoResult.build(500,"订单生成失败");
        }

        return TaotaoResult.ok(orderId);
    }
}
