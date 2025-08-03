package com.jp.login.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jp.login.entity.ContentItem;

@Mapper
public interface ContentItemMapper {
    List<ContentItem> findAll();
}
