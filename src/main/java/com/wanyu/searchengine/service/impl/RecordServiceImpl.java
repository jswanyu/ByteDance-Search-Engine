package com.wanyu.searchengine.service.impl;

import com.wanyu.searchengine.dao.RecordDao;
import com.wanyu.searchengine.entity.Record;
import com.wanyu.searchengine.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname: RecordServiceImpl
 * @author: wanyu
 * @Date: 2022/7/22 20:24
 */

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordDao recordDao;


    @Override
    public List<Record> selectPartialRecords(int limit, int offset) {
        return recordDao.selectPartialRecords(limit, offset);
    }
}
