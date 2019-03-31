package org.sl.dao;

import org.sl.pojo.ZiXun;

import java.util.List;


public interface ZiXunMapper {
	/**
	 * 获取所有咨询集合
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public List<ZiXun> getZiXunList(ZiXun ziXun) throws Exception;
	/**
	 * 获取所有资讯集合，分页显示
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public List<ZiXun> getZiXunListFenYe(ZiXun ziXun) throws Exception;
	/**
	 * 根据Id获取资讯信息
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public ZiXun getZiXunById(ZiXun ziXun) throws Exception;
	/**
	 * 添加资讯
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public int addZiXun(ZiXun ziXun) throws Exception;
	/**
	 * 修改资讯
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public int updateZiXun(ZiXun ziXun) throws Exception;
	/**
	 * 修改资讯上传文件信息
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public int updateZiXunFileInfo(ZiXun ziXun) throws Exception;
	/**
	 * 删除资讯
	 * @param affiche
	 * @return
	 * @throws Exception
	 */
	public int deleteZiXun(ZiXun ziXun) throws Exception;
	/**
	 * 获取咨询数量
	 * @return
	 * @throws Exception
	 */
	public int getCount(ZiXun ziXun) throws Exception;
}
