package org.sl.dao;

import org.sl.pojo.User;

import java.util.List;


public interface UserMapper {

	/**
	 * 获取User对象
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User getUser(User user) throws Exception;

	/**
	 * 根据loginCode获取User对象
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int getUserByName(User user) throws Exception;

	/**
	 * 修改User对象
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int updateUser(User user) throws Exception;

	/**
	 * 获取用户总数量
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int count(User user) throws Exception;

	/**
	 * 获取分页用户列表
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserList(User user) throws Exception;

	/**
	 * 添加用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int addUser(User user) throws Exception;

	/**
	 * 删除用户文件路径
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int delUserPic(User user) throws Exception;

	/**
	 * 根据id获取用户信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User getUserById(User user) throws Exception;

	/**
	 * 删除用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int delUser(User user) throws Exception;

	/**
	 * 根据roleId修改roleName
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int updateUserRoleNameByRoleId(User user) throws Exception;

	/**
	 * 根据roleId查询所有用户列表
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserListByRoleId(User user) throws Exception;

	/**
	 * 查询所有注册会员
	 * @return
	 * @throws Exception
	 */
	public List<User> getZhuCeUser() throws Exception;
}
