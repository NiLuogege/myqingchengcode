package com.qingcheng.timer;

import com.qingcheng.util.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataUnit;

import java.util.Date;
import java.util.List;

@Component
public class SeckillCoodsTask {


    /**
     * 十五秒一次
     */
    @Scheduled(cron = "0/15 * * * * ?")
    public void loadGoodsPushRedis(){
        List<Date> dateMenus = DateUtil.getDateMenus();
        System.out.println(dateMenus.toString());
    }
}
