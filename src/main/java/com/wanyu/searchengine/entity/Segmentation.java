package com.wanyu.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 分词实体类，对应DB中Segmentation表
 * @Classname: Segmentation
 * @author: wanyu
 * @Date: 2022/7/19 16:56
 */
@Data
@AllArgsConstructor
public class Segmentation {
    private int id;
    private String word;
}
