package com.qingcheng.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.pojo.seckill.SeckillGoods;
import com.qingcheng.service.seckill.SeckillGoodsService;

import java.util.List;

@Service(interfaceClass = SeckillGoodsService.class)
public class SeckillGoodsServiceImpl implements SeckillGoodsService {


    @Override
    public List<SeckillGoods> list(String time) {
        return null;
    }

    @Override
    public SeckillGoods one(String time, Long id) {
        return null;
    }

}
