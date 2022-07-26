package com.wanyu.searchengine.service;

import com.wanyu.searchengine.entity.Record;

import java.util.List;

/**
 * @Classname: RecordService
 * @author: wanyu
 * @Date: 2022/7/22 20:23
 */
public interface RecordService {

    List<Record> selectPartialRecords(int limit, int offset);
}
