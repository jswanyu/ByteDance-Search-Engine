package com.wanyu.searchengine.segmentation;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.wanyu.searchengine.ByteDanceSearchEngineApplication;
import com.wanyu.searchengine.dao.TDao;
import com.wanyu.searchengine.entity.Record;
import com.wanyu.searchengine.entity.Segmentation;
import com.wanyu.searchengine.entity.T;
import com.wanyu.searchengine.service.RecordService;
import com.wanyu.searchengine.service.SegmentationService;
import com.wanyu.searchengine.service.TService;
import com.wanyu.searchengine.utils.Keyword;
import com.wanyu.searchengine.utils.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;


/**
 * 扫描data表把所有内容分词并加入分词库
 *
 * @Classname: addAllSegmentation
 * @author: wanyu
 * @Date: 2022/7/22 18:08
 */

@SpringBootTest
public class addAllSegmentation {
    @Autowired
    private TService tService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private SegmentationService segmentationService;

    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
    TFIDFAnalyzer tfidfAnalyzer = new TFIDFAnalyzer();

    static HashSet<String> stopWordsSet;

    @Test
    // 先从data表里查所有数据，进行分词之后，向表segmentation添加分词，为关系表的建立做准备
    public void addAllSeg() {
        List<String> segs = new ArrayList<>();
        BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")), 10000000);
        if (stopWordsSet == null) { // 使用默认停词表
            stopWordsSet = new HashSet<>();
            loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));
        }
        for (int loop = 0; loop < 196; loop++) { // loop上限 = data表里的数据个数/10000
            // 每次去data表里查10000条数据，分页查询，有10000的offset就查10000，没有（即只剩下最后一页）就不用分页
            List<Record> records = recordService.selectPartialRecords(10000, Math.max(0, loop * 10000));
            if (loop % 10 == 0 && loop != 0) {   // 每读取10个10000条就先插入一下
                tService.insert1(segs);
                segs.clear();
            }
            for (int i = loop * 10000; i < (loop + 1) * 10000; i++) {
                // 最后的2000多条没插入成功，会报越界错误，因为data数据没有严格控制为整数，这里就不细改了
                Record record = records.get(i % 10000);
                String caption = record.getCaption();
                List<SegToken> segTokens = jiebaSegmenter.process(caption, JiebaSegmenter.SegMode.INDEX);
                for (SegToken segToken : segTokens) {
                    String word = segToken.word;
                    if (stopWordsSet.contains(word)) continue; // 是停词就不管
                    if (!bf.mightContain(word)) {              // 是正常分词就先用布隆过滤器判断，有就不用添加
                        bf.put(word);
                        segs.add(word);
                    }
                }
            }
        }
        tService.insert1(segs); // 最后剩下的也插入进去
    }

    @Test
    // 分表按照segId的最后两位来分，这样可以保证每个表是比较均匀的。
    // 因为在关系表很大的时候，主要的瓶颈在于找到所有包含某一个segId的data再将所有的tf值加起来比较大小
    public void addAllSegUseSplit() {
        List<Segmentation> segmentations = segmentationService.queryAllSeg();
        // wordToId：key--分词  value--分词对应的id    容量要符合查询到的分词总数
        Map<String, Integer> wordToId = new HashMap<>(800000);
        for (Segmentation seg : segmentations) {
            wordToId.put(seg.getWord(), seg.getId());
        }
        if (stopWordsSet == null) {
            stopWordsSet = new HashSet<>();
            loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));
        }
        // mp，key--分表序号  value--该表准备放的关联结构T集合
        Map<Integer, List<T>> mp = new HashMap<>(100000);
        int cnt = 0;  // 计数
        for (int loop = 0; loop < 196; loop++) {
            List<Record> records = recordService.selectPartialRecords(10000, Math.max(0, loop * 10000));
            // 这个for循环内处理每条语句，即对每条语句都执行以下操作：
            for (int i = loop * 10000; i < (loop + 1) * 10000; i++) {
                // 1. 获取整条语句
                Record record = records.get(i % 10000);
                String caption = record.getCaption();
                // 2. 获取整条语句中所有的分词
                List<SegToken> segTokens = jiebaSegmenter.process(caption, JiebaSegmenter.SegMode.INDEX);
                // 3. 获取整条语句中所有的分词的tfidf值
                List<Keyword> keywords = tfidfAnalyzer.analyze(caption, 5);  // 计算tfidf值
                // countMap：key--每条语句中的词 value--该词的关联结构
                Map<String, T> countMap = new HashMap<>();
                // 4. 处理每个分词，关键点是得到关联结构T中的4个属性
                for (SegToken segToken : segTokens) {
                    String word = segToken.word;
                    if (stopWordsSet.contains(word)) continue;  // 判断是否是停用词
                    // 4.1 segId: segmentation表中某个分词对应的id
                    int segId = wordToId.get(word);
                    // 4.2 dataId: data表中一条数据的id
                    int dataId = record.getId();
                    double tf = 0;
                    for (Keyword keyword : keywords) {
                        if (keyword.getName().equals(word)) {
                            // 4.3 tidif: 每个word的tfidf值  虽然是for循环，但匹配成功一次也就退出循环了
                            tf = keyword.getTfidfvalue();
                            break;
                        }
                    }
                    // 4.4 count: 计数变量，每条语句中，同一个词出现了几次
                    if (!countMap.containsKey(word)) {  // countMap中如果没有这个词，就添加进去，count设为1
                        int count = 1;
                        countMap.put(word, new T(dataId, segId, tf, count));
                    } else {                            // 如果有，就count++
                        T t = countMap.get(word);
                        int count = t.getCount();
                        t.setCount(++count);
                        countMap.put(word, t);
                    }
                }
                // 以上部分就把每条语句的所有分词处理结束了，下面开始准备分表

                // 5.得到每个词的关键结构T后，开始准备分表，一个表里要放多个关联结构T，存在list里
                // 分表按照segId的最后两位来分，这样可以保证每个表是比较均匀的
                // 这个for循环执行完，意味着本条语句中所有的词都已经处理完毕了，所有结果放到了mp里，key--分表序号  value--该表准备放的关联结构T集合
                for (T t : countMap.values()) {
                    int id = t.getSegId();
                    int idx = id % 100;
                    List list = mp.getOrDefault(idx, new ArrayList<>(10000));
                    list.add(t);
                    mp.put(idx, list);
                    cnt++; // 计数
                }
                // 6.每处理完100000个词之后，就开始往DB里添加，第一次添加时还需要创建表
                // 之所以这么搞，是因为在最后直接insert的话，会爆堆空间，虽然我已经开了4个G但好像还是不行。
                if (cnt > 100000) {
                    cnt = 0;
                    for (Integer idx : mp.keySet()) {
                        String tableName = "data_seg_relation_" + idx;
                        tService.createNewTable(tableName);
                        tService.insert2(mp.get(idx), tableName);
                    }
                    mp = new HashMap<>(100000);
                }
            }
        }
        // 最后还剩下一部分
        if (cnt > 0) {
            for (Integer idx : mp.keySet()) {
                String tableName = "data_seg_relation_" + idx;
                tService.createNewTable(tableName);
                tService.insert2(mp.get(idx), tableName);
            }
        }
    }

    // 加载停词表文件
    private void loadStopWords(Set<String> set, InputStream in) {
        BufferedReader bufr;
        try {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = bufr.readLine()) != null) {
                set.add(line.trim());
            }
            try {
                bufr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
