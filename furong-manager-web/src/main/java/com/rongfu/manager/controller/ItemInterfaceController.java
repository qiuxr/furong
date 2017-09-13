package com.rongfu.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rongfu.manager.service.ItemService;
import com.rongfu.manager.pojo.Item;

@Controller
@RequestMapping("item/interface")
public class ItemInterfaceController {

	@Autowired
	private ItemService itemService;

	// 新增商品
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(Item item) {
		try {
			itemService.saveSelective(item);
			return ResponseEntity.status(HttpStatus.CREATED).body(null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果出错返回500状态码
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 修改
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> update(Item item) {
		try {
			this.itemService.updateByIdSelective(item);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			// return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果出错返回500状态码
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 查询
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ResponseEntity<Item> queryItemById(@PathVariable("id") Long id) {
		try {
			Item item = this.itemService.queryById(id);
			return ResponseEntity.ok(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果出错返回500状态码
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 删除
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteItemById(@PathVariable("id") Long id) {
		try {
			this.itemService.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果出错返回500状态码
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

}
