package com.wanyu.searchengine.segmentation;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.wanyu.searchengine.utils.Keyword;
import com.wanyu.searchengine.utils.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Locale;

/**
 * @Classname: segmentationTest
 * @author: wanyu
 * @Date: 2022/7/19 17:37
 */

@SpringBootTest
public class segmentationTest {
    private JiebaSegmenter segmenter = new JiebaSegmenter();

    @Test
    public void test1() {
        String[] sentences =
                new String[]{
                        "美沃可视数码裂隙灯,检查眼前节健康状况",
                        "欧美夏季ebay连衣裙 气质圆领通勤绑带收腰连衣裙 zc3730"
                };
        for (String sentence : sentences) {
            long start = System.currentTimeMillis();
            List<SegToken> tokens = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH);
            System.out.println(tokens);
//            TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
//            List<Keyword> list=tfidfAnalyzer.analyze(sentence,10);
            long end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");

            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
            System.out.println();
            for (SegToken token : tokens) {
                System.out.println(token.word);
            }
        }
    }

    @Test
    public void test2() {
        String[] sentences =
                new String[]{"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH).toString());
        }
    }

    // 测试 tfidf
    @Test
    public void test3() {
        String sentence = "天气真好，我爱打篮球";
        TFIDFAnalyzer tfidfAnalyzer = new TFIDFAnalyzer();
        List<Keyword> list = tfidfAnalyzer.analyze(sentence, 5);
        for (Keyword word : list)
            System.out.print(word.getName() + ":" + word.getTfidfvalue() + ",");
    }


}
