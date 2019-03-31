package org.sl.dao;

import org.apache.ibatis.annotations.Mapper;
import org.sl.pojo.GongGao;

import java.util.List;

@Mapper
public interface GongGaoMapper {

	/**
	 * 获取所有公告列表
	 * @param gongGao
	 * @return
	 * @throws Exception
	 */
	public List<GongGao> getGongGaoList(GongGao gongGao) throws Exception;

	/**
	 * 获取首页的所有公告列表
	 * @param gongGao
	 * @return
	 * @throws Exception
	 */
	public List<GongGao> getShouYeGongGaoList(GongGao gongGao) throws Exception;

	/**
	 * 添加公告
	 * @param gongGao
	 * @return
	 * @throws Exception
	 */
	public int addGongGao(GongGao gongGao) throws Exception;

	/**
	 * 修改公告
	 * @param gongGao
	 * @return
	 * @throws Exception
	 */
	public int updateGongGao(GongGao gongGao) throws Exception;

	/**
	 * 删除公告
	 * @param gongGao
	 * @return
	 * @throws Exception
	 */
	public int deleteGongGao(GongGao gongGao) throws Exception;

	/**
	 * 根据id获取公告信息
	 * @param gongGao
	 * @return
	 * @throws Exception
	 */
	public GongGao getGongGaoById(GongGao gongGao) throws Exception;

	/**
	 * 获取所有公告数量
	 * @param gongGao
	 * @return
	 * @throws Exception
	 */
	public int getCount(GongGao gongGao) throws Exception;

	/**
	 * 获取首页公告数量
	 * @param gongGao
	 * @return
	 * @throws Exception
	 */
	public int getShouYeCount(GongGao gongGao) throws Exception;
}
