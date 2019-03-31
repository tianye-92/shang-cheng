package org.sl.dao;

import org.sl.pojo.QuanXian;

public interface QuanXianMapper {

	/**
	 * 根据roleId和functionId获取权限列表
	 * @param quanXian
	 * @return
	 * @throws Exception
	 */
	public int getQuanXianCount(QuanXian quanXian) throws Exception;

	/**
	 * 添加权限
	 * @param quanXian
	 * @return
	 * @throws Exception
	 */
	public int addQuanXian(QuanXian quanXian) throws Exception;

	/**
	 * 修改权限
	 * @param quanXian
	 * @return
	 * @throws Exception
	 */
	public int updateQuanXian(QuanXian quanXian) throws Exception;

	/**
	 * 删除权限
	 * @param quanXian
	 * @return
	 * @throws Exception
	 */
	public int deleteQuanXian(QuanXian quanXian) throws Exception;
}
