package com.wanyu.searchengine.service;

import com.wanyu.searchengine.entity.Segmentation;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * @Classname: SegmentationService
 * @author: wanyu
 * @Date: 2022/7/23 19:39
 */
public interface SegmentationService {
    List<Segmentation> queryAllSeg();
    List<String> getAllByWords(@Param("word") String word);
}
