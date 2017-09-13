package com.rongfu.item.jms;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rongfu.manager.pojo.Item;
import com.rongfu.manager.pojo.ItemDesc;
import com.rongfu.manager.service.ItemDescService;
import com.rongfu.manager.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemListener implements MessageListener {
	
	private final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemDescService descService;
	@Value("${RONGFU_ITEM_HTML_DIR}")
	private String RONGFU_ITEM_HTML_DIR;

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
					Item item = this.itemService.queryById(itemId);
					ItemDesc itemDesc = this.descService.queryById(itemId);
					
					// 1. 创建核心类Configuration-new
					Configuration  cfg = new Configuration(Configuration.VERSION_2_3_23);
					// 2.
					// 设置模板所在的位置,cfg.setDirectoryForTemplateLoading()，注意路径不应写死
					cfg.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + "/src/main/webapp/WEB-INF/ftl"));
					// 3.
					// 获取模板,参数是模板名称cfg.getTemplate，得到template，注意模板类型，
					//官方要求ftl后缀,实际上jsp,html,java,itcast...后缀都可以
					Template template = cfg.getTemplate("item.jsp");
					// 4. 指定数据模型，一般都用Map
					Map<String, Object> map = new HashMap<>();
					map.put("item", item);
					map.put("itemDesc", itemDesc);
					// 5. 指定输出位置new FileWriter
					Writer out = new FileWriter(new File(RONGFU_ITEM_HTML_DIR + item.getId() + ".html"));
					// 6. 使用模板输出template.process
					template.process(map, out);
					// 7. 关闭out
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
