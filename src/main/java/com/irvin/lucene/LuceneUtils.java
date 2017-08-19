package com.irvin.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author irvin
 * @date Create in 下午11:14 2017/8/9
 * @description Lucene工具类
 */
public class LuceneUtils {
	private volatile static IndexSearcher searcher;


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
			/**
			 * 注意：isLocked方法内部会试图去获取Lock,如果获取到Lock，会关闭它，否则return false表示索引目录没有被锁，
			 * 这也就是为什么unlock方法被从IndexWriter类中移除的原因
			 */
			IndexWriter.isLocked(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return directory;
	}

	/**
	 * 获取IndexSearcher(不支持多线程查询)
	 *
	 * @param reader IndexReader对象
	 * @return
	 */
	public static IndexSearcher getIndexSearcher(IndexReader reader) {
		if (null == reader) {
			throw new IllegalArgumentException("The indexReader can not be null.");
		}
		if (null == searcher) {
			searcher = new IndexSearcher(reader);
		}
		return searcher;
	}

	/**
	 * 获取IndexSearcher(多线程查询)
	 *
	 * @param reader   IndexReader对象
	 * @param executor
	 * @return
	 */
	public static IndexSearcher getIndexSearcher(IndexReader reader, ExecutorService executor) {
		if (null == reader) {
			throw new IllegalArgumentException("The indexReader can not be null.");
		}
		if (null == searcher) {
			searcher = new IndexSearcher(reader);
		}
		return searcher;
	}

	/**
	 * 索引文档查询
	 *
	 * @param searcher
	 * @param query
	 * @return
	 */
	public static List<Document> query(IndexSearcher searcher, Query query) {
		TopDocs topDocs;
		List<Document> docList = new ArrayList<>();
		try {
			topDocs = searcher.search(query, Integer.MAX_VALUE);
			ScoreDoc[] scores = topDocs.scoreDocs;
			int length = scores.length;
			if (length <= 0) {
				return Collections.emptyList();
			}
			for (ScoreDoc score : scores) {
				Document doc = searcher.doc(score.doc);
				docList.add(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docList;
	}
}
