package org.sl.service;

import org.sl.pojo.HuiFu;

import java.util.List;

public interface HuiFuService {

	/**
	 * 获取回复集合
	 * @param huiFu
	 * @return
	 * @throws Exception
	 */
	public List<HuiFu> getHuiFuList(HuiFu huiFu) throws Exception;

	/**
	 * 根据条件获取回复数量
	 * @param huiFu
	 * @return
	 * @throws Exception
	 */
	public int getCount(HuiFu huiFu) throws Exception;

	/**
	 * 删除回复
	 * @param huiFu
	 * @return
	 * @throws Exception
	 */
	public int deleteHuiFu(HuiFu huiFu) throws Exception;

	/**
	 * 添加回复
	 * @param huiFu
	 * @return
	 * @throws Exception
	 */
	public int addHuiFu(HuiFu huiFu) throws Exception;
}
