package com.duckdream.superbuy.dao;

import com.duckdream.superbuy.entity.MsOrder;
import com.duckdream.superbuy.entity.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    @Select("select * from tb_order_ms where user_id = #{userId} and goods_id = #{goodsId}")
    public MsOrder getMsOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into tb_orderinfo(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date) values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insertOrderInfo(OrderInfo orderinfo);

    @Insert("insert into tb_order_ms(user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    public int insertMsOrder(MsOrder msOrder);

    @Select("select * from tb_orderinfo where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId") long orderId);
}
