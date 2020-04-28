package com.taotao.mapper;

import com.taotao.pojo.TbContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbContentMapper {
    int findContentByCount(@Param("categoryId") Long categoryId);

    List<TbContent> findContentByPage(@Param("categoryId") Long categoryId, @Param("index")int index, @Param("limit")Integer limit);

    int deleteContentByCategoryId(@Param("tbContents")List<TbContent> tbContents);
}