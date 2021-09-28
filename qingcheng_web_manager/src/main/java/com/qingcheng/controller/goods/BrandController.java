package com.qingcheng.controller.goods;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/brand")
@RestController
public class BrandController {

    @GetMapping("/demo")
    public String demo() {
        return "hhjhhh";
    }
}
