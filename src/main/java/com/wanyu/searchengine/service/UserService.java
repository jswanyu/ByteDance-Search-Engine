package com.wanyu.searchengine.service;

import com.wanyu.searchengine.entity.TreeNode;
import com.wanyu.searchengine.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @Classname: UserService
 * @author: wanyu
 * @Date: 2022/7/24 21:32
 */
public interface UserService {

    String checkToken(String username);

    boolean checkUserName(String username);

    int register(User user);

    Map<String, String> login(User user);

    User getUserByName(String username);

    List<TreeNode> getFavorite(String username);
}
