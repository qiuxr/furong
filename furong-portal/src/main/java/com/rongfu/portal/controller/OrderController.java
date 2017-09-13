package com.rongfu.portal.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongfu.order.service.OrderService;
import com.rongfu.cart.service.CartService;
import com.rongfu.manager.pojo.Cart;
import com.rongfu.manager.pojo.Order;
import com.rongfu.manager.pojo.User;
import com.rongfu.sso.service.UserService;

@Controller
@RequestMapping("order")
public class OrderController {
	@Autowired
	private UserService userService;

	@Value("${TAOTAO_COOKIES_TICKET}")
	private String TAOTAO_COOKIES_TICKET;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;

	/// order/create.html
	@RequestMapping("create")
	public String create(HttpServletRequest request,Model model) {
		// 验证用户是否登录
		//String ticket = CookieUtils.getCookieValue(request, TAOTAO_COOKIES_TICKET, true);
		//User user = this.userService.queryUserByTicket(ticket);
		User user = (User) request.getAttribute("user");
		// 如果用户已登录
		if (user != null) {
			List<Cart> carts = cartService.queryCartByUserId(user.getId());
			//返回购物车数据模型
			model.addAttribute("carts", carts);
		}
		return "order-cart";
	}
	
	@RequestMapping("submit")
	@ResponseBody
	public Map<String, Object> submit(Order order,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User user = (User) request.getAttribute("user");
			order.setUserId(user.getId());
			//保存订单
			String orderId = orderService.saveOrder(order);
			map.put("status", 200);
			map.put("data", orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping("success")
	public String success(String id,Model model){
		Order order = this.orderService.queryOrderById(id);
		//返回订单数据
		model.addAttribute("order", order);
		//计算预计送达时间
		String data = new DateTime().plusDays(3).toString("yyyy年MM月dd日 HH时mm分ss秒SSS毫秒");
		//返回预计送达时间表
		model.addAttribute("date", data);
		return "success";
	}
}
