package com.taotao.service;

import com.taotao.pojo.TaotaoResult;

public interface ItemGroupService {
    TaotaoResult showItemGroup(Long cId);

    TaotaoResult addItemGroup(Long cId, String params);
}
