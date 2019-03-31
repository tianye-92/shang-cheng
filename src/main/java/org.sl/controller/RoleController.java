package org.sl.controller;

import net.sf.json.JSONObject;
import org.sl.pojo.Role;
import org.sl.pojo.User;
import org.sl.service.RoleService;
import org.sl.service.UserService;
import org.sl.util.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 * @author ty
 *
 */
@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	@Resource
	private UserService userService;

	/**
	 * 获取角色列表
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/rolelist")
	public ModelAndView getRoleList(HttpSession session, Model model){
		//获取当前用户的基本模型
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			List<Role> roleList = null;
			try {
				roleList = roleService.getRoleList();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				roleList = null;
			}
			//把重新获取到的角色列表放入model中
			model.addAttribute(roleList);
			//把用户菜单列表放入model中
			model.addAllAttributes(baseModel);
			return new ModelAndView("/houTaiGuanLi/rolelist");
		}
	}

	/**
	 * 添加角色信息
	 * @param session
	 * @param role
	 * @return
	 */
	@RequestMapping("/addRole")
	@ResponseBody
	public Object addRole(HttpSession session, @RequestParam String role){
		if(null == role || "".equals(role)){
			return "nodata";
		}else{
			JSONObject objectRole = JSONObject.fromObject(role);
			Role objRole =  (Role)JSONObject.toBean(objectRole, Role.class);
			//设置注册时间为当前时间
			objRole.setCreateDate(new Date());
			//设置启用状态默认为启用（1）
			objRole.setIsStart(1);
			//设置创建者为当前用户
			objRole.setCreatedBy(((User)session.getAttribute(Statics.SESSION_USER)).getLoginCode());
			try {
				if(roleService.getRoleByCodeAndName(objRole) !=  null){
					return "chongfu";
				}else{
					roleService.addRole(objRole);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "error";
			}
			return "success";
		}
	}

	/**
	 * 修改角色信息
	 * @param session
	 * @param role
	 * @return
	 */
	@RequestMapping("/updateRole")
	@ResponseBody
	public Object updateRole(HttpSession session, @RequestParam String role){
		if(null == role || "".equals(role)){
			return "nodata";
		}else{
			JSONObject objectRole = JSONObject.fromObject(role);
			Role objRole =  (Role)JSONObject.toBean(objectRole, Role.class);
			try {
				roleService.hl_updateRole(objRole);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error";
			}
			return "success";
		}
	}

	/**
	 * 删除角色信息
	 * @param session
	 * @param role
	 * @return
	 */
	@RequestMapping("/deleteRole")
	@ResponseBody
	public Object deleteRole(HttpSession session, @RequestParam String role){
		if(null == role || "".equals(role)){
			return "nodata";
		}else{
			JSONObject objectRole = JSONObject.fromObject(role);
			Role objRole =  (Role)JSONObject.toBean(objectRole, Role.class);
			try {
				User user = new User();
				List <User> userList = null;
				user.setRoleId(objRole.getId());
				userList = userService.getUserListByRoleId(user);
				if(userList == null || userList.size() == 0){
					//如果没有用户属于这个角色，执行删除角色操作
					roleService.deleteRole(objRole);
				}else{
					//如果有用户属于这个角色，返回这些角色
					String flag = "";
					for(int i = 0; i < userList.size(); i++){
						flag += (userList.get(i).getLoginCode());
						flag +=  ",";
					}
					return flag;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error";
			}
			return "success";
		}
	}
}
