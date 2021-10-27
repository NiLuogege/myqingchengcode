package com.qingcheng.service;

import com.qingcheng.dao.SeckillGoodsMapper;
import com.qingcheng.pojo.order.SeckillStatus;
import com.qingcheng.pojo.seckill.SeckillGoods;
import com.qingcheng.pojo.seckill.SeckillOrder;
import com.qingcheng.service.seckill.SeckillOrderService;
import com.qingcheng.task.MultiThreadingCreateOrder;
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

    @Autowired
    MultiThreadingCreateOrder multiThreadingCreateOrder;

    /**
     * 创建 秒杀订单
     *
     * @param id:商品ID
     * @param time:商品时区
     * @param username:用户名
     * @return
     */
    @Override
    public Boolean add(Long id, String time, String username) throws InterruptedException {

        //对用户下秒杀单次数 + 1 ，如果大于1 说明之前已经抢过了，不能再次参加
        Long userQueueCount = redisTemplate.boundHashOps("UserQueueStatus").increment(username, 1);
        if (userQueueCount>1){
            throw new RuntimeException("重复抢单");
        }

        //该缓存队列的 大小和 商品库存大小 是一致的
        Long size = redisTemplate.boundListOps("SeckillGoodsCountList_" + id).size();
        if (size<=0){
            throw new RuntimeException("售罄");
        }


        //创建秒杀队列 item 并入队
        SeckillStatus seckillStatus = new SeckillStatus(username,new Date(),1,id,time);
        redisTemplate.boundListOps("SeckillOrderQueue").leftPush(seckillStatus);

        //缓存用户对应的秒杀信息
        redisTemplate.boundHashOps("UserQueueStatus").put(username,seckillStatus);


        //开始异步创建订单
        multiThreadingCreateOrder.createOrder();
        return true;
    }
}
