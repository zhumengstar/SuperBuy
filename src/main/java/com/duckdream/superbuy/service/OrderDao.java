package com.duckdream.superbuy.service;

import com.duckdream.superbuy.entity.MsOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderDao {

    @Select("select from tb_order_ms where user_id = #{userId} and goods_id = #{goodsId}")
    public MsOrder getMsOrderByUserIdGoods(@Param("userId") long userId, @Param("goodsId") long goodsId);
}
