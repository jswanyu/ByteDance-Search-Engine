package com.wanyu.searchengine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.wanyu.searchengine.dao")
public class ByteDanceSearchEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ByteDanceSearchEngineApplication.class, args);
    }

}
