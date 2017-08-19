package com.irvin.service.sax;

import com.irvin.lucene.LuceneUtils;
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

/**
 * @author irvin
 * @date Create in 上午2:32 2017/8/9
 * @description
 */
@Service
public class SaxXMLReader {

	private static Logger logger = LoggerFactory.getLogger(SaxXMLReader.class);
	@Autowired
	IndexService indexService;
	@Autowired
	SaxXMLHandler saxXMLHandler;

	/**
	 * 解析 XML 文件
	 *
	 * @param path XML 文件路径
	 */
	public void xmlReader(String path) {

		logger.info("开始读取 xml 文件 并创建索引 ...");
		Long start = System.currentTimeMillis();
		logger.info("start currentTimeMillis:{}", start);

		SAXParser parser;
		try {
			//构建SAXParser
			parser = SAXParserFactory.newInstance().newSAXParser();
			//调用parse()方法
			parser.parse(new FileInputStream(new File(path)), saxXMLHandler);
			//遍历结果
			Long count = saxXMLHandler.getCount();
			logger.info("总文件数量:{}", count);
			logger.info("读文件用时:{}ms", System.currentTimeMillis() - start);

			//最后一部分数据,调 lucene 创建索引
			IndexWriter indexWriter = LuceneUtils.getIndexWriter("index/" + Thread.currentThread().getId());
			indexWriter.commit();
			indexWriter.close();
			logger.info("总用时:{}ms", System.currentTimeMillis() - start);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

}
