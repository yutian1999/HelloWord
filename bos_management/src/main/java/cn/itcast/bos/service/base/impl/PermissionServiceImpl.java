package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.PermissionService;
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService{
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Override
	public List<Permission> findByUser(User user) {
		if (user.getUsername().equals("admin")) {
			return permissionRepository.findAll();
		}
		return permissionRepository.findByUser(user.getId());
	}

	@Override
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}

	@Override
	public void save(Permission model) {
		permissionRepository.save(model);
	}

}
