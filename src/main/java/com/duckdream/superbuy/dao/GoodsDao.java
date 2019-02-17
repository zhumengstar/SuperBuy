package com.duckdream.superbuy.dao;

import com.duckdream.superbuy.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.ms_price,mg.stock_count,mg.start_date,mg.end_date from tb_goods_ms mg left join tb_goods g on mg.goods_id = g.id")
    public List<GoodsVO> listGoodsVO();

    @Select("select g.*,mg.ms_price,mg.stock_count,mg.start_date,mg.end_date from tb_goods_ms mg left join tb_goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVO getGoodsVOByGoodsId(@Param("goodsId")long goodsId);
}
