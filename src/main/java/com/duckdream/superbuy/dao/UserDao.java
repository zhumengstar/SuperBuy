package com.duckdream.superbuy.dao;

import com.duckdream.superbuy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    @Select("select * from tb_user where id = #{id}")
    public User getById(@Param("id") long id);

<<<<<<< HEAD
    @Update("update tb_user set password = #{password} where id = #{id}")
    public void update(User toBeUpdate);
=======
>>>>>>> fae829b05de4bdcc9c877c3279d7a8d02ff8b9a8
}
