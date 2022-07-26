package com.wanyu.searchengine.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.wanyu.searchengine.dao.SegmentationDao;
import com.wanyu.searchengine.dao.TDao;
import com.wanyu.searchengine.entity.T;
import com.wanyu.searchengine.service.TService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Classname: TServiceImpl
 * @author: wanyu
 * @Date: 2022/7/22 18:24
 */

@Service
public class TServiceImpl implements TService {
    @Autowired
    private TDao tDao;
    @Autowired
    private SegmentationDao segmentationDao;

    @Override
    public boolean insert1(List<String> segs) {
        tDao.insert1(segs);
        return true;
    }

    @Override
    public boolean insert2(List<T> relations, String tableName) {
        tDao.insert2(relations, tableName);
        return true;
    }

    @Override
    public int createNewTable(String tableName) {
        tDao.createNewTable(tableName);
        return 0;
    }

    @Override
    public Map<String, Object> getRcordUseSplit(String searchInfo, int pageSize, int pageNum) {
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();
        JiebaSegmenter segmenter = new JiebaSegmenter();

        // 1.处理过滤词  start
        String[] words = searchInfo.split("\\s+"); // split("\s+")以空格、换行符、回车为分隔线，相邻的多个空格、换行符、回车仍然视为只有一个,分隔后返回字符数组
        List<String> filterWord = new ArrayList<>();
        boolean find = false;
        int filterWordIndex = -1;
        for (int i = 0; i < words.length; i++) {
            String str = words[i];
            if (Pattern.matches("^-.*?$", str)) {
                if (!find) {
                    filterWordIndex = searchInfo.indexOf(str);
                    find = true;
                }
                filterWord.add(str.substring(1));
            }
        }
        if (filterWordIndex != -1) { // 不等于-1说明有过滤词，需要截取搜索的内容。等于-1说明没有过滤词，进行下一步分词
            searchInfo = searchInfo.substring(0, filterWordIndex);
        }
        // 处理过滤词end

        // 2.分词
        List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.SEARCH);
        boolean first = true;
        // 3.对每个分词进行处理
        for (int i = 0; i < segTokens.size(); i++) {
            // 3.1 如果分词在整个分词表里没有，不管，看下一个分词
            if (segmentationDao.selectOneSeg(segTokens.get(i).word) == null) continue;
            // 3.2 分词为空，不管，看下一个分词
            if ("".equals(segTokens.get(i).word.trim())) continue;
            // 3.3 在整个分词表segmentation里查到了这个分词
            // 3.3.1 先获取它的id 作为 segId
            int segId = segmentationDao.selectOneSeg(segTokens.get(i).word).getId();
            // 3.3.2 准备去对应关联分表里查询，如果只有一个分词，就只查一次，多个分词就联合查询
            int idx = segId % 100;
            if (first) {
                sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                first = false;
            } else {
                sb.append("union").append('\n');
                sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
            }
        }
        // 搜索信息的分词处理结束

        // 4.对sql语句进行处理，主要是处理过滤词，把需要过滤的信息从结果中过滤走
        String info = sb.toString();
        String filterInfo = "";
        if ("".equals(info)) return null;
        boolean filterWordInSegmentation = false;
        // 4.1 如果有过滤词
        if (filterWord.size() > 0) {
            sb.delete(0, sb.length());
            boolean fi = true;
            for (int i = 0; i < filterWord.size(); i++) {
                if (segmentationDao.selectOneSeg(filterWord.get(i)) == null) continue;
                filterWordInSegmentation = true;
                int segId = segmentationDao.selectOneSeg(filterWord.get(i)).getId();
                int idx = segId % 100;
                if (fi) {
                    sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                    fi = false;
                } else {
                    sb.append("union").append('\n');
                    sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                }
            }
            filterInfo = sb.toString();
        }

        // 4.2 没有过滤词，就直接查前面拼接好的sql
        List records = null;
        int recordsNum = 0;
        if (filterWord.size() > 0 && filterWordInSegmentation) {
            // 有过滤词的查询
            // 对应mapper里的sql语句逻辑：先查所有的，再查过滤的，然后用 where not exists 过滤掉内容。以data_id进行分组查询（即一条caption），按照tidif值总和排序
            records = tDao.getRecordUseSplitFilter(info, filterInfo, pageSize, offset);
            // 查询过滤之后的数目
            recordsNum = tDao.getRecordsNumFilter(info, filterInfo);
        } else {
            // 没过滤的查询
            records = tDao.getRecordUseSplit(info, pageSize, offset);
            recordsNum = tDao.getRecordsNum(info);
        }
        Map<String, Object> mp = new HashMap<>();
        mp.put("recordsNum", recordsNum);
        mp.put("records", records);
        return mp;
    }
}
