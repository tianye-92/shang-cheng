package org.sl.service.impl;

import org.sl.dao.UserMapper;
import org.sl.pojo.User;
import org.sl.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;

	@Override
	public User getUser(User user) throws Exception {
		return userMapper.getUser(user);
	}

	@Override
	public int getUserByName(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserByName(user);
	}

	@Override
	public int updateUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.updateUser(user);
	}

	@Override
	public int count(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.count(user);
	}

	@Override
	public List<User> getUserList(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserList(user);
	}

	@Override
	public int addUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.addUser(user);
	}

	@Override
	public int delUserPic(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.delUserPic(user);
	}

	@Override
	public User getUserById(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserById(user);
	}

	@Override
	public int delUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.delUser(user);
	}

	@Override
	public int updateUserRoleNameByRoleId(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.updateUserRoleNameByRoleId(user);
	}

	@Override
	public List<User> getUserListByRoleId(User user) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserListByRoleId(user);
	}

	@Override
	public List<User> getZhuCeUser() throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getZhuCeUser();
	}

}
