package com.wanyu.searchengine.service.impl;

import com.wanyu.searchengine.entity.TreeNode;
import com.wanyu.searchengine.entity.User;
import com.wanyu.searchengine.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Classname: UserServiceImpl
 * @author: wanyu
 * @Date: 2022/7/24 21:34
 */

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String checkToken(String username) {
        return null;
    }

    @Override
    public boolean checkUserName(String username) {
        return false;
    }

    @Override
    public int register(User user) {
        return 0;
    }

    @Override
    public Map<String, String> login(User user) {
        return null;
    }

    @Override
    public User getUserByName(String username) {
        return null;
    }

    @Override
    public List<TreeNode> getFavorite(String username) {
        return null;
    }
}
