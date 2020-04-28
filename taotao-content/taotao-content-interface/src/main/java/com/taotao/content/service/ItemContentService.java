package com.taotao.content.service;

import com.taotao.pojo.LayuiResult;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.ZtreeResult;

import java.util.List;

public interface ItemContentService {
    List<ZtreeResult> getZtreeResult(Long id);

    LayuiResult findContentByCategoryId(Long categoryId, Integer page, Integer limit);

    LayuiResult deleteContentByCategoryId(List<TbContent> tbContents, Integer page, Integer limit);
}
