package com.taotao.service.impl;

import com.taotao.constant.FTPConstant;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

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

}
