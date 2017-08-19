package com.irvin.service.lucene;

import com.irvin.lucene.LuceneUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ReaderService {

	/**
	 * 多索引目录且多线程查询，异步收集查询结果
	 *
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	public static void multiThreadAndMultiReaderSearch() throws InterruptedException, ExecutionException, IOException {
		int count = 5;
		ExecutorService pool = Executors.newFixedThreadPool(count);

		Directory directory1 = LuceneUtils.openFSDirectory("C:/lucenedir1");
		Directory directory2 = LuceneUtils.openFSDirectory("C:/lucenedir2");
		Directory directory3 = LuceneUtils.openFSDirectory("C:/lucenedir3");
		Directory directory4 = LuceneUtils.openFSDirectory("C:/lucenedir4");
		Directory directory5 = LuceneUtils.openFSDirectory("C:/lucenedir5");
		IndexReader reader1 = DirectoryReader.open(directory1);
		IndexReader reader2 = DirectoryReader.open(directory2);
		IndexReader reader3 = DirectoryReader.open(directory3);
		IndexReader reader4 = DirectoryReader.open(directory4);
		IndexReader reader5 = DirectoryReader.open(directory5);
		MultiReader multiReader = new MultiReader(reader1, reader2, reader3, reader4, reader5);

		final IndexSearcher indexSearcher = new IndexSearcher(multiReader, pool);
		final Query query = new TermQuery(new Term("contents", "volatile"));
		List<Future<List<Document>>> futures = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			futures.add(pool.submit(() -> query(indexSearcher, query)));
		}

		int t = 0;
		//通过Future异步获取线程执行后返回的结果
		for (Future<List<Document>> future : futures) {
			List<Document> list = future.get();
			if (null == list || list.size() <= 0) {
				t++;
				continue;
			}
			for (Document doc : list) {
				String path = doc.get("path");
				//String content = doc.get("contents");
				System.out.println("path:" + path);
				//System.out.println("contents:" + content);
			}
			System.out.println("");
		}
		//释放线程池资源
		pool.shutdown();

		if (t == count) {
			System.out.println("No results.");
		}
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

	/**
	 * 多索引目录查询
	 *
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	public void multiReaderSearch(String rootPath, String queryStr) throws InterruptedException, ExecutionException, IOException, ParseException {

		QueryParser parser = new QueryParser("CONTENT", new StandardAnalyzer());
		Query query = parser.parse(queryStr);

		File file = new File(rootPath);
		// get the folder list
		File[] files = file.listFiles();
		assert files != null;

		List<IndexReader> readers = new ArrayList<>();
		for (File file1 : files)
			if (!file1.isFile())
				readers.add(DirectoryReader.open(LuceneUtils.openFSDirectory(file1.getPath())));

		MultiReader multiReader = new MultiReader(readers.toArray(new IndexReader[readers.size()]), true);
		IndexSearcher indexSearcher = new IndexSearcher(multiReader);
		List<Document> list = query(indexSearcher, query);
		if (null == list || list.size() <= 0) {
			System.out.println("No results.");
			return;
		}
		for (Document doc : list) {
			String path = doc.get("path");
			System.out.println("path:" + path);
		}
	}
}
