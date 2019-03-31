package org.sl.service;

import org.sl.pojo.QuanXian;

public interface QuanXianService {

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
	 * 添加权限事务
	 * @param ids
	 * @param createdBy
	 * @return
	 * @throws Exception
	 */
	public boolean hl_addAuthority(String[] ids, String createdBy) throws Exception;

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

	/**
	 * 删除权限事务
	 * @param authority
	 * @return
	 * @throws Exception
	 */
	public boolean hl_delAddAuthority(QuanXian quanXian, String checkFuncList) throws Exception;
}
