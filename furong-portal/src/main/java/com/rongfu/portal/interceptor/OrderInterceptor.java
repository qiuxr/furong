package com.rongfu.portal.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.rongfu.common.CookieUtils;
import com.rongfu.manager.pojo.Cart;
import com.rongfu.manager.pojo.User;
import com.rongfu.sso.service.UserService;

/**
 * 订单拦截器
 * 
 * @author Steven
 *
 */
public class OrderInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;
	
	@Value("${RONGFU_COOKIES_TICKET}")
	private String RONGFU_COOKIES_TICKET;

	// 预处理方法，会在进入controller方法之前被调用
	// 只有return 为 true时，才能接着逻辑执行
	// 登录拦截器，权限管理
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 验证用户是否登录
		String ticket = CookieUtils.getCookieValue(request, RONGFU_COOKIES_TICKET, true);
		//进入拦截之前，用户将要跳转的操作
		String breakUrl = request.getRequestURL().toString();
		//如果ticket为空，说明用户跟本没登录过
		if(StringUtils.isBlank(ticket)){
			response.sendRedirect("/page/login.html?breakUrl=" + breakUrl);
		}else{
			User user = this.userService.queryUserByTicket(ticket);
			// 如果登录过期
			if (user == null) {
				response.sendRedirect("/page/login.html?breakUrl=" + breakUrl);
			}else{
				request.setAttribute("user", user);
			}
		}
		return true;
	}

	// 执行完controller方法并且在返回ModelAndView之前被调用
	// 页面共享的数据模型设置与清理
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	// 完全执行完controller方法之后并且preHandle返回了一个true时，被调用
	// 异常处理相关
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
