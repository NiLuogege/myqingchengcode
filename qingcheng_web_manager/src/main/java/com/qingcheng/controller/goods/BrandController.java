package com.qingcheng.controller.goods;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/brand")
@RestController
public class BrandController {

    @RequestMapping("/demo")
    public String demo() {
        return "hhjhhh";
    }
}
