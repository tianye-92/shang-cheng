package org.sl.dao;

import org.sl.pojo.Role;

import java.util.List;

public interface RoleMapper {

	/**
	 * 获取角色列表
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoleList()throws Exception;

	/**
	 * 获取已启用的角色列表
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoleIsStart() throws Exception;

	/**
	 * 根据角色编号和角色名称获取角色列表
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public Role getRoleByCodeAndName(Role role) throws Exception;

	/**
	 * 模糊查询角色信息
	 * @param role
	 * @return
	 */
	public Role getRole(Role role) throws Exception;

	/**
	 * 添加角色
	 * @param role
	 * @return
	 */
	public int addRole(Role role) throws Exception;

	/**
	 * 模糊修改角色
	 * @param role
	 * @return
	 */
	public int updateRole(Role role) throws Exception;

	/**
	 * 删除角色
	 * @param role
	 * @return
	 */
	public int deleteRole(Role role) throws Exception;
}
