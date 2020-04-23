package com.taotao.mapper;

import com.taotao.pojo.TbItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TbItemMapper {

	//查询数据库tbitem表中，根据商品id查询商品信息
	TbItem findTbItemById(Long ItemId);

	//查询数据库tbitem表中的总记录数
	int findTbItemCount();

	List<TbItem> findTbItemByPage(@Param("index") int index, @Param("pageSize") int pageSize);

	int updateItemByIds(@Param("ids") List<Long> ids,@Param("type") int type, @Param("date")Date date);
}