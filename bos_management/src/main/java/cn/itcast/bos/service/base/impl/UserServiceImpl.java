package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.RolesRepository;
import cn.itcast.bos.dao.base.UserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.UserService;
@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RolesRepository rolesRepository;
	@Override
	public User findByUserName(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void save(User model, String[] roleIds) {
		userRepository.save(model);
		//关联角色
		if (roleIds != null) {
			for (String id : roleIds) {
				Role role = rolesRepository.findOne(Integer.parseInt(id));
				model.getRoles().add(role);
			}
		}
	}

}
