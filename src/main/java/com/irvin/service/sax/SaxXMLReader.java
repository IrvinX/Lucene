package com.irvin.service.sax;

import com.irvin.common.bean.Artical;
import com.irvin.lucene.IndexUtil;
import com.irvin.service.lucene.IndexService;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author irvin
 * @date Create in 上午2:32 2017/8/9
 * @description
 */
@Service
public class SaxXMLReader {

    @Autowired
    IndexService indexService;
    @Autowired
    SaxXMLHandler saxXMLHandler;

    private static Logger logger = LoggerFactory.getLogger(SaxXMLReader.class);

    /**
     * 解析 XML 文件
     *
     * @param path XML 文件路径
     */
    public void xmlReader(String path) {

        logger.info("开始读取 xml 文件 ...");
        Long start = System.currentTimeMillis();

        SAXParser parser;
        try {
            //构建SAXParser
            parser = SAXParserFactory.newInstance().newSAXParser();
            //调用parse()方法
            parser.parse(new FileInputStream(new File(path)), saxXMLHandler);
            //遍历结果
            Long count = saxXMLHandler.getCount();
            logger.info("总文件数量:{}", count);
            List<Artical> list = saxXMLHandler.getList();

            logger.info("文件读取结束 \n 读文件 用时:{}ms", System.currentTimeMillis() - start);

            //最后一部分数据,调 lucene 创建索引
            IndexWriter indexWriter = saxXMLHandler.getIndexWriter();
            indexService.indexFiles(list, indexWriter);
            indexWriter.commit();
            indexWriter.close();

            logger.info("建索引 用时:{}ms", System.currentTimeMillis() - start);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

}
