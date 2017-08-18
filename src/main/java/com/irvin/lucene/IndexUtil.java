package com.irvin.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author irvin
 * @date Create in 下午11:14 2017/8/9
 * @description
 */
public class IndexUtil {
    /**
     * 创建索引写入器
     *
     * @param indexPath
     * @return
     * @throws IOException
     */
    public static IndexWriter getIndexWriter(String indexPath) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexPath));

        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
        /*
        设置 segment 添加文档(Document)时的合并频率
        值较小,建立索引的速度就较慢
        值较大,建立索引的速度就较快,>10适合批量建立索引
        */
        mergePolicy.setMergeFactor(50);
        /*
        设置 segment 最大合并文档(Document)数
        值较小有利于追加索引的速度
        值较大,适合批量建立索引和更快的搜索
        */
        mergePolicy.setMaxMergeDocs(5000);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, iwc);
        return writer;
    }
}
