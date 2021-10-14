package com.qingcheng.controller.goods;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.Result;
import com.qingcheng.service.goods.SkuSearchService;
import com.qingcheng.utils.WebUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class SearchController {

    @Reference
    private SkuSearchService skuSearchService;

    @GetMapping("/search")
    public Result search(@RequestParam Map<String, String> searchMap) throws Exception {
        //字符集处理(解决中文乱码问题)
        searchMap= WebUtil.convertCharsetToUTF8(searchMap);

        System.out.println("search params=" + searchMap.toString());
        return new Result(skuSearchService.search(searchMap));
    }

}
