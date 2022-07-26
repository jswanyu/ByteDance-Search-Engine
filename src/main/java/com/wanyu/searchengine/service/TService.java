package com.wanyu.searchengine.service;

import com.wanyu.searchengine.entity.T;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Classname: TService
 * @author: wanyu
 * @Date: 2022/7/22 18:23
 */


public interface TService {
    boolean insert1(List<String> segs);
    boolean insert2(List<T> relations, String tableName);
    int createNewTable(String tableName);
    Map<String, Object> getRcordUseSplit(String searchInfo, int pageSize, int pageNum);
}
