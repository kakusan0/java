package com.jp.login.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.jp.login.entity.MstUser;

@Mapper
public interface UserMapper {
        @Select("select count(id) from mst_user where username = #{username}")
        int existsById(String username);

        @Select("select * from mst_user where username = #{username}")
        Optional<MstUser> existsByUsername(String username);

        @Insert("insert into mst_user(username, password, role) values (#{username}, #{password}, 'ROLE_USER')")
        void insert(MstUser user);

        @Select("select password from mst_user where username = #{username}")
        String getPassword(String username);
}
