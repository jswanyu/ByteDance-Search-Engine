package com.wanyu.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 用户表，对应DB中user表
 * @Classname: User
 * @author: wanyu
 * @Date: 2022/7/19 16:13
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;

    private String username;

    private String password;

    private String token;

    //权限
    //权限默认为普通用户
    private Role role = Role.DEFAULT;
}
