package org.sl.service;

import org.sl.pojo.LiuYan;

import java.util.List;

public interface LiuYanService {


	/**
	 * 获取留言集合
	 * @param liuYan
	 * @return
	 * @throws Exception
	 */
	public List<LiuYan> getLiuYanList(LiuYan liuYan) throws Exception;

	/**
	 * 根据id获取留言信息
	 * @param liuYan
	 * @return
	 * @throws Exception
	 */
	public LiuYan getLiuYanById(LiuYan liuYan) throws Exception;

	/**
	 * 获取留言数量
	 * @param liuYan
	 * @return
	 * @throws Exception
	 */
	public int getCount(LiuYan liuYan) throws Exception;

	/**
	 * 添加留言
	 * @param liuYan
	 * @return
	 * @throws Exception
	 */
	public int addLiuYan(LiuYan liuYan) throws Exception;

	/**
	 * 修改留言
	 * @param liuYan
	 * @return
	 * @throws Exception
	 */
	public int updateLiuYan(LiuYan liuYan) throws Exception;

	/**
	 * 删除留言
	 * @param liuYan
	 * @return
	 * @throws Exception
	 */
	public int deleteLiuYan(LiuYan liuYan) throws Exception;
}
