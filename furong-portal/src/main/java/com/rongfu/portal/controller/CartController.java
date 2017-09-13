package com.rongfu.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongfu.portal.service.impl.CartCookiesServiceImpl;
import com.rongfu.cart.service.CartService;
import com.rongfu.common.CookieUtils;
import com.rongfu.manager.pojo.Cart;
import com.rongfu.manager.pojo.User;
import com.rongfu.sso.service.UserService;

@Controller
@RequestMapping("cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartCookiesServiceImpl cartCookiesServiceImpl;

	@Value("${TAOTAO_COOKIES_TICKET}")
	private String TAOTAO_COOKIES_TICKET;

	// http://www.rongfu.com/cart/add/976898.html
	@RequestMapping("{itemId}")
	public String add(@PathVariable("itemId") Long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 验证用户是否登录
		String ticket = CookieUtils.getCookieValue(request, TAOTAO_COOKIES_TICKET, true);
		User user = this.userService.queryUserByTicket(ticket);
		// 如果用户已登录
		if (user != null) {
			this.cartService.saveCart(user.getId(), itemId, num);
		} else {
			// 末登录购物车添加
			this.cartCookiesServiceImpl.saveCart(itemId, num, request, response);
		}
		return "redirect:show.html";
	}

	@RequestMapping("show")
	public String show(HttpServletRequest request, Model model) {
		// 验证用户是否登录
		String ticket = CookieUtils.getCookieValue(request, TAOTAO_COOKIES_TICKET, true);
		User user = this.userService.queryUserByTicket(ticket);
		// 如果用户已登录
		if (user != null) {
			List<Cart> carts = this.cartService.queryCartByUserId(user.getId());
			model.addAttribute("cartList", carts);
		} else {
			// 末登录购物车添加
			List<Cart> carts = this.cartCookiesServiceImpl.queryCartByUserId(request);
			model.addAttribute("cartList", carts);
		}
		return "cart";
	}

	// http://www.rongfu.com/service/cart/update/num/976898/3
	@RequestMapping("update/num/{itemId}/{num}")
	@ResponseBody
	public String update(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 验证用户是否登录
		String ticket = CookieUtils.getCookieValue(request, TAOTAO_COOKIES_TICKET, true);
		User user = this.userService.queryUserByTicket(ticket);
		// 如果用户已登录
		if (user != null) {
			this.cartService.updateCart(user.getId(), itemId, num);
		} else {
			// 末登录购物车添加
			this.cartCookiesServiceImpl.updateCart(itemId, num, request, response);
		}

		return "";
	}

	// http://www.rongfu.com/cart/delete/976898.html
	@RequestMapping("delete/{itemId}")
	public String delete(@PathVariable("itemId") Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		// 验证用户是否登录
		String ticket = CookieUtils.getCookieValue(request, TAOTAO_COOKIES_TICKET, true);
		User user = this.userService.queryUserByTicket(ticket);
		// 如果用户已登录
		if (user != null) {
			this.cartService.deleteCart(user.getId(), itemId);
		} else {
			// 末登录购物车添加
			this.cartCookiesServiceImpl.deleteCart(itemId, request, response);
		}

		return "redirect:/cart/show.html";
	}
}
