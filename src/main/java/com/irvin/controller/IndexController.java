package com.irvin.controller;

import com.irvin.service.sax.SaxXMLReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author irvin
 * @date Create in 下午12:08 2017/8/12
 * @description
 */
@Api(value = "indexer", description = "indexer")
@RestController
public class IndexController {

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    SaxXMLReader saxXMLReader;

    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/index")
    public Long index(@RequestBody Map<String,String> map) {
        logger.info("IndexController -> index...");
        Long start = System.currentTimeMillis();
        saxXMLReader.xmlReader(map.get("path"));
        return System.currentTimeMillis() - start;
    }

}
