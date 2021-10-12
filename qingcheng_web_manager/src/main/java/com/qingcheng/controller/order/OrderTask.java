package com.qingcheng.controller.order;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderTask {

    //一分钟一次
    @Scheduled(cron = "0 * * * * ?")
    public  void printTime(){
        System.out.println("Scheduled test = " + new Date());
    }

}
