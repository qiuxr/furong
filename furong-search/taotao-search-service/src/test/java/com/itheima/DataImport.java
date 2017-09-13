package com.itheima;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.rongfu.manager.mapper.ItemMapper;
import com.rongfu.manager.pojo.Item;

public class DataImport {
	private ApplicationContext context;

	@Before
	public void init() {
		context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
	}

	@Test
	public void testImport() throws Exception{
		CloudSolrServer cloudSolrServer = context.getBean(CloudSolrServer.class);
		ItemMapper itemMapper = context.getBean(ItemMapper.class);

		// 分页查询商品信息
		int pageNum = 1, pageSize = 500;
		List<SolrInputDocument> docs = null;
		while (pageSize == 500) {
			//分页查询商品
			PageHelper.startPage(pageNum, pageSize);
			List<Item> list = itemMapper.select(null);
			SolrInputDocument doc = null;
			docs = new ArrayList<>();
			for (Item item : list) {
				doc = new SolrInputDocument();
				doc.addField("id", item.getId());
				// <field name="item_title" type="text_ik" indexed="true"
				doc.addField("item_title", item.getTitle());
				// stored="true" />
				// <field name="item_price" type="double" indexed="true"
				// stored="true" />
				doc.addField("item_price", item.getPrice());
				// <field name="item_image" type="string" indexed="false"
				// stored="true" />
				if(item.getImage() != null){
					doc.addField("item_image", StringUtils.split(item.getImage(), ",")[0]);
				}else{
					doc.addField("item_image", "");
				}
				// <field name="item_cid" type="long" indexed="false"
				// stored="true" />
				doc.addField("item_cid", item.getCid());
				// <field name="item_status" type="int" indexed="true"
				// stored="false" />
				doc.addField("item_status", item.getStatus());
				
				//保存数据到集合中
				docs.add(doc);
			}
			
			//保存索引并提交
			cloudSolrServer.add(docs);
			cloudSolrServer.commit();
			
			pageSize = list.size();
			
			System.out.println("正在导入第" + pageNum + "页数据，当前总共要导入" + pageSize + "条记录");
			
			pageNum++;
		}

	}
}
