package com.jp.login.mapper;

import com.jp.login.entity.ContentItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContentItemMapper {
    List<ContentItem> findAll();
}
