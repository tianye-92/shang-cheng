package org.sl.controller;

import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.sl.pojo.*;
import org.sl.service.FunctionService;
import org.sl.service.QuanXianService;
import org.sl.service.RoleService;
import org.sl.util.RedisAPI;
import org.sl.util.Statics;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/quanxian")
public class QuanXianController extends BaseController {

	@Resource
	private RoleService roleService;
	@Resource
	private FunctionService functionService;
	@Resource
	private RedisAPI redisAPI;
	@Resource
	private UserController userController;
	@Resource
	private QuanXianService quanXianService;

	/**
	 * 进入权限管理页面
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/loginquanxian")
	public ModelAndView loginQuanXian(HttpSession session, Model model){

		Map<String,Object> baseModel= (Map<String,Object>)session.getAttribute(Statics.SESSION_BASE_MODEL);

		if(baseModel == null){
			return new ModelAndView("redirect:/");
		}else{
			List<Role> roleList = null;
			try {
				roleList = roleService.getRoleIsStart();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				roleList = null;
				e.printStackTrace();
			}
			model.addAllAttributes(baseModel);
			model.addAttribute(roleList);
			return new ModelAndView("/houTaiGuanLi/quanxianpage");
		}
	}

	/**
	 * 获取菜单功能列表
	 * @return
	 */
	@RequestMapping(value = "/getFunctions", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getFunctions(){
		String resultString = "nodata";
		Function function = new Function();
		try {
			function.setId(0);
			List<Function> fList = functionService.getZhuFuncList(function);
			List<RoleFunctions> rList = new ArrayList<RoleFunctions>();
			if(null != fList){

				for(Function func : fList){
					RoleFunctions rFunctions = new RoleFunctions();
					rFunctions.setMainFunction(func);
					rFunctions.setSubFunctions(functionService.getZhuFuncList(func));
					rList.add(rFunctions);
				}
				resultString = JSONArray.fromObject(rList).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * 回显加载当前角色已有的功能
	 * @param rid
	 * @param fid
	 * @return
	 */
	@RequestMapping(value = "/getQuanXianDefault", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getAuthorityDefault(@RequestParam(value="rid") Integer rid, @RequestParam(value="fid") Integer fid){
		String resultString = "nodata";
		try {
			QuanXian authority = new QuanXian();
			authority.setRoleId(rid);
			authority.setFunctionId(fid);
			if(quanXianService.getQuanXianCount(authority) > 0){
				resultString =  "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * 修改权限(并把该角色的重新授权写入到redis中去)
	 * @param session
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updateQuanXian", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object modifyAuthority(HttpSession session, @RequestParam(value="ids") String ids){

		String resultString = "nodata";
			try {
				if(null != ids){
					String[] idsArrayStrings = StringUtils.split(ids, "-");
					if(idsArrayStrings.length > 0){
						User user = (User)session.getAttribute(Statics.SESSION_USER);
						quanXianService.hl_addAuthority(idsArrayStrings,user.getLoginCode());
						List<Menu> mList = null;
						//把更改之后的菜单列表（功能列表）重新set到redis中
						mList = userController.getmListBySuccessUser(Integer.valueOf(idsArrayStrings[0]));
						JSONArray jsonArray = JSONArray.fromObject(mList);
						redisAPI.set("redis"+idsArrayStrings[0], jsonArray.toString());

						//把角色所有的url放到redis中
						QuanXian quanXian = new QuanXian();
						quanXian.setRoleId(Integer.valueOf(idsArrayStrings[0]));
						List<Function> functionList = functionService.getFunctionListByRoId(quanXian);

						if(null != functionList){
							StringBuffer sBuffer = new StringBuffer();
							for(Function f:functionList){
								sBuffer.append(f.getFuncUrl());
							}
							redisAPI.set("redis"+idsArrayStrings[0]+"url", sBuffer.toString());
						}
						resultString = "success";
					}
				}
			} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}
}
