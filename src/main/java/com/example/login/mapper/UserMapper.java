package com.example.login.mapper;

import com.example.login.entity.MasterUser;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
        @Select("select count(id) from mst_user where username = #{username}")
        int existsById(String username);

        @Select("select * from mst_user where username = #{username}")
        MasterUser existsByUsername(String username);

        @Select("select count(id) from mst_user where ((bindingStatus = 1 and wikiStatus = 1) or (bindingStatus = 1)) and username = #{username}")
        int existsByBindingANDWikiStatus(String username);

        @Select("select count(id) from mst_user where wikiStatus = 1 and username = #{username}")
        int existsByWikiStatus(String username);

        @Update("update mst_user set wikiStatus = 0 where username = #{username}")
        void updateWikiStatus(String username);

        @Insert("insert into mst_user(username, password, role) values (#{username}, #{password}, 'ROLE_USER')")
        void insert(MasterUser username);

        @Select("select password from mst_user where username = #{username}")
        String getPassword(String username);
}
