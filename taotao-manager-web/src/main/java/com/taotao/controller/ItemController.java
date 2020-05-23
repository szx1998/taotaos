package com.taotao.controller;

import com.taotao.pojo.LayuiResult;
import com.taotao.pojo.PictureResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    //我们的请求路径为http://localhost:8080/taotao-manager-web/item/参数(商品id)
    @RequestMapping("/{itemId}")
    @ResponseBody
    public TbItem findTbItemById(@PathVariable Long itemId){
        TbItem item = itemService.findTbItemById(itemId);
        return item;
    }

    @RequestMapping("/showItemPage")
    @ResponseBody
    public LayuiResult showItemPage(Integer page, Integer limit){
        LayuiResult result = itemService.findTbItemByPage(page, limit);
        return result;
    }
    @RequestMapping("/itemDelete")
    @ResponseBody
    public TaotaoResult itemDelete(@RequestBody List<TbItem> tbItems){
        Date date = new Date();
        TaotaoResult result = itemService.updateItem(tbItems,2,date);
        return result;
    }
    @RequestMapping("/commodityUpperShelves")
    @ResponseBody
    public TaotaoResult commodityUpperShelves(@RequestBody List<TbItem> tbItems){
        Date date = new Date();
        TaotaoResult result = itemService.updateItem(tbItems,1,date);
        return result;
    }

    @RequestMapping("/commodityLowerShelves")
    @ResponseBody
    public TaotaoResult commodityLowerShelves(@RequestBody List<TbItem> tbItems){
        Date date = new Date();
        TaotaoResult result = itemService.updateItem(tbItems,0,date);
        return result;
    }

    @RequestMapping("/searchItem")
    @ResponseBody
    public LayuiResult searchItem(Integer page, Integer limit, String title,Integer priceMin,Integer priceMax,Long cId){
        LayuiResult result = itemService.getLikeItem(page,limit,title,priceMin,priceMax,cId);
        return result;
    }
    @RequestMapping("/fileUpload")
    @ResponseBody
    public PictureResult fileUpload(MultipartFile file){
        try {
            PictureResult pictureResult = itemService.addPicture(file.getOriginalFilename(),file.getBytes());
            return pictureResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/addItem")
    @ResponseBody
    public TaotaoResult addItem(TbItem tbItem, String itemDesc, @RequestParam("paramKeyIds[]") String[] paramKeyIds, @RequestParam("paramValue[]")String[] paramValue){
        TaotaoResult result = itemService.addItem(tbItem,itemDesc, paramKeyIds,paramValue);
        return result;
    }
}
