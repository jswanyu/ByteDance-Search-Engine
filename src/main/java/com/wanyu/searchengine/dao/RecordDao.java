package com.wanyu.searchengine.dao;

import com.wanyu.searchengine.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname: RecordDao
 * @author: wanyu
 * @Date: 2022/7/22 20:20
 */

@Mapper
public interface RecordDao {
    List<Record> selectPartialRecords(@Param("limit") int limit, @Param("offset") int offset);

}
