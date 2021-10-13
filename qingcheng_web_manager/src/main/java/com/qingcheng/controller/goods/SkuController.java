package com.qingcheng.controller.goods;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.PageResult;
import com.qingcheng.entity.Result;
import com.qingcheng.pojo.goods.Sku;
import com.qingcheng.service.goods.SkuService;
import org.apache.http.HttpHost;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/sku")
public class SkuController {

    @Reference
    private SkuService skuService;

    @GetMapping("/findAll")
    public List<Sku> findAll(){
        return skuService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Sku> findPage(int page, int size){
        return skuService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Sku> findList(@RequestBody Map<String,Object> searchMap){
        return skuService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Sku> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  skuService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Sku findById(String id){
        return skuService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Sku sku){
        skuService.add(sku);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Sku sku){
        skuService.update(sku);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(String id){
        skuService.delete(id);
        return new Result();
    }

    @GetMapping("/putToEs")
    public Result putToEs(){
        //1.连接rest接口
//        HttpHost http=new HttpHost("127.0.0.1",9200,"http");
//        RestClientBuilder restClientBuilder = RestClient.builder(http);
//        RestHighLevelClient restHighLevelClient=new RestHighLevelClient(restClientBuilder);
//
        List<Sku> skus = skuService.findAll();
        for (Sku sku : skus) {

        }
        return new Result();
    }

}
