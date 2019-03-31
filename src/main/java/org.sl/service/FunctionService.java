package org.sl.service;

import org.apache.ibatis.annotations.Param;
import org.sl.pojo.Function;
import org.sl.pojo.QuanXian;

import java.util.List;

public interface FunctionService {

	/**
	 * 获取主菜单
	 * @param quanXian
	 * @return
	 * @throws Exception
	 */
	public List<Function> getZhuCaiDan(QuanXian quanXian) throws Exception;

	/**
	 * 获取子菜单
	 * @param function
	 * @return
	 * @throws Exception
	 */
	public List<Function> getZiCaiDan(Function function) throws Exception;

	/**
	 * 获取主菜单
	 * @param function
	 * @return
	 * @throws Exception
	 */
	public List<Function> getZhuFuncList(Function function) throws Exception;

	/**
	 * 根据角色id获取功能集合
	 * @param function
	 * @return
	 * @throws Exception
	 */
	public List<Function> getFunctionListByRoId(QuanXian quanXian) throws Exception;

	/**
	 * getFunctionListByIn
	 * @param sqlInString
	 * @return
	 * @throws Exception
	 */
	public List<Function> getFunctionListByIn(@Param(value = "sqlInString") String sqlInString) throws Exception;
}
