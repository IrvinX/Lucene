package com.irvin.service.spark;

import com.irvin.Application;
import com.irvin.service.sax.SaxXMLReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author irvin
 * @date Create in 上午1:10 2017/8/5
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class FileReaderTest {
    private static final Logger logger = LoggerFactory.getLogger(FileReaderTest.class);

    @Autowired
    SaxXMLReader saxXMLReader;

    @Test
    public void readXML(){
        logger.info("FileReaderTest -> readXML started.");
        String path="/Users/irvin/Desktop/clinicalTrec1.xml";
        saxXMLReader.xmlReader(path);
        logger.info("FileReaderTest -> readXML finished.");
    }
}