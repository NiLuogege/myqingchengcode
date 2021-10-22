package com.qingcheng.service;

import com.qingcheng.dao.SeckillGoodsMapper;
import com.qingcheng.pojo.seckill.SeckillGoods;
import com.qingcheng.pojo.seckill.SeckillOrder;
import com.qingcheng.service.seckill.SeckillOrderService;
import com.qingcheng.util.CacheKeyString;
import com.qingcheng.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IdWorker idWorker;

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 创建 秒杀订单
     *
     * @param id:商品ID
     * @param time:商品时区
     * @param username:用户名
     * @return
     */
    @Override
    public Boolean add(Long id, String time, String username) {

        //从缓存中取出对应秒杀商品
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(CacheKeyString.seckill_goods + time).get(id);

        if (seckillGoods == null) {
            throw new RuntimeException("商品异常");
        }

        if (seckillGoods.getStockCount() <= 0) {
            throw new RuntimeException("没有库存了");
        }


        //创建订单并保存到缓存中
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(id);
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setUserId(username);
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");
        redisTemplate.boundHashOps("SeckillOrder").put(username, seckillOrder);

        //削减库存 并更新数据
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        if (seckillGoods.getStockCount() == 0) {//没有库存了，删除缓存数据，并同步到数据库
            redisTemplate.boundHashOps(CacheKeyString.seckill_goods + time).delete(id);
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
        } else {//更新数据
            redisTemplate.boundHashOps(CacheKeyString.seckill_goods + time).put(id, seckillGoods);
        }

        return true;
    }
}
