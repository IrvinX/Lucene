package com.irvin.service.sax;

import com.irvin.common.bean.Artical;
import com.irvin.lucene.IndexUtil;
import com.irvin.service.lucene.IndexService;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author irvin
 * @date Create in 上午2:24 2017/8/9
 * @description
 */
@Component
public class SaxXMLHandler extends DefaultHandler {

    private static Logger logger = LoggerFactory.getLogger(SaxXMLHandler.class);

    @Autowired
    IndexService indexService;

    public SaxXMLHandler() {
        this.fixedThreadPool = Executors.newFixedThreadPool(3);
        try {
            this.indexWriter = IndexUtil.getIndexWriter("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long getCount() {
        return count;
    }

    private static final int MAX_SIZE = 1024;
    //存放遍历集合
    private List<Artical> list;
    //Article 内部属性
    private List<String> meshTerms = new ArrayList<>();
    private List<String> textblocks = new ArrayList<>();
    //构建 Artical 对象
    private Artical artical;
    //用来存放每次遍历后的元素名称(节点名称)
    private String tagName;
    private Long count = 0L;
    private IndexWriter indexWriter;
    private ExecutorService fixedThreadPool;

    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public List<Artical> getList() {
        return list;
    }

    //只调用一次  初始化list集合
    @Override
    public void startDocument() throws SAXException {
        logger.debug("Start to analyse xml ...");
        list = new ArrayList<>();
    }


    //调用多次    开始解析
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equals("DOC")) {// 一个文档的开始
            artical = new Artical();
        }
        this.tagName = qName;
    }


    //调用多次
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("DOC")) {
            this.list.add(this.artical);
            count++;
        }

        if (MAX_SIZE == list.size()) {
            List<Artical> threadList = new ArrayList<>();
            threadList.addAll(list);
            list.clear();

            fixedThreadPool.execute(() -> {
                try {
                    // 调 lucene 创建索引
                    indexService.indexFiles(threadList, indexWriter);
                    indexWriter.commit();
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
//            IndexThread indexThread = new IndexThread(list);
//            indexThread.start();
        }
        this.tagName = null;
    }


    //只调用一次
    @Override
    public void endDocument() throws SAXException {
    }

    //调用多次
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.tagName != null) {
            String data = new String(ch, start, length);
            if (this.tagName.equals("DOCNO")) {
                this.artical.setDocNo(data);
            } else if (this.tagName.equals("brief_title")) {
                this.artical.setBriefTitle(data);
            } else if (this.tagName.equals("condition")) {
                this.artical.setCondition(data);
            } else if (this.tagName.equals("intervention_type")) {
                this.artical.setInterventionType(data);
            } else if (this.tagName.equals("intervention_name")) {
                this.artical.setInterventionName(data);
            } else if (this.tagName.equals("description")) {
                this.artical.setDescription(data);
            } else if (this.tagName.equals("arm_group_label")) {
                this.artical.setArmGroupLabel(data);
            } else if (this.tagName.equals("gender")) {
                this.artical.setGender(data);
            } else if (this.tagName.equals("minimum_age")) {
                this.artical.setMinimumAge(data);
            } else if (this.tagName.equals("maximum_age")) {
                this.artical.setMaximumAge(data);
            } else if (this.tagName.equals("healthy_volunteers")) {
                this.artical.setHealthyVolunteers(data);
            } else if (this.tagName.equals("mesh_term")) {
                meshTerms.add(data);
                this.artical.setMeshTerms(meshTerms);
            } else if (this.tagName.equals("textblock")) {
                textblocks.add(data);
                this.artical.setTextblocks(textblocks);
            }
        }
    }

//    public class IndexThread extends Thread {
//
//        private List<Artical> articals;
//
//        public IndexThread(List<Artical> articals) {
//            this.articals = articals;
//        }
//
//        @Override
//        public void run() {
//            // 调 lucene 创建索引
//            indexService.indexFiles(articals, indexWriter);
//            try {
//                indexWriter.commit();
//                indexWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}