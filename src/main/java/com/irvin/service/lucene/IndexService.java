package com.irvin.service.lucene;

import com.irvin.common.bean.Artical;
import com.irvin.lucene.ArticalIndex;
import com.irvin.lucene.IndexUtil;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author irvin
 * @date Create in 上午12:52 2017/8/5
 * @description
 */

@Service
public class IndexService {

    private static final Logger logger = LoggerFactory.getLogger(IndexService.class);

    public void indexFiles(List<Artical> articals, IndexWriter indexWriter) {
        logger.info("开始创建索引...");
        Long start = System.currentTimeMillis();
        articals.forEach(artical -> new ArticalIndex(indexWriter, artical).indexDoc());
        logger.info("创建索引结束 \n 创建索引用时:{}ms", System.currentTimeMillis() - start);
    }
}
