package com.wanyu.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 关联record和segmentation的实体
 * @Classname: RecordSeg
 * @author: wanyu
 * @Date: 2022/7/22 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordSeg {
    private Integer dataId;
    private Integer segId;
    private Double tidifValue;
    private Integer count;
}
