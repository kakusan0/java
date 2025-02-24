package com.example.demo.mapper.post;

import com.example.demo.entity.post.MasterUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select count(id) from users where username = #{username}")
    int existsByUsernameAndPassword(
            String username);

    @Select("select username, password from users where username = #{username}")
    MasterUser existsByUsernameAndPasswordAndId(
            String username);

    @Insert("insert into users (username, password) values (#{username}, #{password})")
    void insert(MasterUser user);

}