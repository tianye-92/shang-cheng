package org.sl.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.sl.pojo.*;
import org.sl.service.*;
import org.sl.util.RedisAPI;
import org.sl.util.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 登录控制器
 * @author ty
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private FunctionService functionService;
	@Autowired
	private RedisAPI redisAPI;
	@Resource
	private ZiDianService ziDianSerivce;
	@Resource
	private GongGaoService gongGaoService;
	@Resource
	private ZiXunService ziXunService;

	/**
	 * 登录进入的方法
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/checkLogin")
	@ResponseBody
	public Object checkLogin(HttpSession session, @RequestParam String user){
		if(user == null || "".equals(user)){
			//如果拿到的值为空，直接返回
			return "nodata";
			//model.setViewName("nodata");
		}else{
			//拿到值了
			JSONObject userObject = JSONObject.fromObject(user);
			User userBean = (User)JSONObject.toBean(userObject, User.class);
			try {
				if(userService.getUserByName(userBean)==0){
					//数据库没有这个名字
					return "nofind";
					//model.setViewName("nofind");
				}else{
					//数据库有这个名字
					User lastUser = userService.getUser(userBean);
					if(lastUser == null ){
						//密码错误
						return "pwderror";
						//model.setViewName("pwderror");
					}else{
						//登录成功，并存到session中
						session.setAttribute(Statics.SESSION_USER, lastUser);
						//更新当前用户登录的最后登录时间（lastLoginTime）
						User upUser = new User();  //新建一个对象
						upUser.setId(lastUser.getId());  //赋值查到对象的id
						upUser.setLastLoginTime(new Date());  //赋值当前时间
						userService.updateUser(upUser);  //修改该对象的最后登录时间
						upUser=null;  //释放该对象
						return "success";
						//model.addObject(Statics.SESSION_USER, lastUser);
						//model.setViewName("user/success");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "error";
				//model.setViewName("error");
			}
		}
	}

	/**
	 * 登录成功之后进入的方法
	 * @return
	 */
	@RequestMapping(value="/success")
	public ModelAndView loginSuccess(HttpSession session){
		//使用父类获取session中的当前用户
		User user = this.getSuccessUser();
		//创建一个菜单集合
		List<Menu> mList = null;
		if(null != user){
			//创建一个map集合，为之后其他页面拿到菜单列表
			Map<String, Object> modelMap = new HashMap<String, Object>();
			modelMap.put("user", user);

			//获取公告信息
			List<GongGao> gongGaoList = null;
			GongGao gongGao = new GongGao();
			gongGao.setStarNum(0);
			gongGao.setPageSize(5);
			//获取资讯信息
			List<ZiXun> ziXunList = null;
			ZiXun ziXun = new ZiXun();
			ziXun.setStarNum(0);
			ziXun.setPageSize(5);
			ziXun.setState(1);

			try {
				gongGaoList = gongGaoService.getShouYeGongGaoList(gongGao);
				ziXunList = ziXunService.getZiXunListFenYe(ziXun);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				gongGaoList = null;
			}

			//redis存储规则：redis+roleId
			//判断redis里是否有当前用户
			if(!(redisAPI.exist("redis"+user.getRoleId()))){
				//如果没有
				//根据当前用户获取菜单列表mList
				mList = getmListBySuccessUser(user.getRoleId());
				if(null != mList){
					//把mList转成String
					JSONArray jsonArray= JSONArray.fromObject(mList);
					String st = jsonArray.toString();
					//把mList放到map中
					modelMap.put("menuList", st);
					//放进redis中
					redisAPI.set("redis"+user.getRoleId(), st);
				}
			}else{
				//如果有，直接从redis里取数据
				String st = redisAPI.get("redis"+user.getRoleId());
				if(null != st && !"".equals(st)){
					//放到Map集合
					modelMap.put("menuList", st);
				}else{
					return new ModelAndView("redirect:/");
				}
			}
			if(!redisAPI.exist("redis"+user.getRoleId()+"url")){
				//如果redis中没有当前用户的url列表，就放入redis中
				QuanXian quanXian = new QuanXian();
				quanXian.setRoleId(user.getRoleId());
				try {
					//获取当前用户的所有权限（url）列表
					List<Function> functionList = functionService.getFunctionListByRoId(quanXian);
					if(functionList != null){
						StringBuffer string = new StringBuffer();
						for (Function function : functionList) {
							string.append(function.getFuncUrl());
						}
						redisAPI.set("redis"+user.getRoleId()+"url", string.toString());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//把放有菜单列表的Map集合放到session中
			session.setAttribute("gongGaoList", gongGaoList);
			session.setAttribute("ziXunList", ziXunList);
			session.setAttribute(Statics.SESSION_BASE_MODEL, modelMap);
			return new ModelAndView("success",modelMap);
		}
		//如果user为空，返回到系统根目录（登录页面）
		return new ModelAndView("redirect:/");
	}

	/**
	 * 根据当前用户获取菜单列表
	 * @param roleId
	 * @return
	 */
	public List<Menu> getmListBySuccessUser(int roleId){
		List<Menu> menuList = new ArrayList<Menu>();
		QuanXian quanXian = new QuanXian();
		quanXian.setRoleId(roleId);
		try {
			//获取到主菜单，放到zhuList
			List<Function> zhuList = functionService.getZhuCaiDan(quanXian);
			if(null != zhuList){
				for (Function function : zhuList) {
					Menu menu = new Menu();
					//把每个主菜单放到menu里面
					menu.setZhuMenu(function);
					function.setRoleId(roleId);
					//获取当前主菜单的所有子菜单
					List<Function> ziList = functionService.getZiCaiDan(function);
					if(null != ziList){
						 menu.setZiMenus(ziList);
					}
					menuList.add(menu);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menuList;
	}

	/**
	 *  根据角色获取用户类型(用于菜单列表中的注册用户)
	 * @param s_role
	 * @return
	 */
	@RequestMapping(value="getUserTyepe",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getUserTyepe(HttpSession session, @RequestParam(value="s_roletianye") String s_roletianye){
		String st = "";
		try {
			List<ZiDian> ziDianList = ziDianSerivce.getZiDianList(new ZiDian("USER_TYPE"));
			JSONArray jo = JSONArray.fromObject(ziDianList);
			st = jo.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st;
	}

	/**
	 * 注销功能
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/zhuXiao")
	public String zhuXiao(HttpSession session){
		//在session中清除掉当前用户
		session.removeAttribute(Statics.SESSION_USER);
		//设置当前session失效
		session.invalidate();
		//把当前用户置空
		this.setSuccessUser(null);
		//返回到登录页面,使用redirect，url路径会显示登录时的路径
		return "redirect:/";
	}

	/**
	 * 跳转401页面
	 * @return
	 */
	@RequestMapping(value="/401")
	public String lanJieQi(){
		return "/401";
	}

	/**
	 * 跳转999页面
	 * @return
	 */
	@RequestMapping(value="/999")
	public String xiazai(){
		return "/999";
	}

	/**
	 * 注销功能
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/xiugaimima")
	public String xiugaimima(HttpSession session){
		//把当前用户放进session中，登录页面显示用户名
		User user = (User) session.getAttribute(Statics.SESSION_USER);
		session.setAttribute("user", user);
		//返回到登录页面,使用redirect，url路径会显示登录时的路径
		return "redirect:/";
	}
}
