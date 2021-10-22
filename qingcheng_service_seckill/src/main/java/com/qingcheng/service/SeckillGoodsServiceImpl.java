package com.qingcheng.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.pojo.seckill.SeckillGoods;
import com.qingcheng.service.seckill.SeckillGoodsService;
import com.qingcheng.util.CacheKeyString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service(interfaceClass = SeckillGoodsService.class)
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public List<SeckillGoods> list(String time) {
        return redisTemplate.boundHashOps(CacheKeyString.seckill_goods + time).values();
    }

    @Override
    public SeckillGoods one(String time, Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps(CacheKeyString.seckill_goods + time).get(id);
    }

}
