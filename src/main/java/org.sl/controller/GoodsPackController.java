package org.sl.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.sl.pojo.Goods;
import org.sl.pojo.GoodsPack;
import org.sl.pojo.ZiDian;
import org.sl.service.GoodsPackService;
import org.sl.service.GoodsService;
import org.sl.service.ZiDianService;
import org.sl.util.PageUtil;
import org.sl.util.SqlTools;
import org.sl.util.Statics;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 商品套餐控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/goodspack")
public class GoodsPackController extends BaseController {

	@Resource
	private GoodsPackService goodsPackService;
	@Autowired
	private ZiDianService ziDianService;
	@Resource
	private GoodsService goodsService;

	/**
	 * 进入商品套餐页面
	 * @param session
	 * @param model
	 * @param currentpage
	 * @param s_goodsPackName
	 * @param s_typeId
	 * @param s_state
	 * @param tiaoZhuanYe
	 * @return
	 */
	@RequestMapping(value="/logingoodspackpage", produces = {"text/html;charset=UTF-8"})
	public ModelAndView logingoodspackpage(HttpSession session, Model model,
                                           @RequestParam(value="currentpage",required=false)Integer currentpage,
                                           @RequestParam(value="s_goodsPackName",required=false) String s_goodsPackName,
                                           @RequestParam(value="s_typeId",required=false) String s_typeId,
                                           @RequestParam(value="s_state",required=false) String s_state,
                                           @RequestParam(value="tiaoZhuanYe",required=false) String tiaoZhuanYe){
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			GoodsPack goodsPack = new GoodsPack();
			if(null != s_goodsPackName && !"".equals(s_goodsPackName))
				goodsPack.setGoodsPackName("%"+SqlTools.sqlTools(s_goodsPackName)+"%");
			if(!StringUtils.isBlank(s_typeId)){
				goodsPack.setTypeId(Integer.valueOf(s_typeId));
			}else {
				goodsPack.setTypeId(null);
			}
			if(!StringUtils.isBlank(s_state)){
				goodsPack.setState(Integer.valueOf(s_state));
			}else {
				goodsPack.setState(null);
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
				page.setZongShu(goodsPackService.count(goodsPack));
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
				goodsPack.setStarNum((page.getPage() - 1) * page.getPageSize());
				goodsPack.setPageSize(page.getPageSize());

				List<GoodsPack> goodsPackList = null;
				try {
					goodsPackList = goodsPackService.getGoodsPackList(goodsPack);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					goodsPackList = null;
				}
				page.setItems(goodsPackList);
			}else{
				page.setItems(null);
			}
			List<ZiDian> ziDianList = null;
			//实例化一个字典类,因为要给他赋值typeCode,才能在数据库查到你想要的数据
			ZiDian ziDian = new ZiDian("PACK_TYPE");
			try {
				ziDianList = ziDianService.getZiDianList(ziDian);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//把在前端页面中所有所需要的数据全部放到model中
			session.setAttribute("ziDianList",ziDianList);
			model.addAttribute("page", page);
			model.addAttribute("s_goodsPackName", s_goodsPackName);
			model.addAttribute("s_typeId", s_typeId);
			model.addAttribute("s_state", s_state);
			model.addAllAttributes(baseModel);
			return new ModelAndView("/houTaiGuanLi/goodspacklist");
		}
	}

