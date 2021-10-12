package com.qingcheng.service.impl;

import com.qingcheng.pojo.goods.Goods;
import com.qingcheng.service.goods.SpuService;
import com.qingcheng.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;

public class SpuServiceImpl implements SpuService {

    @Autowired
    IdWorker idWorker;

    @Override
    public void saveGoods(Goods goods) {
        //获取 分布式id
        long id = idWorker.nextId();

    }
}
