package com.qingcheng.controller.goods;


import com.qingcheng.entity.PageResult;
import com.qingcheng.pojo.goods.Brand;
import com.qingcheng.service.goods.BrandService;
import com.qingcheng.service.impl.BrandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return brandService.findPage(page,size);
    }


}
