package com.taotao.service.impl;

import com.taotao.constant.FTPConstant;
import com.taotao.constant.RedisConstant;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.service.JedisClient;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination destination ;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public TbItem findTbItemById(Long itemId) {
        String json = jedisClient.get(RedisConstant.ITEM_INFO+":"+itemId);
        int rand = (int) (Math.random()*1000)+1;
        if(StringUtils.isNoneBlank(json)){
            if(json.equals("null")){
                return null;
            }else{
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                jedisClient.expire(RedisConstant.ITEM_INFO,RedisConstant.REDIS_TIME_OUT+rand);
                System.out.println("从缓存中获取数据");
                return tbItem;
            }
        }

        TbItem item = tbItemMapper.findTbItemById(itemId);
        if(item == null){
            jedisClient.set(RedisConstant.ITEM_INFO+":"+itemId, "null");
            jedisClient.expire(RedisConstant.ITEM_INFO+":"+itemId,RedisConstant.REDIS_TIME_OUT);
        }else{
            //把数据库得到的结果集存入缓存中
            jedisClient.set(RedisConstant.ITEM_INFO+":"+itemId, JsonUtils.objectToJson(item));
            jedisClient.expire(RedisConstant.ITEM_INFO+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
        }
        System.out.println("从数据库中获取数据");
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
            return pictureResult;
        }

        return null;
    }

    @Override
    public TaotaoResult addItem(TbItem tbItem, String itemDesc,String[] paramKeyIds, String[] paramValue) {
        Date date = new Date();
        final Long itemId = IDUtils.genItemId();
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
        int count = 0;
        for(int s = 0; s < paramKeyIds.length; s++){
            Integer keyId = Integer.valueOf(paramKeyIds[s]);
            String value = paramValue[s];
            count = tbItemParamMapper.addParameter(tbItem.getId(),keyId,value);
        }
        if(count <= 0){
            return TaotaoResult.build(500,"添加商品参数信息失败");
        }

        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(itemId+"");
                return textMessage;
            }
        });

        return TaotaoResult.build(200,"商品添加成功");
    }

    @Override
    public TbItemDesc findTbItemDescByItemId(Long itemId) {
        String json = jedisClient.get(RedisConstant.ITEM_DESC+":"+itemId);
        int rand = (int)(Math.random()*1000)+1;
        //当json不为null 有数据的时候
        if(StringUtils.isNotBlank(json)){
            if(json.equals("null")){
                return null;
            }else{
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);

                jedisClient.expire(RedisConstant.ITEM_DESC+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
                return itemDesc;
            }
        }

        TbItemDesc itemDesc = tbItemDescMapper.findTbItemDescByItemId(itemId);
        if(itemDesc==null){
            jedisClient.set(RedisConstant.ITEM_DESC+":"+itemId,"null");
            jedisClient.expire(RedisConstant.ITEM_DESC+":"+itemId,RedisConstant.REDIS_TIME_OUT);
        }else {
            //吧查询数据库得到的结果集存入到redis缓存中
            jedisClient.set(RedisConstant.ITEM_DESC+":"+itemId, JsonUtils.objectToJson(itemDesc));
            jedisClient.expire(RedisConstant.ITEM_DESC+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
        }
        return itemDesc;
    }

    @Override
    public List<TbItemParamGroup> findTbItemGroupByItemId(Long itemId) {
        String json = jedisClient.get(RedisConstant.ITEM_PARAM+":"+itemId);
        int rand = (int)(Math.random()*1000)+1;
        //当json不为null 有数据的时候
        if(StringUtils.isNotBlank(json)){
            if(json.equals("null")){
                return null;
            }else{
                List<TbItemParamGroup> groupList =  JsonUtils.jsonToPojo(json, List.class);

                jedisClient.expire(RedisConstant.ITEM_PARAM+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
                return groupList;
            }
        }
        List<TbItemParamGroup> groupList = tbItemParamMapper.findTbItemGroupByItemId(itemId);
        if(groupList==null){
            jedisClient.set(RedisConstant.ITEM_PARAM+":"+itemId,"null");
            jedisClient.expire(RedisConstant.ITEM_PARAM+":"+itemId,RedisConstant.REDIS_TIME_OUT);
        }else {
            //吧查询数据库得到的结果集存入到redis缓存中
            jedisClient.set(RedisConstant.ITEM_PARAM+":"+itemId, JsonUtils.objectToJson(groupList));
            jedisClient.expire(RedisConstant.ITEM_PARAM+":"+itemId,RedisConstant.REDIS_TIME_OUT+rand);
        }

        return groupList;
    }

}
