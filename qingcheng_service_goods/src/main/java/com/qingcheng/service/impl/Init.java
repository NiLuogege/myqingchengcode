package com.qingcheng.service.impl;

import com.qingcheng.service.goods.CategoryService;
import com.qingcheng.service.goods.SkuService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 实现 InitializingBean 就说明这个类要在spring启动的时候就加载
 */
@Component
public class Init implements InitializingBean {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuService skuService;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("---缓存预热----");
        categoryService.saveCategoryTreeToRedis();

    }
}
