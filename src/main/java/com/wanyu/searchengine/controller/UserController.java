package com.wanyu.searchengine.controller;

import com.wanyu.searchengine.dao.UserDao;
import com.wanyu.searchengine.entity.TreeNode;
import com.wanyu.searchengine.entity.User;
import com.wanyu.searchengine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname: UserController
 * @author: wanyu
 * @Date: 2022/7/24 21:31
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //判断用户名称是否存在
    @GetMapping(value = "/existUserName")
    public void existUserName(@RequestParam("username") String username){

    }

    //登录
    @CrossOrigin
    @PostMapping(value = "/user/login")
    @ResponseBody
    public Map<String, String> login(@RequestBody User user){
        return null;
    }

    //注册
    @PostMapping(value = "/register")
    @ResponseBody
    public Map<String, String> register(@RequestBody User user) {
        return null;
    }

    @CrossOrigin
    @GetMapping(value = "/user/logout")
    @ResponseBody
    public Map<String,String> logout(@RequestParam("username") String username,@RequestParam("token") String token){
        return null;
    }

    //判断token是否还存在
    @GetMapping(value = "/survival")
    @ResponseBody
    public Map<String,String> checkJJwt(@RequestParam("token") String token,@RequestParam("username") String username){
        return null;
    }

    @GetMapping("/getFavorites")
    @ResponseBody
    public List<TreeNode> getFavorite(@RequestParam("username") String username) {
        return null;
    }

    @PostMapping("/updateTreeNodeName")
    @ResponseBody
    public boolean updateTreeNodeNameById(@RequestBody Map<String,Object> params) {
        return true;
    }

    @PostMapping("/deleteTreeNode")
    @ResponseBody
    public boolean deleteTreeNodeById(@RequestBody Map<String, Object> params) {
        return true;
    }

    @PostMapping("/addTreeNode")
    @ResponseBody
    public boolean addTreeNode(@RequestBody Map<String, Object> params) {
        return true;
    }
}
