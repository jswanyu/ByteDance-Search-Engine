package com.wanyu.searchengine.controller;

import com.wanyu.searchengine.common.CodeCache;
import com.wanyu.searchengine.service.RecordService;
import com.wanyu.searchengine.service.SegmentationService;
import com.wanyu.searchengine.service.TService;
import com.wanyu.searchengine.utils.Trie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Classname: RecordController
 * @author: wanyu
 * @Date: 2022/7/25 16:40
 */

@RestController
@Slf4j
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private SegmentationService segmentationService;
    @Autowired
    private TService tService;

    private final int pageSize = 15; // 每页显示15条

    // 搜索词关联
    @GetMapping("/prefix_word")
    public List<String> getPrefixWord(@RequestParam("word") String searchInfo) {
        Trie trie = CodeCache.trie;
        return trie.getRelatedWords(searchInfo);
    }

    @GetMapping("/search_use_split")
    public Map<String, Object> search_use_split(@RequestParam("word") String searchInfo, @RequestParam("pageNum") int pageNum) {
        return tService.getRcordUseSplit(searchInfo, pageSize, pageNum);
    }

    @GetMapping("/related_word")
    public List<String> relatedWord(@RequestParam("word") String searchInfo){
        return segmentationService.getAllByWords(searchInfo);
    }

}
