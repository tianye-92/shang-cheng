package org.sl.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.sl.pojo.ZiDian;
import org.sl.service.ZiDianService;
import org.sl.util.Statics;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 数据字典控制器
 * @author ty
 *
 */
@Controller
@RequestMapping(value="/zidian")
public class ZiDianController extends BaseController {

	@Resource
	private ZiDianService ziDianService;

	/**
	 * 进入数据字典页面
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/loginZiDianPage")
	public ModelAndView loginZiDianPage(HttpSession session, Model model){
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			List<ZiDian> ziDianList = null;
			try {
				ziDianList = ziDianService.getOnlyZiDianList();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ziDianList = null;
			}
			model.addAttribute("ziDianList",ziDianList);
			model.addAllAttributes(baseModel);
			return new ModelAndView("/houTaiGuanLi/shujuzidian");
		}
	}

	/**
	 * 获取具体数据字典
	 * @param session
	 * @param model
	 * @param tcode
	 * @return
	 */
	@RequestMapping(value="/getSubZiDian",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String getSubZiDian(@RequestParam(value="tcode") String tcode){
		String st = "";
		if(null == tcode || "".equals(tcode)){
			st = "nodata";
		}else{
			List<ZiDian> ziDianList = null;
			try {
				ziDianList = ziDianService.getZiDianList(new ZiDian(tcode));
				JSONArray jsonArray = JSONArray.fromObject(ziDianList);
				st = jsonArray.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				st = "failed";
			}
		}
		return st;
	}

	/**
	 * 修改数据字典信息
	 * @param dicJson
	 * @return
	 */
	@RequestMapping(value="/updatezidian",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object updatezidian(@RequestParam(value="dicJson") String dicJson){
		String st = "";
		if(null == dicJson || "".equals(dicJson)){
			st = "nodata";
		}else{
			JSONObject obj = JSONObject.fromObject(dicJson);
			ZiDian ziDian = (ZiDian) obj.toBean(obj, ZiDian.class);
			try {
				if(ziDianService.exisByValueName(ziDian)>0){
					st = "rename";
				}else{
					ziDianService.updateZiDian(ziDian);
					st = "success";
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
	 * 修改数据字典类型
	 * @param dicJson
	 * @return
	 */
	@RequestMapping(value="/updatezidiantype",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object updatezidiantype(@RequestParam(value="dicJson") String dicJson){
		String st = "";
		if(null == dicJson || "".equals(dicJson)){
			st = "nodata";
		}else{
			JSONObject obj = JSONObject.fromObject(dicJson);
			ZiDian ziDian = (ZiDian) obj.toBean(obj, ZiDian.class);
			try {
				if(ziDianService.exis(ziDian)>0){
					st = "rename";
				}else{
					ziDianService.updateZiDianType(ziDian);
					st = "success";
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
	 * 添加数据字典
	 * @param dicJson
	 * @return
	 */
	@RequestMapping(value="/addZiDian",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object addZiDian(@RequestParam(value="dic") String dic){
		String st = "";
		if(null == dic || "".equals(dic)){
			st = "nodata";
		}else{
			JSONObject obj = JSONObject.fromObject(dic);
			ZiDian ziDian = (ZiDian) obj.toBean(obj, ZiDian.class);
			try {
				if(ziDianService.exis(ziDian)>0){
					st = "rename";
				}else{
					ziDianService.addZiDian(ziDian);
					st = "success";
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
	 * 添加数据字典信息
	 * @param dicJson
	 * @return
	 */
	@RequestMapping(value="/addSubZiDian",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object addSubZiDian(@RequestParam(value="dic") String dic){
		String st = "";
		if(null == dic || "".equals(dic)){
			st = "nodata";
		}else{
			JSONObject obj = JSONObject.fromObject(dic);
			ZiDian ziDian = (ZiDian) obj.toBean(obj, ZiDian.class);
			try {
				if(ziDianService.exisByValueName(ziDian)>0){
					st = "rename";
				}else{
					//查询当前类型的valueId最大值是多少，然后+1放进当前对象
					ziDian.setValueId(ziDianService.getMaxValueId(ziDian)+1);
					ziDianService.addZiDian(ziDian);
					st = "success";
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
	 * 删除数据字典信息
	 * @param dicJson
	 * @return
	 */
	@RequestMapping(value="/deleteZiDian",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object deleteZiDian(@RequestParam(value="dic") String dic){
		String st = "";
		if(null == dic || "".equals(dic)){
			st = "nodata";
		}else{
			JSONObject obj = JSONObject.fromObject(dic);
			ZiDian ziDian = (ZiDian) obj.toBean(obj, ZiDian.class);
			try {
				ziDianService.deleteZiDian(ziDian);
				st = "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				st = "failed";
			}
		}
		return st;
	}
}
