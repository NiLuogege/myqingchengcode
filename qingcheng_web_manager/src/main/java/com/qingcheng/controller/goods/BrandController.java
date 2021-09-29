package com.qingcheng.controller.goods;


import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Brand;
import com.qingcheng.service.goods.BrandService;
import com.qingcheng.service.impl.BrandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/brand")
@RestController
public class BrandController {


    @Autowired
    BrandServiceImpl brandService;


    @GetMapping("/demo")
    public String demo() {
        return "hhjhhh";
    }

    @RequestMapping("/allBrand")
    public List<Brand> getAllBrand() {
        return brandService.getAllBrand();
    }


    @RequestMapping("/findAll")
    public List<Brand> findAll() {
        return brandService.findAll();
    }


    @GetMapping("/findPage")
    public PageResult<Brand> findPage(int page, int size) {
        return brandService.findPage(page, size);
    }

    @PostMapping("/findPage")
    public PageResult<Brand> findPage(@RequestBody Map searchMap, int page, int size) {
        return brandService.findPage(searchMap,page, size);
    }


}
