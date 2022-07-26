package com.wanyu.searchengine.common;

import com.wanyu.searchengine.entity.Segmentation;
import com.wanyu.searchengine.service.SegmentationService;
import com.wanyu.searchengine.utils.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/** 将某些数据缓存到全局变量中
 * @Classname: CodeCache
 * @author: wanyu
 * @Date: 2022/7/25 16:35
 */

@Component
public class CodeCache {
    public static Trie trie = new Trie();

    @Autowired
    private SegmentationService segmentationService;

    @PostConstruct
    public void init() {  // 前缀树的初始化
        List<Segmentation> segmentations = segmentationService.queryAllSeg();
        for (int i = 0; i < segmentations.size(); i++) {
            String word = segmentations.get(i).getWord();
            trie.add(word);
        }
    }

}
