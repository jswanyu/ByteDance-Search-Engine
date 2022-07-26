package com.wanyu.searchengine;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.wanyu.searchengine.dao.TDao;
import com.wanyu.searchengine.entity.Record;
import com.wanyu.searchengine.entity.T;
import com.wanyu.searchengine.service.RecordService;
import com.wanyu.searchengine.service.SegmentationService;
import com.wanyu.searchengine.service.TService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class ByteDanceSearchEngineApplicationTests {
    @Autowired
    private TService tService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private SegmentationService segmentationService;

    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

    @Test
    // 测试TService insert1
    public void TServiceTest() {
        List<String> segs = new ArrayList<>();
        String sentence = "我喜欢唱跳篮球";
        List<SegToken> segTokens = jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
        System.out.println(segTokens);
        for (SegToken segToken : segTokens) {
            String word = segToken.word;
            segs.add(word);
        }
        System.out.println(segs);
        tService.insert1(segs);
    }

    @Test
    // 测试TService createNewTable 和 insert2
    public void TServiceTest1() {
        HashMap<Integer, List<T>> listTMap = new HashMap<>(100000);
        ArrayList<T> list = new ArrayList<>();
        list.add(new T(8, 10, 0.33, 1));
        listTMap.put(10,list);
        String tableName = "data_seg_relation_" + 10;
        tService.createNewTable(tableName);
        tService.insert2(listTMap.get(10), tableName);
    }

    @Test
    // 测试RecordService
    public void recordServiceTest() {
        List<String> segs = new ArrayList<>();
        List<Record> records = recordService.selectPartialRecords(10, 0);
        for (int i = 0; i < 10; i++) {
            Record record = records.get(i % 10000);
            String caption = record.getCaption();
            List<SegToken> segTokens = jiebaSegmenter.process(caption, JiebaSegmenter.SegMode.INDEX);
            for (SegToken segToken : segTokens) {
                String word = segToken.word;
                System.out.println(word);
                segs.add(word);
            }
        }
        tService.insert1(segs);
    }

    @Test
    // 测试SegmentationService
    public void SegmentationServiceTest() {
        System.out.println(segmentationService.queryAllSeg());
    }


    @Test
    void contextLoads() {
    }


}
