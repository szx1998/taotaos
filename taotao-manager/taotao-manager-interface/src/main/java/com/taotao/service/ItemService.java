package com.taotao.service;

import com.taotao.pojo.*;

import java.util.Date;
import java.util.List;

public interface ItemService {
    TbItem findTbItemById(Long itemId);

    LayuiResult findTbItemByPage(int page,int limit);

    TaotaoResult updateItem(List<TbItem> tbItems, int type, Date date);

    LayuiResult getLikeItem(Integer page, Integer limit, String title, Integer priceMin, Integer priceMax, Long cId);

    PictureResult addPicture(String Filename, byte[] bytes);

    TaotaoResult addItem(TbItem tbItem, String itemDesc,String[] paramKeyIds, String[] paramValue);

    TbItemDesc findTbItemDescByItemId(Long itemId);

    List<TbItemParamGroup> findTbItemGroupByItemId(Long itemId);
}
