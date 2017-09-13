package com.rongfu.search.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rongfu.common.TaoResult;
import com.rongfu.manager.pojo.Item;
import com.rongfu.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private CloudSolrServer solrServer;

	@Override
	public TaoResult<Item> search(String q, Integer page, Integer rows) {
		TaoResult<Item> result = new TaoResult<>();
		try {
			// 1. 创建搜索对象SolrQuery
			SolrQuery query = new SolrQuery();
			// 2. 设置查询条件(q,fq,)
			query.setQuery("item_title:" + q + " AND item_status:1");
			// query.setFilterQueries("title:我");
			// 3. 如有需要，设置排序(setSort("字段名", ORDER.asc))
			//query.setSort("id", ORDER.asc);
			// 4. 如有需要，设置分页(start与rows)
			query.setStart((page - 1) * rows);
			query.setRows(rows);
			// 5. 如有需要，设置fl(setFi..),设置df(set("df", "字段名")),设置wt(set("wt", "json"))
			//query.setFields("id,title");
			// query.set("df", "id");
			query.set("wt", "json");
			// 6. 如有需要，设置高亮(setHh,addHf,setHSP前后缀)
			query.setHighlight(true);
			// 设置高亮字段
			query.addHighlightField("item_title");
			// 设置高亮前缀
			query.setHighlightSimplePre("<em color='red'>");
			// 设置高亮后缀
			query.setHighlightSimplePost("</em>");
			// 7. 查询数据(server.query)
			QueryResponse response = this.solrServer.query(query);
			// 8. 获取文档列表(response.getResults)
			SolrDocumentList results = response.getResults();
			// 9. 输出总条数(results.getNumFound)
			long numFound = results.getNumFound();
			result.setTotal(numFound);
			// 获取高亮数据
			Map<String, Map<String, List<String>>> map = response.getHighlighting();
			// 10. 解析查询结果，遍历results，直接通过get("属性取值")
			List<Item> list = new ArrayList<>();
			Item item = null;
			for (SolrDocument doc : results) {
				item = new Item();
				item.setId(new Long(doc.get("id").toString()));
				//设置标题
				String hh = map.get(doc.get("id")).get("item_title").get(0);
				if(hh != null){
					item.setTitle(hh);
				}else{
					item.setTitle(doc.get("item_title").toString());
				}
				item.setPrice(new Double(doc.get("item_price").toString()));
				item.setImage(doc.get("item_image").toString());
				item.setCid(new Long(doc.get("item_cid").toString()));
				
				list.add(item);
			}
			result.setRows(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
