package com.wanyu.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 关联record和segmentation的实体，猜测是之前的RecordSeg弃用了，就弄了个新的
 * @Classname: T
 * @author: wanyu
 * @Date: 2022/7/22 18:05
 */
@Data
@AllArgsConstructor
public class T {
    Integer dataId;
    Integer segId;
    Double tidif;
    Integer count;
}
