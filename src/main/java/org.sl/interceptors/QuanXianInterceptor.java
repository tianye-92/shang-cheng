package org.sl.interceptors;

import org.sl.pojo.User;
import org.sl.util.RedisAPI;
import org.sl.util.Statics;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 权限拦截器（全局）
 * @author Administrator
 *
 */
public class QuanXianInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private RedisAPI redisAPI;

	@Override
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		//获取httpsession
		HttpSession session = request.getSession();
		//获取当前路径(getRequestURI:相对路径，getRequestURL:绝对路径)
		String urlPath = request.getRequestURI();
		//获取当前用户对象
		User user = (User) session.getAttribute(Statics.SESSION_USER);

		if(null == user){
			//如果当前用户为空，返回登录页面
			response.sendRedirect("/");
			return false;
		}else{
			//如果有用户，跳转到401页面
			//获取当前用户对应的key
			String keyString = "redis"+user.getRoleId()+"url";
			//获取当前用户对应的value
			String urlsString = redisAPI.get(keyString);
			//获取的value必须有值，并且获取的value里面包含当前页面url（urlsString.indexOf(urlPath)>0）
			if(null != urlsString && !"".equals(urlsString) && urlsString.indexOf(urlPath)>=0){
				return true;
			}else{
				response.sendRedirect("/user/401");
				return false;
			}
		}
	}
}
