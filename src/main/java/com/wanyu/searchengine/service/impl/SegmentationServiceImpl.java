package com.wanyu.searchengine.service.impl;

import com.wanyu.searchengine.dao.SegmentationDao;
import com.wanyu.searchengine.entity.Segmentation;
import com.wanyu.searchengine.service.SegmentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Classname: SegmentationServiceImpl
 * @author: wanyu
 * @Date: 2022/7/23 19:40
 */

@Service
public class SegmentationServiceImpl implements SegmentationService {
    @Autowired
    private SegmentationDao segmentationDao;

    @Override
    public List<Segmentation> queryAllSeg() {
        return segmentationDao.selectAllSeg();
    }

    @Override
    public List<String> getAllByWords(String word) {
        List<Segmentation> segmentation = segmentationDao.getAllByWords(word + "%");
        List<String> newList = new ArrayList<>();
        System.out.println(segmentation.size());
        if(segmentation.size() >= 8){
            for(int i=0;i<8;i++) {
                Random random = new Random();
                int index = random.nextInt(segmentation.size());
                newList.add(segmentation.get(index).getWord());
                segmentation.remove(index);
            }
        }else{
            for (Segmentation segmentation1 : segmentation) {
                newList.add(segmentation1.getWord());
            }
        }
        return newList;
    }
}
