package com.duckdream.superbuy.dao;

import com.duckdream.superbuy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Mapper
public interface UserDao {

    @Select("select * from tb_user where id = #{id}")
    public User getById(@Param("id") long id);
}
