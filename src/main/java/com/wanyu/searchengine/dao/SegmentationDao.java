package com.wanyu.searchengine.dao;

import com.wanyu.searchengine.entity.Segmentation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Classname: SegmentationDao
 * @author: wanyu
 * @Date: 2022/7/23 19:36
 */
@Mapper
public interface SegmentationDao {
    //查看所有分词
    List<Segmentation> selectAllSeg();
    //查询单个分词对应的id
    Segmentation selectOneSeg(String word);
    //查询相似词
    List<Segmentation> getAllByWords(String word);
}
