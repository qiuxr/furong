package com.rongfu.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rongfu.manager.service.ItemService;
import com.rongfu.common.CookieUtils;
import com.rongfu.manager.pojo.Cart;
import com.rongfu.manager.pojo.Item;

@Service
public class CartCookiesServiceImpl {

	@Autowired
	private ItemService itemService;

	@Value("${RONGFU_COOKIES_CART_KEY}")
	private String RONGFU_COOKIES_CART_KEY;

	private final ObjectMapper mapper = new ObjectMapper();

	public void saveCart(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
		// 处理用户是否存在相同商品的购物车
		List<Cart> carts = this.queryCartByUserId(request);
		Cart temp = null;
		for (Cart cart : carts) {
			// 如果找到相关商品，数量相加
			if (cart.getItemId().longValue() == itemId.longValue()) {
				cart.setNum(cart.getNum() + num);
				cart.setUpdated(new Date());
				temp = cart;
				break;
			}
		}
		// 如果当前商品不存在购物车中
		if (temp == null) {
			temp = new Cart();

			Item item = this.itemService.queryById(itemId);

			temp.setItemId(itemId);
			temp.setItemImage(item.getImages() == null ? "" : item.getImages()[0]);
			temp.setItemPrice(item.getPrice());
			temp.setItemTitle(item.getTitle());
			temp.setNum(num);
			temp.setCreated(new Date());
			temp.setUpdated(temp.getCreated());
			// 把商品购物车数据保存到集合中
			carts.add(temp);
		}
		try {
			String json = this.mapper.writeValueAsString(carts);
			CookieUtils.setCookie(request, response, RONGFU_COOKIES_CART_KEY, json, 60 * 60 * 24 * 3, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 跟据用户id搜索购物车列表
	 * 
	 * @return
	 */
	public List<Cart> queryCartByUserId(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, RONGFU_COOKIES_CART_KEY, true);
		if (StringUtils.isNotBlank(json)) {
			try {
				List<Cart> carts = mapper.readValue(json,
						mapper.getTypeFactory().constructCollectionType(List.class, Cart.class));
				return carts;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 返回不为null，交给调用的方法遍历
		return new ArrayList<>();
	}

	public void updateCart(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
		List<Cart> carts = this.queryCartByUserId(request);
		for (Cart cart : carts) {
			// 如果找到相关商品，数量相加
			if (cart.getItemId().longValue() == itemId.longValue()) {
				cart.setNum(num);
				cart.setUpdated(new Date());
				break;
			}
		}
		try {
			String json = this.mapper.writeValueAsString(carts);
			CookieUtils.setCookie(request, response, RONGFU_COOKIES_CART_KEY, json, 60 * 60 * 24 * 3, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
		List<Cart> carts = this.queryCartByUserId(request);
		for (Cart cart : carts) {
			// 如果找到相关商品，数量相加
			if (cart.getItemId().longValue() == itemId.longValue()) {
				// 删除相关商品的购物车
				carts.remove(cart);
				break;
			}
		}
		try {
			String json = this.mapper.writeValueAsString(carts);
			CookieUtils.setCookie(request, response, RONGFU_COOKIES_CART_KEY, json, 60 * 60 * 24 * 3, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
