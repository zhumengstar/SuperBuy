package com.duckdream.superbuy.service;

import com.duckdream.superbuy.dao.GoodsDao;
import com.duckdream.superbuy.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVO> listGoodsVO() {
        return goodsDao.listGoodsVO();
    }


    public GoodsVO getGoodsVOByGoodsId(long goodsId) {
        return goodsDao.getGoodsVOByGoodsId(goodsId);
    }
}
