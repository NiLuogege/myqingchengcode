package com.qingcheng.timer;

import com.qingcheng.dao.SeckillGoodsMapper;
import com.qingcheng.pojo.seckill.SeckillGoods;
import com.qingcheng.util.CacheKeyString;
import com.qingcheng.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataUnit;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class SeckillCoodsTask {


    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 十五秒一次
     */
    @Scheduled(cron = "0/15 * * * * ?")
    public void loadGoodsPushRedis() {
        List<Date> dateMenus = DateUtil.getDateMenus();
        System.out.println(dateMenus.toString());


        for (Date dateMenu : dateMenus) {

            String extName = DateUtil.date2Str(dateMenu);

            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            //必须审核通过
            criteria.andEqualTo("status", "1");
            //库存要大于o
            criteria.andGreaterThan("stockCount", 0);

            //开始时间大于等于 dateMenu
            criteria.andGreaterThanOrEqualTo("startTime", dateMenu);

            //结束时间小于等于 dateMenu+2
            criteria.andLessThanOrEqualTo("endTime", DateUtil.addDateHour(dateMenu, 2));


            //排除已经加入到缓存的 商品
            Set keys = redisTemplate.boundHashOps(CacheKeyString.seckill_goods + extName).keys();
            if (keys != null && keys.size() > 0) {
                criteria.andNotIn("id", keys);
            }


            //执行查询
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);


            //将秒杀商品 插入到缓存中
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps(CacheKeyString.seckill_goods + extName).put(seckillGood.getId(), seckillGood);
            }

        }
    }
}
