package com.qingcheng.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.entity.PageResult;
import com.qingcheng.entity.Result;
import com.qingcheng.pojo.user.User;
import com.qingcheng.service.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @GetMapping("/findAll")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<User> findPage(int page, int size) {
        return userService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<User> findList(@RequestBody Map<String, Object> searchMap) {
        return userService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<User> findPage(@RequestBody Map<String, Object> searchMap, int page, int size) {
        return userService.findPage(searchMap, page, size);
    }

    @GetMapping("/findById")
    public User findById(String username) {
        return userService.findById(username);
    }


    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        userService.update(user);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(String username) {
        userService.delete(username);
        return new Result();
    }

    @GetMapping("/sendSms")
    public Result sendSms(String phone) {
        userService.sendSms(phone);
        return new Result();
    }


    @GetMapping("/add")
    public Result addUser(String phone, String password, String smsCode) {
        //密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = encoder.encode(password);

        User user = new User();
        user.setPhone(phone);
        user.setUsername(phone);
        user.setPassword(password);

        userService.add(user, smsCode);
        return new Result();
    }
}