	/**
	 * 跳转到添加商品套餐页面
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addgoodspack")
	public ModelAndView addgoodspack(HttpSession session, Model model){
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			model.addAllAttributes(baseModel);
			return new ModelAndView("/houTaiGuanLi/addgoodspack");
		}
	}

	/**
	 * 跳转到修改商品套餐页面
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/updategoodspack")
	public ModelAndView updategoodspack(HttpSession session, Model model, @RequestParam(value="id")Integer id){
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			GoodsPack goodsPack = new GoodsPack();
			List<Goods> goodsList = null;
			if(null != id && !"".equals(id)){
				goodsPack.setId(id);
				try {
					//查询套餐信息里边的商品集合
					goodsList = goodsPackService.getGoodsById(goodsPack);
					//查询套餐信息
					goodsPack = goodsPackService.getGoodsPackById(goodsPack);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					goodsPack = null;
					goodsList = null;
				}
			}
			model.addAllAttributes(baseModel);
			model.addAttribute(goodsPack);
			model.addAttribute(goodsList);
			return new ModelAndView("/houTaiGuanLi/modifygoodspack");
		}
	}

	/**
	 * 修改商品套餐
	 * @param session
	 * @param goodspack
	 * @return
	 */
	@RequestMapping(value="/savemodifygoodspackl",produces = {"text/html;charset=UTF-8"})
	public ModelAndView savemodifygoodspackl(HttpSession session, @ModelAttribute("goodspack") GoodsPack goodspack, @RequestParam(value="m_listGoods") String m_listGoods){
		if(null == session.getAttribute(Statics.SESSION_BASE_MODEL)){
			return new ModelAndView("redirect:/");
		}else{
			//获取套餐中的商品集合
			JSONArray obj = JSONArray.fromObject(m_listGoods);
			List<Goods> goodsList = obj.toList(obj, Goods.class);
			goodspack.setLastUpdateTime(new Date());
			try {
				goodsPackService.hl_updateGoodsPack(goodspack, goodsList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/goodspack/logingoodspackpage");
		}
	}

	/**
	 * 查看商品套餐
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/viewgoodspack")
	public ModelAndView viewgoodspack(HttpSession session, Model model, @RequestParam(value="id")Integer id){
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null ==baseModel){
			return new ModelAndView("redirect:/");
		}else{
			GoodsPack goodsPack = new GoodsPack();
			List<Goods> goodsList = null;
			if(null != id && !"".equals(id)){
				goodsPack.setId(id);
				try {
					//查询套餐信息里边的商品集合
					goodsList = goodsPackService.getGoodsById(goodsPack);
					//查询套餐信息
					goodsPack = goodsPackService.getGoodsPackById(goodsPack);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					goodsPack = null;
					goodsList = null;
				}
			}
			model.addAllAttributes(baseModel);
			model.addAttribute(goodsPack);
			model.addAttribute(goodsList);
			return new ModelAndView("/houTaiGuanLi/viewgoodspack");
		}
	}

	/**
	 * 添加商品套餐
	 * @param session
	 * @param goodspack
	 * @return
	 */
	@RequestMapping(value="saveaddgoodspack",produces = {"text/html;charset=UTF-8"})
	public ModelAndView saveaddgoodspack(HttpSession session, @ModelAttribute("goodspack") GoodsPack goodspack, @RequestParam(value="a_listGoods") String a_listGoods){
		if(null == session.getAttribute(Statics.SESSION_BASE_MODEL)){
			return new ModelAndView("redirect:/");
		}else{
			//获取套餐中的商品集合
			JSONArray obj = JSONArray.fromObject(a_listGoods);
			List<Goods> goodsList = obj.toList(obj, Goods.class);
			//添加固定属性值
			goodspack.setCreatedBy(this.getSuccessUser().getLoginCode());
			goodspack.setCreateTime(new Date());
			goodspack.setLastUpdateTime(new Date());
			try {
				goodsPackService.hl_addGoodsPack(goodspack, goodsList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/goodspack/logingoodspackpage");
		}
	}

	/**
	 * 动态加载商品套餐页面中的商品列表
	 * @param model
	 * @param s_goodsName
	 * @return
	 */
	@RequestMapping(value="/goodslist", produces = {"text/html;charset=UTF-8"})
	public ModelAndView goodslist(Model model, @RequestParam(value="s_goodsName",required=false) String s_goodsName){
		List<Goods> goodsList = null;
		Goods goods = new Goods();
		if(null != s_goodsName && !"".equals(s_goodsName)){
			goods.setGoodsName("%"+SqlTools.sqlTools(s_goodsName)+"%");
		}
		try {
			goodsList = goodsService.getGoosListNoFenYe(goods);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			goodsList = null;
		}
		model.addAttribute("goodsList", goodsList);
		return new ModelAndView("/houTaiGuanLi/goodslist");
	}

	/**
	 * 修改商品套餐状态
	 * @param goodsPack
	 * @return
	 */
	@RequestMapping(value="/updategoodspackstate",method= RequestMethod.POST)
	public Object updategoodspackstate(@RequestParam(value="state") String goodsPack){
		String st = "";
		if(null == goodsPack){
			st = "nodata";
		}else{
			JSONObject obj = JSONObject.fromObject(goodsPack);
			GoodsPack upGoodsPack = (GoodsPack) obj.toBean(obj,GoodsPack.class);
			upGoodsPack.setLastUpdateTime(new Date());
			try {
				goodsPackService.updateGoodsPack(upGoodsPack);
				st ="success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				st = "failed";
			}
		}
		return st;
	}

	/**
	 * 判断商品套餐编号是否重复
	 * @param goodsPackCode
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/goodspackcodeisexit", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public String goodspackcodeisexit(@RequestParam(value="goodsPackCode",required=false) String goodsPackCode,
								  @RequestParam(value="id",required=false) String id){
		String result = "failed";
		GoodsPack goodsPack = new GoodsPack();
		goodsPack.setGoodsPackCode(goodsPackCode);
		if(!id.equals("-1"))
			goodsPack.setId(Integer.valueOf(id));
		try {
			if(goodsPackService.count(goodsPack) == 0)
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
	 * 删除商品套餐
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletegoodspack",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object deletegoodspack(@RequestParam(value="delId") Integer id){
		String st = "";
		if(null == id){
			st = "nodata";
		}else{
			GoodsPack goodsPack = new GoodsPack();
			goodsPack.setId(id);
			try {
				goodsPackService.hl_deleteGoodsPack(goodsPack);
				st = "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return st;
	}
}
