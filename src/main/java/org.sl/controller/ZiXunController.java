package org.sl.controller;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.sl.pojo.UploadTemp;
import org.sl.pojo.User;
import org.sl.pojo.ZiDian;
import org.sl.pojo.ZiXun;
import org.sl.service.UploadTempService;
import org.sl.service.ZiDianService;
import org.sl.service.ZiXunService;
import org.sl.util.HtmlEncode;
import org.sl.util.JsonDateValueProcessor;
import org.sl.util.PageUtil;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 资讯控制器
 * @author ty
 *
 */
@Controller
@RequestMapping(value="/zixun")
public class ZiXunController extends BaseController {

	@Autowired
	private ZiXunService ziXunService;
	@Resource
	private ZiDianService ziDianService;
	@Resource
	private UploadTempService uploadTempService;

	/**
	 * 进入资讯管理页面
	 * @param session
	 * @param model
	 * @param currentpage
	 * @param tiaoZhuanYe
	 * @return
	 */
	@RequestMapping(value="/loginZiXunPage")
	public ModelAndView loginZiXunPage(HttpSession session, Model model,
                                       @RequestParam(value="currentpage",required=false) Integer currentpage,
                                       @RequestParam(value="tiaoZhuanYe",required=false) String tiaoZhuanYe){
		Map<String,Object> baseModel = (Map<String, Object>) session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(null == baseModel){
			return new ModelAndView("redirect:/");
		}else{
			List<ZiXun> ziXunList = null;
			List<ZiDian> ziDianList	= null;
			ZiXun ziXun = new ZiXun();
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
				ziDianList = ziDianService.getZiDianList(new ZiDian("INFO_TYPE"));
				page.setZongShu(ziXunService.getCount(ziXun));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				ziXun.setStarNum((page.getPage()-1)*page.getPageSize());
				ziXun.setPageSize(page.getPageSize());
			try {
				ziXunList = ziXunService.getZiXunListFenYe(ziXun);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ziXunList = null;
			}
				page.setItems(ziXunList);
			}else{
				page.setItems(null);
			}
			model.addAllAttributes(baseModel);
			model.addAttribute("page",page);
			model.addAttribute("ziDianList", ziDianList);
		}
		return new ModelAndView("/XinXiGuanLi/zixun");
	}

	/**
	 * 删除上传文件
	 * @param request
	 * @param session
	 * @param filePath
	 * @return
	 */
	@RequestMapping("/deleteZiXunFile")
	@ResponseBody
	public Object deleteZiXunFile(HttpServletRequest request, HttpSession session, @RequestParam(value="filePath") String filePath){
		if(null == filePath || "".equals(filePath)){
			return "nodata";
		}else{
			try {
				String path = request.getSession().getServletContext().getRealPath("/");
				File file = new File(filePath);
				System.out.println(path+filePath);
				if(file.exists()){
					file.delete();
				}

				ZiXun ziXun = new ZiXun();
				ziXun.setTypeName(filePath);
				ziXun.setFileName("");
				ziXun.setFilePath("#");
				ziXun.setFileSize(0d);
				ziXun.setUploadTime(new Date());
				ziXunService.updateZiXunFileInfo(ziXun);

				UploadTemp uploadTemp = new UploadTemp();
				filePath = filePath.replaceAll("/", File.separator+File.separator);
				uploadTemp.setUploadFilePath(filePath);
				uploadTempService.delete(uploadTemp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failed";
			}
			return "success";
		}
	}

	/**
	 * 查看资讯
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/showInfo", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
	public Object showInfo(HttpSession session, @RequestParam(value="id") Integer id){
		String result = "";
		if(null == id || "".equals(id)){
			result =  "nodata";
		}else{
			try {
				ZiXun ziXun = new ZiXun();
				ziXun.setId(id);
				ziXun = ziXunService.getZiXunById(ziXun);
				if(null != ziXun && ziXun.getTitle() != null){
					ziXun.setTitle(HtmlEncode.htmlDecode(ziXun.getTitle()));
					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
					result =  JSONObject.fromObject(ziXun,jsonConfig).toString();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block

				result =  "failed";
			}
		}
		return result;
	}

	/**
	 * 删除资讯
	 * @param request
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteZiXun")
	@ResponseBody
	public Object deleteZiXun(HttpServletRequest request, HttpSession session, @RequestParam(value="id") Integer id){

		if(null == id || "".equals(id)){
			return "nodata";
		}else{
			try {
				ZiXun ziXun = new ZiXun();
				ziXun.setId(id);
				ZiXun ziXun1 = new ZiXun();
				ziXun1 = ziXunService.getZiXunById(ziXun);
				if(null != ziXun1){
					String path = request.getSession().getServletContext().getRealPath("/");
					ziXun1.setFilePath(ziXun1.getFilePath().replace("/", File.separator+File.separator));
					File file = new File(path + ziXun1.getFilePath());
					if(file.exists()){
						file.delete();
					}
					ziXunService.deleteZiXun(ziXun);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "failed";
			}
			return "success";
		}
	}

	/**
	 * 修改资讯状态
	 * @param session
	 * @param inforState
	 * @return
	 */
	@RequestMapping("/updateZiXunState")
	@ResponseBody
	public Object updateZiXunState(HttpSession session, @RequestParam(value="ziXunState") String ziXunState){
		if(null == ziXunState || "".equals(ziXunState)){
			return "nodata";
		}else{
			JSONObject ziXunObject = JSONObject.fromObject(ziXunState);
			ZiXun ziXun =  (ZiXun)JSONObject.toBean(ziXunObject, ZiXun.class);
			ziXun.setUploadTime(new Date());
			ziXun.setPublisher(((User)session.getAttribute(Statics.SESSION_USER)).getLoginCode());
			try {
				ziXunService.updateZiXun(ziXun);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "failed";
			}
			return "success";
		}
	}

	/**
	 * 上传文件
	 * @param uploadInformationFile
	 * @param uploadInformationFileM
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/upload", produces = {"text/html;charset=UTF-8"})
	@ResponseBody
    public Object upload(@RequestParam(value = "uploadInformationFile", required = false) MultipartFile uploadInformationFile,
                         @RequestParam(value = "uploadInformationFileM", required = false) MultipartFile uploadInformationFileM,
                         HttpServletRequest request, HttpSession session) {

        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"zixunfiles");

        if(uploadInformationFile == null && uploadInformationFileM != null){
        	uploadInformationFile = uploadInformationFileM;
        }

        if(uploadInformationFile != null){
        	String oldFileName = uploadInformationFile.getOriginalFilename();
            String prefix=FilenameUtils.getExtension(oldFileName);
            List<ZiDian> ziDianlist = null;
            ZiDian ziDian = new ZiDian();
            ziDian.setTypeCode("INFOFILE_SIZE");
            try {
            	ziDianlist = ziDianService.getZiDianList(ziDian);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            int filesize = 500000000;
            if(null != ziDianlist){
	            if(ziDianlist.size() == 1){
	            	filesize = Integer.valueOf(ziDianlist.get(0).getValueName());
	            }
            }
            if(uploadInformationFile.getSize() >  filesize){//上传大小不得超过 500M
            	return "1";
            }else{//上传图片格式不正确
            	String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_info."+prefix;
                File targetFile = new File(path, fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                //保存
                try {
                	uploadInformationFile.transferTo(targetFile);
                	//add file info to uploadtemp
                	User user =  ((User)session.getAttribute(Statics.SESSION_USER));
                	UploadTemp uploadTemp = new UploadTemp();
                	uploadTemp.setUploader(user.getLoginCode());
                	uploadTemp.setUploadType("info");
                	uploadTemp.setUploadFilePath(File.separator + "statics" + File.separator + "zixunfiles" + File.separator + fileName );
                	uploadTempService.add(uploadTemp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String url = oldFileName + "[[[]]]" + request.getContextPath()+"/statics/infofiles/"+fileName + "size:"+uploadInformationFile.getSize();
                return url;
            }
        }
        return null;
    }

	/**
	 * 添加资讯
	 * @param information
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/addZiXun",method= RequestMethod.POST)
	public ModelAndView addZiXun(@ModelAttribute("ziXun") ZiXun ziXun, HttpSession session){
		if(session.getAttribute(Statics.SESSION_BASE_MODEL) == null){
			return new ModelAndView("redirect:/");
		}else{
			try {
				User user =  ((User)session.getAttribute(Statics.SESSION_USER));
				ziXun.setPublisher(user.getLoginCode());
				ziXun.setPublishTime(new Date(System.currentTimeMillis()));
				ziXun.setState(1);
				ziXun.setUploadTime(ziXun.getPublishTime());
				if(null != ziXun.getTitle() && !ziXun.getTitle().equals("")){
					ziXun.setTitle(HtmlEncode.htmlEncode(ziXun.getTitle()));
				}

				UploadTemp uploadTemp = new UploadTemp();
            	uploadTemp.setUploader(user.getLoginCode());
            	uploadTemp.setUploadType("info");
            	uploadTemp.setUploadFilePath(ziXun.getFilePath().replaceAll("/", File.separator+File.separator));
            	uploadTempService.delete(uploadTemp);
            	ziXunService.addZiXun(ziXun);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ModelAndView("redirect:/zixun/loginZiXunPage");
	}

	/**
	 * 修改资讯
	 * @param information
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/updateZiXun",method= RequestMethod.POST)
	public ModelAndView updateZiXun(@ModelAttribute("ziXun") ZiXun ziXun, HttpSession session){
		if(session.getAttribute(Statics.SESSION_BASE_MODEL) == null){
			return new ModelAndView("redirect:/");
		}else{
			try {
				User user =  ((User)session.getAttribute(Statics.SESSION_USER));
				ziXun.setPublisher(user.getLoginCode());
				ziXun.setPublishTime(new Date(System.currentTimeMillis()));
				ziXun.setUploadTime(ziXun.getPublishTime());
				if(null != ziXun.getTitle() && !ziXun.getTitle().equals("")){
					ziXun.setTitle(HtmlEncode.htmlEncode(ziXun.getTitle()));
				}
				ziXunService.updateZiXun(ziXun);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ModelAndView("redirect:/zixun/loginZiXunPage");
	}


	/**
	 * 首页more功能
	 * @param session
	 * @param model
	 * @param currentpage
	 * @param tiaoZhuanYe
	 * @return
	 */
	@RequestMapping(value="/shouYeZiXunList")
	public ModelAndView shouYeGongGaoList(HttpSession session, Model model,
                                          @RequestParam(value="currentpage",required=false)Integer currentpage,
                                          @RequestParam(value="tiaoZhuanYe",required=false) String tiaoZhuanYe){
		if(null == session.getAttribute(Statics.SESSION_BASE_MODEL)){
			return new ModelAndView("redirect:/");
		}else{
			loginZiXunPage(session, model, currentpage, tiaoZhuanYe);
			return new ModelAndView("/shouye/zixunlist");
		}
	}

	/**
	 * 首页点击资讯，跳转资讯显示信息页面
	 * @param session
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/shouYeZiXun", produces = {"text/html;charset=UTF-8"})
	public ModelAndView shouYeZiXun(HttpSession session, @RequestParam(value="id",required=false) Integer id, Model model){
		Map<String,Object> baseModel= (Map<String,Object>)session.getAttribute(Statics.SESSION_BASE_MODEL);
		if(baseModel == null){
			return new ModelAndView("redirect:/");
		}else{
			if(null == id || "".equals(id)){
				id = 0;
			}else{
				try {
					ZiXun ziXun = new ZiXun();
					ziXun.setId(id);
					ziXun = ziXunService.getZiXunById(ziXun);
					if(null != ziXun && ziXun.getTitle() != null){
						model.addAttribute("ziXun", ziXun);
					}
				} catch (Exception e) {
				}
			}
		}
		model.addAllAttributes(baseModel);
		return new ModelAndView("/shouye/zixuninfo");
	}
}
