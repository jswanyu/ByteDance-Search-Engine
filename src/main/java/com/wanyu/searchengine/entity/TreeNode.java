package com.wanyu.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Classname: TreeNode
 * @author: wanyu
 * @Date: 2022/7/24 21:33
 */
@Data
@AllArgsConstructor
public class TreeNode {
    String id;
    String pid;
    String name;
    boolean isLeaf;
    String url;
}

