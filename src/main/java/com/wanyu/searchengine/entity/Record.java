package com.wanyu.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**  记录表，对应DB中data表
 * @Classname: Record
 * @author: wanyu
 * @Date: 2022/7/19 16:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    private Integer id;

    private String url;

    private String caption;
}
