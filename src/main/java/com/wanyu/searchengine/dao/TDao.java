package com.wanyu.searchengine.dao;

import com.wanyu.searchengine.entity.Record;
import com.wanyu.searchengine.entity.T;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname: TDao
 * @author: wanyu
 * @Date: 2022/7/22 18:13
 */

@Mapper
public interface TDao {
    boolean insert1(@Param("segs") List<String> segs);  // 添加分词表，这里是只添加了分词，严格来说这个应该放在 SegmentationDao 里
    boolean insert2(@Param("relations")List<T> relations, @Param("tableName")String tableName);  // 添加关系表
    int createNewTable(@Param("tableName")String tableName);
    List<Record> getRecordUseSplitFilter(@Param("info")String info, @Param("filterInfo")String filterInfo, @Param("pageSize")int pageSize, @Param("offset")int offset);
    int getRecordsNumFilter(@Param("info")String info, @Param("filterInfo")String filterInfo);
    List<Record> getRecordUseSplit(@Param("info")String info, @Param("pageSize")int pageSize, @Param("offset")int offset);
    int getRecordsNum(@Param("info")String info);

}
