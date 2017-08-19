package com.irvin.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author irvin
 * @date Create in 下午11:14 2017/8/9
 * @description Lucene工具类
 */
public class LuceneUtils {


	/**
	 * 创建索引写入器
	 *
	 * @param indexPath 索引创建目录
	 * @return IndexWriter
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
		return new IndexWriter(dir, iwc);
	}

	/**
	 * 打开索引目录
	 *
	 * @param luceneDir
	 * @return
	 * @throws IOException
	 */
	public static FSDirectory openFSDirectory(String luceneDir) {
		FSDirectory directory = null;
		try {
			directory = FSDirectory.open(Paths.get(luceneDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return directory;
	}
}
