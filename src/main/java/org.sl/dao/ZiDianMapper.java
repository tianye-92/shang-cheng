package org.sl.dao;

import org.sl.pojo.ZiDian;

import java.util.List;


public interface ZiDianMapper {

	/**
	 * 获取字典集合
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public List<ZiDian> getZiDianList(ZiDian ziDian) throws Exception;

	/**
	 * 获取不重复的字典集合
	 * @return
	 * @throws Exception
	 */
	public List<ZiDian> getOnlyZiDianList() throws Exception;

	/**
	 * 查询不包含注册会员的用户类型
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public List<ZiDian> getUserTypeNoZhuCe(ZiDian ziDian) throws Exception;

	/**
	 * 判断是否是重复名称
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public int exis(ZiDian ziDian) throws Exception;

	/**
	 * 判断是否是重复名称
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public int exisByValueName(ZiDian ziDian) throws Exception;

	/**
	 * 修改数据字典
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public int updateZiDian(ZiDian ziDian) throws Exception;

	/**
	 * 修改数据字典类型
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public int updateZiDianType(ZiDian ziDian) throws Exception;

	/**
	 * 添加数据字典信息
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public int addZiDian(ZiDian ziDian) throws Exception;

	/**
	 * 判断当前数据字典某个类型的valueId最大值
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public int getMaxValueId(ZiDian ziDian) throws Exception;

	/**
	 * 删除数据字典信息
	 * @param ziDian
	 * @return
	 * @throws Exception
	 */
	public int deleteZiDian(ZiDian ziDian) throws Exception;
}
