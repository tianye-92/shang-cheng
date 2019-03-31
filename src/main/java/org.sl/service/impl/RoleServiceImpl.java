package org.sl.service.impl;

import org.sl.dao.RoleMapper;
import org.sl.dao.UserMapper;
import org.sl.pojo.Role;
import org.sl.pojo.User;
import org.sl.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleMapper roleMapper;
	@Resource
	private UserMapper userMapper;

	@Override
	public List<Role> getRoleList() throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.getRoleList();
	}

	@Override
	public List<Role> getRoleIsStart() throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.getRoleIsStart();
	}

	@Override
	public Role getRoleByCodeAndName(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.getRoleByCodeAndName(role);
	}

	@Override
	public Role getRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.getRole(role);
	}

	@Override
	public int addRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.addRole(role);
	}

	@Override
	public int deleteRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		return roleMapper.deleteRole(role);
	}

	@Override
	public boolean hl_updateRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		roleMapper.updateRole(role);
		int roleId = role.getId();
		String roleName = role.getRoleName();
		User user = new User();
		user.setRoleId(roleId);
		user.setRoleName(roleName);
		if (roleName != null && !roleName.equals("")) {
			// 使用当前修改的角色信息，去修改当前所有用户的角色信息，如果失败，回滚到修改角色前
			userMapper.updateUserRoleNameByRoleId(user);
		}
		return true;
	}

}
