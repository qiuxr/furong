package com.rongfu.search.service.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rongfu.manager.mapper.ItemMapper;
import com.rongfu.manager.pojo.Item;

public class ItemListener implements MessageListener {
	
	private final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private CloudSolrServer solrServer;
	@Autowired
	private ItemMapper itemMapper;

	@Override
	public void onMessage(Message message) {
		if (message != null && message instanceof TextMessage) {
			try {
				//获取消息内容
				TextMessage msg = (TextMessage)message;
				
				String json = msg.getText();
				//直接读取json串
				JsonNode jsonNode = mapper.readTree(json);
				//获取操作类型
				String type = jsonNode.get("type").textValue();
				//如果为新增操作
				if("save".equals(type)){
					long itemId = jsonNode.get("itemId").asLong();
					//查询商品
					Item item = this.itemMapper.selectByPrimaryKey(itemId);
					
					SolrInputDocument doc = new SolrInputDocument();
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
					
					this.solrServer.add(doc);
					this.solrServer.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
