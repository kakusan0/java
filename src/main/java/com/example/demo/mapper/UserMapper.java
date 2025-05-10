package com.example.demo.mapper.post;

import com.example.demo.entity.MasterUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int existsByUsername(
            String username);

    int existsByUsernameDiscord(
            String username);

    MasterUser existsByUsernameAndPasswordAndId(
            String username);

    void insert(MasterUser username);


    void insertDiscord(String username);
}