package com.taotao.mapper;


import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

public interface TbOrderMapper {
    int addOrder(TbOrder tbOrder);

    int addOrderItem(TbOrderItem orderItem);

    int addOrderShipping(TbOrderShipping orderShipping);
}