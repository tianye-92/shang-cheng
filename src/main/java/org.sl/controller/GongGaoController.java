package org.sl.controller;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.sl.pojo.GongGao;
import org.sl.pojo.User;
import org.sl.service.GongGaoService;
import org.sl.util.HtmlEncode;
import org.sl.util.JsonDateValueProcessor;
import org.sl.util.PageUtil;
import org.sl.util.Statics;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公告控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/gonggao")
public class GongGaoController extends BaseController {

	@Resource
	private GongGaoService gongGaoService;

	/**
	 * 进入公告管理页面
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/loginGongGaoPage")
	public ModelAndView loginGongGaoPage(HttpSession session, Model model,
                                         @RequestParam(value="currentpage",required=false)Integer currentpage,
                                         @RequestParam(value="tiaoZhuanYe",required=false) String tiaoZhuanYe,
                                         boolean flag){
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			List<GongGao> gongGaoList = null;
			GongGao gongGao = new GongGao();
			//配置分页信息
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
				if(flag){
					page.setZongShu(gongGaoService.getShouYeCount(gongGao));
				}else{
					page.setZongShu(gongGaoService.getCount(gongGao));
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				page.setZongShu(0);
			}
			if(page.getZongYeShu()>0){
				if(tiaozhuan){
					page.setPage(Integer.valueOf(tiaoZhuanYe));
				}
				if(currentpage != null){
					page.setPage(currentpage);
				}
				if(page.getPage()<=0){
					page.setPage(1);
				}
				if(page.getPage()>page.getZongYeShu()){
					page.setPage(page.getZongYeShu());
				}
				gongGao.setStarNum((page.getPage()-1)*page.getPageSize());
				gongGao.setPageSize(page.getPageSize());
			try {
				if(flag){
					gongGaoList = gongGaoService.getShouYeGongGaoList(gongGao);
				}else{
					gongGaoList = gongGaoService.getGongGaoList(gongGao);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				gongGaoList = null;
			}
				page.setItems(gongGaoList);
			}else{
				page.setItems(null);
			}
			model.addAllAttributes(baseModel);
			model.addAttribute("page", page);
			return new ModelAndView("/XinXiGuanLi/gonggao");
		}
	}

	/**
	 * 添加公告方法
	 * @param gongGao
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/addGongGao",method= RequestMethod.POST)
	public ModelAndView addGongGao(@ModelAttribute("gongGao") GongGao gongGao, HttpSession session){
		if(null == session.getAttribute(Statics.SESSION_BASE_MODEL)){
			return new ModelAndView("redirect:/");
		}else{
			User user = (User)session.getAttribute(Statics.SESSION_USER);
			gongGao.setPublisher(user.getLoginCode());
			gongGao.setPublishTime(new Date());
			if(null != gongGao.getTitle() && !"".equals(gongGao.getTitle())){
				gongGao.setTitle(HtmlEncode.htmlEncode(gongGao.getTitle()));
			}
			try {
				gongGaoService.addGongGao(gongGao);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/gonggao/loginGongGaoPage");
		}
	}

	/**
	 * 查看公告方法
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/gonggaoInfo",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String gonggaoInfo(@RequestParam(value="id") Integer id){
		String st = "";
		if(null == id){
			st = "nodata";
		}else{
			GongGao gongGao = new GongGao();
			gongGao.setId(id);
			try {
				gongGao = gongGaoService.getGongGaoById(gongGao);
				if(null != gongGao){
					gongGao.setTitle(HtmlEncode.htmlDecode(gongGao.getTitle()));
					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
					st = JSONObject.fromObject(gongGao, jsonConfig).toString();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				st = "failed";
			}
		}
		return st;
	}

	/**
	 * 修改公告
	 * @param gongGao
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/updateGongGao",method= RequestMethod.POST)
	public ModelAndView updateGongGao(@ModelAttribute("gongGao") GongGao gongGao, HttpSession session){
		if(null == session.getAttribute(Statics.SESSION_BASE_MODEL)){
			return new ModelAndView("redirect:/");
		}else{
			User user = (User) session.getAttribute(Statics.SESSION_USER);
			gongGao.setPublisher(user.getLoginCode());
			gongGao.setPublishTime(new Date());
			if(null != gongGao.getTitle() && !"".equals(gongGao.getTitle())){
				gongGao.setTitle(HtmlEncode.htmlEncode(gongGao.getTitle()));
			}
			try {
				gongGaoService.updateGongGao(gongGao);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/gonggao/loginGongGaoPage");
		}
	}

	/**
	 * 删除公告
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/deleteGongGao",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String deleteGongGao(@RequestParam(value="id",required=true) Integer id, HttpSession session){
		String st = "";
		if(null == id || "".equals(id)){
			st = "nodata";
		}else{
			GongGao gongGao = new GongGao();
			gongGao.setId(id);
			try {
				gongGaoService.deleteGongGao(gongGao);
				st = "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				st = "failed";
			}
		}
		return st;
	}

	/**
	 * 首页more功能
	 * @param session
	 * @param model
	 * @param currentpage
	 * @param tiaoZhuanYe
	 * @return
	 */
	@RequestMapping(value="/shouYeGongGaoList")
	public ModelAndView shouYeGongGaoList(HttpSession session, Model model,
                                          @RequestParam(value="currentpage",required=false)Integer currentpage,
                                          @RequestParam(value="tiaoZhuanYe",required=false) String tiaoZhuanYe){
		if(null == session.getAttribute(Statics.SESSION_BASE_MODEL)){
			return new ModelAndView("redirect:/");
		}else{
			loginGongGaoPage(session, model, currentpage, tiaoZhuanYe, true);
			return new ModelAndView("/shouye/gonggaolist");
		}
	}

	/**
	 * 首页点击公告，跳转公告显示信息页面
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value="shouYeGongGao",produces={"text/html;charset=UTF-8"})
	public ModelAndView shouYeGongGao(HttpSession session, Model model, @RequestParam(value="id") Integer id){
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			if(null == id || "".equals(id)){
				id = 0;
			}else{
				GongGao gongGao = new GongGao();
				gongGao.setId(id);
				try {
					gongGao = gongGaoService.getGongGaoById(gongGao);
					session.setAttribute("gongGao", gongGao);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			model.addAllAttributes(baseModel);
			return new ModelAndView("/shouye/gonggaoinfo");
		}
	}
}
