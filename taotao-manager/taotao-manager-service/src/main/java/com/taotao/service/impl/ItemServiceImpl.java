package com.taotao.service.impl;

import com.taotao.constant.FTPConstant;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TbItem findTbItemById(Long itemId) {
        TbItem item = tbItemMapper.findTbItemById(itemId);
        return item;
    }

    @Override
    public LayuiResult findTbItemByPage(int page, int limit) {
        LayuiResult result = new LayuiResult();
        result.setCode(0);
        result.setMsg("");
        int count = tbItemMapper.findTbItemCount();
        result.setCount(count);
        List<TbItem> data = tbItemMapper.findTbItemByPage((page-1)*limit,limit);
        result.setData(data);
        return result;
    }

    @Override
    public TaotaoResult updateItem(List<TbItem> tbItems, int type, Date date) {

        if(tbItems.size() <= 0){
            return TaotaoResult.build(500,"请勾选商品",null);
        }
        List<Long> ids = new ArrayList<Long>();
        for (TbItem tbItem : tbItems) {
            ids.add(tbItem.getId());
        }

        int count = tbItemMapper.updateItemByIds(ids, type,date);
        if(count > 0 && type == 0){
            return TaotaoResult.build(200,"商品下架成功",null);
        }else if(count > 0 && type == 1){
            return TaotaoResult.build(200,"商品上架成功",null);
        }else if(count > 0 && type == 2){
            return TaotaoResult.build(200,"商品删除成功",null);
        }

        return TaotaoResult.build(500,"商品修改失败",null);
    }

    @Override
    public LayuiResult getLikeItem(Integer page, Integer limit, String title, Integer priceMin, Integer priceMax, Long cId) {
        if(priceMin == null){
            priceMin = 0;
        }
        if (priceMax == null){
            priceMax = 1000000000;
        }

        LayuiResult layuiResult = new LayuiResult();
        layuiResult.setCode(0);
        layuiResult.setMsg("");
        int count = tbItemMapper.findTbItemByLikeCount(title,priceMin,priceMax,cId);
        layuiResult.setCount(count);
        List<TbItem> tbItems = tbItemMapper.findTbItemByLike(title,priceMin,priceMax,cId,(page-1)*limit,limit);
        layuiResult.setData(tbItems);
        return layuiResult;
    }

    @Override
    public PictureResult addPicture(String filename, byte[] bytes) {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String filePath = format.format(new Date());
        String fileName = IDUtils.genImageName() + filename.substring(filename.indexOf("."));

        boolean b = FtpUtil.uploadFile(FTPConstant.FTP_ADDRESS,FTPConstant.FTP_PORT,FTPConstant.FTP_USERNAME,FTPConstant.FTP_PASSWORD,FTPConstant.FILI_UPLOAD_PATH,filePath,fileName,bis);

        if(b){
            PictureResult pictureResult = new PictureResult();
            pictureResult.setCode(0);
            pictureResult.setMsg("");
            pictureResult.setData(new PictureData(FTPConstant.IMAGE_BASE_URL+"/"+filePath+"/"+fileName));
            System.out.println(pictureResult.getData().getSrc());
            return pictureResult;
        }

        return null;
    }

    @Override
    public TaotaoResult addItem(TbItem tbItem, String itemDesc) {
        Date date = new Date();
        Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(date);
        tbItem.setUpdated(date);

        int i = tbItemMapper.addItem(tbItem);
        if(i <= 0){
            return TaotaoResult.build(500,"添加商品基本信息失败");
        }

        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        tbItemDesc.setItemDesc(itemDesc);

        int j = tbItemDescMapper.addItemDesc(tbItemDesc);
        if(j <= 0){
            return TaotaoResult.build(500,"添加商品描述信息失败");
        }

        return TaotaoResult.build(200,"商品添加成功");
    }

}
