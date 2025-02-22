package com.example.demo.mapper.post;

import com.example.demo.entity.post.ZipCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ZipcodeMapper {
    int countZipcodes(ZipCode example);
}