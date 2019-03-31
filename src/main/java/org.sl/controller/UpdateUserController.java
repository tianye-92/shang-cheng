package org.sl.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.sl.pojo.Role;
import org.sl.pojo.User;
import org.sl.pojo.ZiDian;
import org.sl.service.RoleService;
import org.sl.service.UserService;
import org.sl.service.ZiDianService;
import org.sl.util.JsonDateValueProcessor;
import org.sl.util.PageUtil;
import org.sl.util.SqlTools;
import org.sl.util.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户控制器
 * @author ty
 *
 */
@Controller
@RequestMapping(value="/updateUser")
public class UpdateUserController extends BaseController {

	@Autowired
	private UserService userService;
	@Resource
	private ZiDianService ziDianSerivce;
	@Resource
	private RoleService roleService;

	/**
	 * 修改当前用户密码
	 * @param upUser
	 * @return
	 */
	@RequestMapping(value="/updatePassword")
	@ResponseBody
								//传参名称（upUser）必须与Ajax的data（key名称）一致
	public Object updatePassword(@RequestParam String upUser){
		//获取当前用户
		User userNew = this.getSuccessUser();
		if(null == userNew && userNew.equals("")){
			//如果当前用户为空
			return "nodata";
			//model.setViewName("nodata");
		}else{
			//如果不为空
			JSONObject userObj = JSONObject.fromObject(upUser);
			//转换为Javabean
			User newUser = (User) JSONObject.toBean(userObj, User.class);
			//把当前用户的id放进传进来的对象
			newUser.setId(userNew.getId());
			//把当前用户的loginCode放进传进来的对象
			newUser.setLoginCode(userNew.getLoginCode());
			try {
				//如果能查到，代表原密码正确
				if(null != userService.getUser(newUser)){
					//把新密码放进传进来的对象
					newUser.setPassword(newUser.getPassword2());
					newUser.setPassword2(null);
					//修改数据库
					userService.updateUser(newUser);
					return "success";
					//model.setViewName("success");
				}else{
					//查不到，代表原密码错误
					return "oldpwdwrong";
					//model.setViewName("oldpwdwrong");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failed";
				//model.setViewName("failed");
			}
		}
	}

	/**
	 * 用于菜单列表（修改密码）跳转
	 * @return
	 */
	@RequestMapping(value="/modifypwd")
	public ModelAndView tiaoZhuan(HttpSession session, Model model){
		Map<String, Object> sessionModel = (Map<String, Object>)session.getAttribute(Statics.SESSION_BASE_MODEL);
		model.addAllAttributes(sessionModel);
		return new ModelAndView("/huiYuanGuanLi/xiugaimima");
	}

	/**
	 * 用于菜单列表（注册新会员）跳转
	 * @return
	 */
	@RequestMapping(value="/zhuce")
	public ModelAndView zhuCe(HttpSession session, Model model){
		Map<String, Object> sessionModel = (Map<String, Object>)session.getAttribute(Statics.SESSION_BASE_MODEL);
		//获取已启用角色列表
		List<Role> roleListTrue = null;
		List<ZiDian> ziDianList = null;
		//实例化一个字典类,因为要给他赋值typeCode,才能在数据库查到你想要的数据
		ZiDian ziDian = new ZiDian();
		ziDian.setTypeCode("CARD_TYPE");
		try {
			roleListTrue = roleService.getRoleIsStart();
			ziDianList = ziDianSerivce.getZiDianList(ziDian);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.setAttribute("roleListTrue", roleListTrue);
		session.setAttribute("ziDianList",ziDianList);
		model.addAllAttributes(sessionModel);
		return new ModelAndView("/huiYuanGuanLi/zhucehuiyuan");
	}

	/**
	 * 用于菜单列表（修改资料）跳转
	 * @return
	 */
	@RequestMapping(value="/xiugai")
	public ModelAndView xiugai(HttpSession session, Model model){
		Map<String, Object> sessionModel = (Map<String, Object>)session.getAttribute(Statics.SESSION_BASE_MODEL);
		model.addAllAttributes(sessionModel);
		return new ModelAndView("/huiYuanGuanLi/xiugaiziliao");
	}

	/**
	 * 获取用户列表（使用分页查询）
	 * @return
	 */
	@RequestMapping("/userlist")
	public ModelAndView userList(HttpSession session, Model model,
                                 @RequestParam(value="currentpage",required=false)Integer currentpage ,
                                 @RequestParam(value="tiaoZhuanYe",required=false) String tiaoZhuanYe ,
                                 @RequestParam(value="s_referCode",required=false) String s_referCode,
                                 @RequestParam(value="s_loginCode",required=false) String s_loginCode,
                                 @RequestParam(value="s_roleId",required=false) String s_roleId,
                                 @RequestParam(value="s_isStart",required=false) String s_isStart){
		//创建一个Map集合，用来接收当前用户的菜单列表
		Map<String, Object> sessionModel = (Map<String, Object>)session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(sessionModel == null){
			return new ModelAndView("redirect:/");
		}else{
			//设置查询条件-把传过来的参数全部放入user对象中
			User user = new User();
			if(null != s_loginCode && !"".equals(s_loginCode))
				user.setLoginCode("%"+SqlTools.sqlTools(s_loginCode)+"%");
			if(null != s_referCode && !"".equals(s_referCode))
				user.setReferCode("%"+SqlTools.sqlTools(s_referCode)+"%");
			if(!StringUtils.isBlank(s_isStart)){
				user.setIsStart(Integer.valueOf(s_isStart));
			}else {
				user.setIsStart(null);
			}
			if(!StringUtils.isBlank(s_roleId)){
				user.setRoleId(Integer.valueOf(s_roleId));
			}else{
				user.setRoleId(null);
			}

			//分页设置
			PageUtil page = new PageUtil();
			//正整数的正则表达式
			String zhengZe = "^[0-9]*[1-9][0-9]*$";
			//编译正则表达式
			Pattern pattern = Pattern.compile(zhengZe);
			boolean tiaozhuan = false;
			if(null != tiaoZhuanYe){
				//传参是否与正则表达式相匹配
				Matcher matcher = pattern.matcher(tiaoZhuanYe);
				tiaozhuan = matcher.matches();
			}
			try {
				//获取用户的总数量，然后计算分页的总页数
				page.setZongShu(userService.count(user));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				//如果异常，总页数赋值为0
				page.setZongYeShu(0);
			}
			//判断总页数是否大于0
			if(page.getZongYeShu() > 0){
				//判断跳转页
				if(tiaozhuan){
					//把String转换成Integer然后赋值到当前页面
					page.setPage(Integer.parseInt(tiaoZhuanYe));
				}
				//判断实现分页的时候，传过来的页数id
				if(currentpage != null){
					page.setPage(currentpage);
				}
				//判断当前页是否小于0
				if(page.getPage() <= 0){
					page.setPage(1);
				}
				//判断当前页是否大于总页数
				if(page.getPage() > page.getZongYeShu()){
					page.setPage(page.getZongYeShu());
				}
				//把分页数据包装到user对象中
				user.setStarNum((page.getPage() - 1) * page.getPageSize());
				user.setPageSize(page.getPageSize());

				//创建一个集合，用来接收分页的用户列表
				List<User> userList = null;
				try {
					//实现分页的用户列表查询
					userList = userService.getUserList(user);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					//如果异常，把集合置空
					userList = null;
				}
				//把查出来的分页用户列表放到page对象中，在页面中可以直接使用page对象获取
				page.setItems(userList);
			}else{
				page.setItems(null);
			}
			//获取角色列表
			List<Role> roleList = null;
			//获取已启用角色列表
			List<Role> roleListTrue = null;
			List<ZiDian> ziDianList = null;
			//实例化一个字典类,因为要给他赋值typeCode,才能在数据库查到你想要的数据
			ZiDian ziDian = new ZiDian("CARD_TYPE");
			try {
				roleListTrue = roleService.getRoleIsStart();
				roleList = roleService.getRoleList();
				ziDianList = ziDianSerivce.getZiDianList(ziDian);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//把在前端页面中所有所需要的数据全部放到model中
			session.setAttribute("roleList",roleList);
			session.setAttribute("roleListTrue", roleListTrue);
			session.setAttribute("ziDianList",ziDianList);
			model.addAllAttributes(sessionModel);
			model.addAttribute("page", page);
			model.addAttribute("s_loginCode", s_loginCode);
			model.addAttribute("s_referCode", s_referCode);
			model.addAttribute("s_isStart", s_isStart);
			model.addAttribute("s_roleId", s_roleId);
			return new ModelAndView("/houTaiGuanLi/userlist");
		}
	}

	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/addUser",method= RequestMethod.POST)
	public ModelAndView addUser(HttpSession session, @ModelAttribute("user") User user){
		//判断session里是否有基础模型
		if(session.getAttribute(Statics.SESSION_BASE_MODEL) == null){
			//如果为空，返回登录页面
			return new ModelAndView("redirect:/");
		}else{//如果不为空，开始进行添加操作
			//获取提交信息的证件信息
			String card = user.getIdCard();
			//截取证件信息后6位，用于设置注册用户的密码
			//substring()用于截取字符串，参数为从第几位开始截，截到字符串末尾，然后返回截取后的字符串
			String st = card.substring(card.length()-6);
			//设置为密码
			user.setPassword(st);
			user.setPassword2(st);
			//设置注册时间
			user.setCreateTime(new Date());
			//把当前操作用户的id和名称设置为注册用户推荐人id和名称
			user.setReferId(this.getSuccessUser().getId());
			user.setReferCode(this.getSuccessUser().getLoginCode());
			//设置最后更新时间
			user.setLastUpdateTime(new Date());
			try {
				//添加用户
				userService.addUser(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/updateUser/userlist");
		}
	}

	/**
	 *  根据角色获取用户类型(用于＋号的注册)
	 * @param s_role
	 * @return
	 */
	@RequestMapping(value="/getUserTyepe",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getUserTyepe(HttpSession session, @RequestParam(value="s_role") String s_role){
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
	 * 查找用户，用于判断是否重名
	 * @param loginCode
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/logincodeisexit", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public String loginCodeIsExit(@RequestParam(value="loginCode",required=false) String loginCode,
								  @RequestParam(value="id",required=false) String id){
		String result = "failed";
		User user = new User();
		user.setLoginCode(loginCode);
		if(!id.equals("-1"))
			user.setId(Integer.valueOf(id));
		try {
			if(userService.getUserByName(user) == 0)
				result = "only";
			else
				result = "repeat";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * 处理文件上传
	 * @param cardFile
	 * @param bankFile
	 * @param m_cardFile
	 * @param m_bankFile
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upload",produces={"text/html;charset=UTF-8"} )
	@ResponseBody
	public Object upLoad(@RequestParam(value="a_fileInputID", required=false) MultipartFile cardFile,
                         @RequestParam(value="a_fileInputBank", required=false) MultipartFile bankFile,
                         @RequestParam(value="m_fileInputID", required=false) MultipartFile m_cardFile,
                         @RequestParam(value="m_fileInputBank", required=false) MultipartFile m_bankFile,
                         HttpSession session, HttpServletRequest request){
		//获取存放文件的路径（如果没有会自动创建）File.separator:获取一个系统依赖的默认名称分隔符字符“/”
		String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"upLoadFiles");
		//创建一个大小，用于判断上传文件的大小
		int fileSize = 500000;
		Object obj = null;
		if(null != cardFile){
			//上传身份证图片，最后一个参数是重命名文件时，文件名的后缀
			obj = baseUpLoad(request, path, cardFile, fileSize, "_IDcard.jpg");
		}
		if(null != bankFile){
			//上传银行卡图片
			obj =  baseUpLoad(request, path, bankFile, fileSize, "_bank.jpg");
		}
		if(null != m_cardFile){
			//修改身份证图片
			obj =  baseUpLoad(request, path, m_cardFile, fileSize, "_IDcard.jpg");
		}
		if(null != m_bankFile){
			//修改银行卡图片
			obj =  baseUpLoad(request, path, m_bankFile, fileSize, "_bank.jpg");
		}
		return obj;
	}
	/**
	 * 文件上传提取的公共方法
	 * @return
	 */
	public Object baseUpLoad(HttpServletRequest request, String path, MultipartFile file, int fileSize, String filePrefix){
		//获取上传文件的原名称
		String oldName = file.getOriginalFilename();
		//获取文件名称后缀
		String prefix = FilenameUtils.getExtension(oldName);
		//判断文件大小
		if(file.getSize() > fileSize){
			//如果大于设定大小，返回1
			return "1";
		}else if(!prefix.equalsIgnoreCase("jpg") && !prefix.equalsIgnoreCase("png") && !prefix.equalsIgnoreCase("jpeg") && !prefix.equalsIgnoreCase("pneg")){
			//如果不是这四个文件格式，返回2  ---- 比较使用忽略大小写比较
			return "2";
		}else{
			//如果符合条件，给文件重命名
			String fileName = RandomUtils.nextInt(1000000)+filePrefix;
			//创建文件路径及名称（样式）
			File newFile = new File(path,fileName);
			if(!newFile.exists()){
				//判断此文件是否存在，如果不存在--->创建
				newFile.mkdirs();
			}
			//以文件样式保存传过来的文件
			try {
				file.transferTo(newFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//返回文件路径
			String url = request.getContextPath()+"/statics/upLoadFiles/"+fileName;
			return url;
		}
	}

	/**
	 * 删除文件显示区的文件
	 * @param picpath
	 * @param id
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/delpic", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object delPic(@RequestParam(value="picpath",required=false) String picpath,
                         @RequestParam(value="id",required=false) String id,
                         HttpServletRequest request, HttpSession session){
		//picpath：文件路径
		if(picpath == null || picpath.equals("")){
			//代表本来就没有文件，所以提示删除成功
			return "success";
		}else{
			//如果有，把picpath解析成物理路径
			//以“/”截取成字符串数组
			String[] paths = picpath.split("/");
			String path = request.getSession().getServletContext().getRealPath(paths[1]+File.separator+paths[2]+File.separator+paths[3]);
			File file = new File(path);
			if(file.exists()){
				if(file.delete()){
					//如果存在，就删除
					if(id.equals("0")){
						//这个0是标记是否为注册还是修改，如果是0，代表是注册新用户
						return "success";
					}else{
						//如果不是0，代表是修改，id就是
						User user = new User();
						user.setId(Integer.valueOf(id));
						if(picpath.indexOf("_IDcard.jpg") != -1){
							//如果路径存在“_IDcard.jpg”
							user.setIdCardPicPath(picpath);
						}else if(picpath.indexOf("_bank.jpg") != -1){
							//如果路径存在“_bank.jpg”
							user.setBankPicPath(picpath);
						}
						try {
							if(userService.delUserPic(user) > 0){
								//删除成功
								return "success";
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return "error";
						}
					}
				}
			}
		}
		return "error";
	}


	/**
	 * 根据用户id查询用户所有信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getuser", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getUser(@RequestParam(value="id",required=false) String id){
		String cjson = "";
		if(null == id || "".equals(id)){
			return "nodata";
		}else{
			try {
				User user = new User();
				user.setId(Integer.valueOf(id));
				user = userService.getUserById(user);
				//user对象里有日期，所有有日期的属性，都要按照此日期格式进行json转换（对象转json）
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
				JSONObject jo = JSONObject.fromObject(user,jsonConfig);
				cjson = jo.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failed";
			}
				return cjson;
		}
	}

	/**
	 * 修改用户
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/updateUser",method= RequestMethod.POST)
	public ModelAndView updateUser(HttpSession session, @ModelAttribute(value="user") User user){
		if(session.getAttribute(Statics.SESSION_BASE_MODEL) == null){
			return new ModelAndView("redirect:/");
		}else{
			user.setLastUpdateTime(new Date());
			try {
				userService.updateUser(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/updateUser/userlist");
		}
	}

	/**
	 * 修改用户(新会员管理页面)
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/updateNewUser",method= RequestMethod.POST)
	public ModelAndView updateNewUser(HttpSession session, @ModelAttribute(value="user") User user){
		if(session.getAttribute(Statics.SESSION_BASE_MODEL) == null){
			return new ModelAndView("redirect:/");
		}else{
			user.setLastUpdateTime(new Date());
			try {
				userService.updateUser(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/updateUser/xinhuiyuanguanli");
		}
	}

	/**
	 * 删除用户
	 * @param delId
	 * @param delIdCardPicPath
	 * @param delBankPicPath
	 * @param delUserType
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deluser", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object delUser(@RequestParam(value="delId",required=false) String delId,
                          @RequestParam(value="delIdCardPicPath",required=false) String delIdCardPicPath,
                          @RequestParam(value="delBankPicPath",required=false) String delBankPicPath,
                          @RequestParam(value="delUserType",required=false) String delUserType,
                          HttpServletRequest request, HttpSession session){

		String result= "false" ;
		User user = new User();
		user.setId(Integer.valueOf(delId));
		try {
			//若被删除的用户为：普通消费会员、VIP会员、加盟店  则不可被删除
			if(delUserType.equals("2") || delUserType.equals("3") || delUserType.equals("4")){
				result = "noallow";
			}else{
				if(this.delPic(delIdCardPicPath,delId,request,session).equals("success") && this.delPic(delBankPicPath,delId,request,session).equals("success")){
					//用户身份证路径和银行卡路径都删除成功了，才可以删除用户
					if(userService.delUser(user) > 0){
						result = "success";
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取注册用户列表（使用分页查询）
	 * @return
	 */
	@RequestMapping("/xinhuiyuanguanli")
	public ModelAndView xinhuiyuanguanli(HttpSession session, Model model,
                                         @RequestParam(value="currentpage",required=false)Integer currentpage ,
                                         @RequestParam(value="tiaoZhuanYe",required=false) String tiaoZhuanYe ,
                                         @RequestParam(value="s_referCode",required=false) String s_referCode,
                                         @RequestParam(value="s_loginCode",required=false) String s_loginCode,
                                         @RequestParam(value="s_isStart",required=false) String s_isStart){
		//创建一个Map集合，用来接收当前用户的菜单列表
		Map<String, Object> sessionModel = (Map<String, Object>)session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(sessionModel == null){
			return new ModelAndView("redirect:/");
		}else{
			//设置查询条件-把传过来的参数全部放入user对象中
			User user = new User();
			user.setUserType("1");
			if(null != s_loginCode && !"".equals(s_loginCode))
				user.setLoginCode("%"+SqlTools.sqlTools(s_loginCode)+"%");
			if(null != s_referCode && !"".equals(s_referCode))
				user.setReferCode("%"+SqlTools.sqlTools(s_referCode)+"%");
			if(!StringUtils.isBlank(s_isStart)){
				user.setIsStart(Integer.valueOf(s_isStart));
			}else {
				user.setIsStart(null);
			}

			//分页设置
			PageUtil page = new PageUtil();
			//正整数的正则表达式
			String zhengZe = "^[0-9]*[1-9][0-9]*$";
			//编译正则表达式
			Pattern pattern = Pattern.compile(zhengZe);
			boolean tiaozhuan = false;
			if(null != tiaoZhuanYe){
				//传参是否与正则表达式相匹配
				Matcher matcher = pattern.matcher(tiaoZhuanYe);
				tiaozhuan = matcher.matches();
			}
			try {
				//获取用户的总数量，然后计算分页的总页数
				page.setZongShu(userService.count(user));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				//如果异常，总页数赋值为0
				page.setZongYeShu(0);
			}
			//判断总页数是否大于0
			if(page.getZongYeShu() > 0){
				//判断跳转页
				if(tiaozhuan){
					//把String转换成Integer然后赋值到当前页面
					page.setPage(Integer.parseInt(tiaoZhuanYe));
				}
				//判断实现分页的时候，传过来的页数id
				if(currentpage != null){
					page.setPage(currentpage);
				}
				//判断当前页是否小于0
				if(page.getPage() <= 0){
					page.setPage(1);
				}
				//判断当前页是否大于总页数
				if(page.getPage() > page.getZongYeShu()){
					page.setPage(page.getZongYeShu());
				}
				//把分页数据包装到user对象中
				user.setStarNum((page.getPage() - 1) * page.getPageSize());
				user.setPageSize(page.getPageSize());

				//创建一个集合，用来接收分页的用户列表
				List<User> userList = null;
				try {
					//实现分页的用户列表查询
					userList = userService.getUserList(user);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					//如果异常，把集合置空
					userList = null;
				}
				//把查出来的分页用户列表放到page对象中，在页面中可以直接使用page对象获取
				page.setItems(userList);
			}else{
				page.setItems(null);
			}
			//获取角色列表
			List<Role> roleList = null;
			//获取已启用角色列表
			List<Role> roleListTrue = null;
			List<ZiDian> ziDianList = null;
			//实例化一个字典类,因为要给他赋值typeCode,才能在数据库查到你想要的数据
			ZiDian ziDian = new ZiDian("CARD_TYPE");
			try {
				roleListTrue = roleService.getRoleIsStart();
				roleList = roleService.getRoleList();
				ziDianList = ziDianSerivce.getZiDianList(ziDian);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//把在前端页面中所有所需要的数据全部放到model中
			session.setAttribute("roleList",roleList);
			session.setAttribute("roleListTrue", roleListTrue);
			session.setAttribute("ziDianList",ziDianList);
			model.addAllAttributes(sessionModel);
			model.addAttribute("page", page);
			model.addAttribute("s_loginCode", s_loginCode);
			model.addAttribute("s_referCode", s_referCode);
			model.addAttribute("s_isStart", s_isStart);
			return new ModelAndView("/huiYuanGuanLi/xinhuiyuan");
		}
	}

}
