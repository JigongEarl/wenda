package com.nowcoder.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SearchService;
@Service
public class SearchServiceImpl implements SearchService, InitializingBean{

	@Autowired
	private QuestionService questionServiceImpl;
	
	private CloudSolrClient solrClient;

	@Override
	public void afterPropertiesSet() throws Exception {
		solrClient = new CloudSolrClient("192.168.241.135:2181,192.168.241.135:2182,192.168.241.135:2183");
		solrClient.setDefaultCollection("collection1");
	}
	
	public void init() {
		//先清空数据
		try {
			solrClient.deleteByQuery("*:*");
			solrClient.commit();
		
			//初始化数据
			List<Question> quesList = questionServiceImpl.selQuesAll();
			for(Question ques : quesList) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.setField("id", ques.getId() + 10000);
				doc.setField("wenda_ques_id", ques.getId());
				doc.setField("wenda_ques_title", ques.getTitle());
				doc.setField("wenda_ques_content", ques.getContent());
				doc.setField("wenda_ques_createdDate", ques.getCreatedDate());
				solrClient.add(doc);
				solrClient.commit();
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<Question> selByQuery(String query, int page, int rows, String hlPre, String hlPos) throws SolrServerException, IOException{
		
		//查询条件对象
		SolrQuery params = new SolrQuery();
		
		//分页条件
		params.setStart(rows * (page - 1));
		params.setRows(rows);
		
		//降序排序
		params.setSort("wenda_ques_createdDate", ORDER.desc);
		
		//搜索关键字
		params.setQuery("wenda_keywords:" + query);

		/*
		 * 设置高亮
		 * 1、打开高亮  2、设置高亮索引字段  3、设置高亮样式(html/css样式，会加在字段前后)
		 */
		params.setHighlight(true);
		params.setHighlightSimplePre(hlPre);
		params.setHighlightSimplePost(hlPos);
		
		//查询结果
		QueryResponse response = solrClient.query(params);

		//未高亮内容
		SolrDocumentList solrList = response.getResults();
	
		//高亮内容
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		//返回结果
		List<Question> questionList = new ArrayList<Question>();
		for(SolrDocument doc : solrList) {
			Question question = questionServiceImpl.selQuesById((int) doc.getFieldValue("wenda_ques_id"));
			List<String> highlights = highlighting.get(doc.getFieldValue("wenda_ques_id")).get("wenda_ques_title");
			if(highlights != null && highlights.size() > 0) {
				question.setTitle(highlights.get(0));
			}
			questionList.add(question);
		}
		return questionList;
	}
	
	
	public int add(Question ques) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", ques.getId() + 10000);
		doc.setField("wenda_ques_id", ques.getId());
		doc.setField("wenda_ques_title", ques.getTitle());
		doc.setField("wenda_ques_content", ques.getContent());
		doc.setField("wenda_ques_createdDate", ques.getCreatedDate());
		try {
			UpdateResponse add = solrClient.add(doc);
			solrClient.commit();
			if(add.getStatus() == 0) {
				return 1;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
}
