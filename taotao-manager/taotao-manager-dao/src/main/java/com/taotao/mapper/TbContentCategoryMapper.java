package com.taotao.mapper;


import com.taotao.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryMapper {
    List<TbContentCategory> findTbContentByParentId(Long id);
}