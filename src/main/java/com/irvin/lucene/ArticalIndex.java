package com.irvin.lucene;

import com.alibaba.fastjson.JSONArray;
import com.irvin.common.bean.Artical;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

/**
 * @author irvin
 * @date Create in 下午11:15 2017/8/9
 * @description
 */
public class ArticalIndex {

	private IndexWriter writer;
	private Artical t;

	public ArticalIndex(IndexWriter writer, Artical t) {
		this.writer = writer;
		this.t = t;
	}

	public void indexDoc() {
		Document doc = new Document();
		if (null != t.getDocNo())
			doc.add(new StringField("DOCNO", t.getDocNo(), Field.Store.YES));
		if (null != t.getBriefTitle())
			doc.add(new StringField("brief_title", t.getBriefTitle(), Field.Store.NO));
		if (null != t.getCondition())
			doc.add(new StringField("condition", t.getCondition(), Field.Store.NO));
		if (null != t.getInterventionType())
			doc.add(new StringField("intervention_type", t.getInterventionType(), Field.Store.NO));
		if (null != t.getInterventionName())
			doc.add(new StringField("intervention_name", t.getInterventionName(), Field.Store.NO));
		if (null != t.getDescription())
			doc.add(new StringField("description", t.getDescription(), Field.Store.NO));
		if (null != t.getGender())
			doc.add(new StringField("gender", t.getGender(), Field.Store.NO));
		if (null != t.getTitle())
			doc.add(new StringField("title", t.getTitle(), Field.Store.NO));
		if (null != t.getMinimumAge())
			doc.add(new StringField("minimum_age", t.getMinimumAge(), Field.Store.NO));
		if (null != t.getMaximumAge())
			doc.add(new StringField("maximum_age", t.getMaximumAge(), Field.Store.NO));
		if (null != t.getHealthyVolunteers())
			doc.add(new StringField("healthy_volunteers", t.getHealthyVolunteers(), Field.Store.NO));
//		if (null != t.getTextblocks())
//			doc.add(new TextField("textblock", t.getTextblocks().toString(), Field.Store.NO));
//		if (null != t.getMeshTerms())
//			doc.add(new TextField("mesh_term", t.getMeshTerms().toString(), Field.Store.NO));
		if (null != t.getContent())
			doc.add(new TextField("content", t.getContent(), Field.Store.NO));
		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
