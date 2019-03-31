package org.sl.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.sl.pojo.HuiFu;
import org.sl.pojo.LiuYan;
import org.sl.pojo.LiuYanHuiFu;
import org.sl.pojo.User;
import org.sl.service.HuiFuService;
import org.sl.service.LiuYanService;
import org.sl.util.HtmlEncode;
import org.sl.util.JsonDateValueProcessor;
import org.sl.util.PageUtil;
import org.sl.util.Statics;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 留言控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/liuyan")
public class LiuYanController extends BaseController {

	@Resource
	private LiuYanService liuYanService;

	@Resource
	private HuiFuService huiFuService;

	/**
	 * 进去留言界面
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/loginLiuYanList")
	public ModelAndView loginLiuYanList(HttpSession session, Model model, @RequestParam(value="currentpage",required=false)Integer currentpage, @RequestParam(value="createdBy",required=false) String createdBy, @RequestParam(value="tiaoZhuanYe",required=false) String tiaoZhuanYe){
		//获取当前用户
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			LiuYan liuYan = new LiuYan();
			List<LiuYan> liuYanList = null;
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
				if(null != createdBy && !"".equals(createdBy)){
					liuYan.setCreatedBy(createdBy);
				}
				page.setZongShu(liuYanService.getCount(liuYan));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				page.setZongYeShu(0);
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
				liuYan.setStarNum((page.getPage()-1)*page.getPageSize());
				liuYan.setPageSize(page.getPageSize());
				//获取留言集合
				try {
					liuYanList = liuYanService.getLiuYanList(liuYan);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					liuYanList = null;
				}
				page.setItems(liuYanList);
			}else{
				page.setItems(null);
			}
			model.addAllAttributes(baseModel);
			model.addAttribute("page", page);
			model.addAttribute("currentUser",((User)session.getAttribute(Statics.SESSION_USER)).getLoginCode());
			return new ModelAndView("/XinXiGuanLi/liuyanlist");
		}
	}

	/**
	 * 获取留言信息（查看功能）
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/getLiuYan",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getLiuYan(@RequestParam(value="id", required=false) String id){
		String st = "";
		if(id == null || "".equals(id)){
			st = "nodata";
		}else{
			LiuYan liuYan = new LiuYan();
			HuiFu huiFu = new HuiFu();
			List<HuiFu> huiFuList = null;
			LiuYanHuiFu liuYanHuiFu = new LiuYanHuiFu();
			try {
				liuYan.setId(Integer.valueOf(id));
				liuYan = liuYanService.getLiuYanById(liuYan);
				huiFu.setMessageId(Integer.valueOf(id));
				huiFuList = huiFuService.getHuiFuList(huiFu);
				liuYanHuiFu.setLiuYan(liuYan);
				liuYanHuiFu.setHuiFuList(huiFuList);

				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
				JSONObject obj = JSONObject.fromObject(liuYanHuiFu, jsonConfig);
				st = obj.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				st = "failed";
			}
		}
		return st;
	}

	/**
	 * 回复留言
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/huifu",method= RequestMethod.POST)
	public ModelAndView huifu(HttpSession session, @ModelAttribute(value="huiFu") HuiFu huiFu){
		if(null == session.getAttribute(Statics.SESSION_BASE_MODEL)){
			return new ModelAndView("redirect:/");
		}else{
			huiFu.setCreateTime(new Date());
			huiFu.setCreatedBy(((User)session.getAttribute(Statics.SESSION_USER)).getLoginCode());
			//判断回复内容是否为空
			if(null != huiFu.getReplyContent() && !"".equals(huiFu.getReplyContent())){
				//把处理Html防止注入后的回复内容放进去
				huiFu.setReplyContent(HtmlEncode.htmlEncode(huiFu.getReplyContent()));
			}
			try {
				if(huiFuService.addHuiFu(huiFu)>0){
					LiuYan liuYan = new LiuYan();
					liuYan.setId(huiFu.getMessageId());
					liuYan.setState(1);
					liuYanService.updateLiuYan(liuYan);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/liuyan/loginLiuYanList");
		}
	}

	/**
	 * 删除留言信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deleteLiuYan",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String deleteLiuYan(@RequestParam(value="id",required=false) String id){
		String st = "false";
		LiuYan liuYan = new LiuYan();
		liuYan.setId(Integer.valueOf(id));
		try {
			if(liuYanService.deleteLiuYan(liuYan)>0){
				//如果留言删除成功，所属的回复也删除
				HuiFu huiFu = new HuiFu();
				huiFu.setMessageId(Integer.valueOf(id));
				st = "success";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st;
	}

	/**
	 * 进入我的留言界面
	 * @param session
	 * @param model
	 * @param currentpage
	 * @return
	 */
	@RequestMapping("/myliuyan")
	public ModelAndView myMessage(HttpSession session, Model model, @RequestParam(value="currentpage",required=false)Integer currentpage){
		Map<String,Object> baseModel= (Map<String,Object>)session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(baseModel == null){
			return new ModelAndView("redirect:/");
		}else{
			User sessionUser =  ((User)session.getAttribute(Statics.SESSION_USER));
			loginLiuYanList(session,model,currentpage,sessionUser.getLoginCode(), null);
		}
		return new ModelAndView("/XinXiGuanLi/myliuyan");
	}

	/**
	 * 我的留言界面显示回复信息
	 * @param request
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/huifuInfo", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getReply(HttpServletRequest request, HttpSession session, @RequestParam(value="id") Integer id){
		String st = "";
		if(null == id || "".equals(id)){
			st = "nodata";
		}else{
			try {
				HuiFu huiFu = new HuiFu();
				huiFu.setMessageId(id);
				List<HuiFu> rList = huiFuService.getHuiFuList(huiFu);
				if(rList == null){
					st = "";
				}else{
					st = JSONArray.fromObject(rList).toString();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				st = "failed";
			}
		}
		return st;
	}

	/**
	 * 我的留言界面提交留言
	 * @param message
	 * @param session
	 * @return
	 */
	@RequestMapping("/addliuyan")
	public ModelAndView addMessage(@ModelAttribute("liuyan") LiuYan liuYan, HttpSession session){

		if(session.getAttribute(Statics.SESSION_BASE_MODEL) == null){
			return new ModelAndView("redirect:/");
		}else{
			try {
				User user =  ((User)session.getAttribute(Statics.SESSION_USER));
				liuYan.setCreatedBy(user.getLoginCode());
				liuYan.setState(0);
				liuYan.setCreateTime(new Date());
				if(null != liuYan.getMessageContent() && !liuYan.getMessageContent().equals("")){
					liuYan.setMessageContent(HtmlEncode.htmlEncode(liuYan.getMessageContent()));
				}
				liuYanService.addLiuYan(liuYan);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ModelAndView("redirect:/liuyan/myliuyan");
	}
}
