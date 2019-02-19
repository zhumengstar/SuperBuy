package com.duckdream.superbuy.service;

import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.Order;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    @Select("select * from tb_order_ms where user_id = #{userId} and goods_id = #{goodsId}")
    public MsOrder getMsOrderByUserIdGoods(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into tb_order(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date) values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(Order orderinfo);

    @Insert("insert into tb_order_ms(user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{oederId})")
    public int insertMsOrder(MsOrder msOrder);

}
