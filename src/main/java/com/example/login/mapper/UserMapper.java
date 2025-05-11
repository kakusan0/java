package com.example.login.mapper;

import com.example.login.entity.MasterUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select count(id) from mst_user where username = #{username}")
    int existsByUsername(
            String username);

    @Select("select count(id) from mst_user where discord_name = #{username}")
    int existsByUsernameDiscord(
            String username);

    @Select("select * from mst_user where username = #{username}")
    MasterUser existsByUsernameAndPasswordAndId(
            String username);

    @Insert("insert into mst_user(username, password, role) values (#{username}, #{password}, 'ROLE_USER')")
    void insert(MasterUser username);

    @Insert("insert into mst_user(username, role) values (#{username}, 'ROLE_USER')")
    void insertDiscord(String username);
}