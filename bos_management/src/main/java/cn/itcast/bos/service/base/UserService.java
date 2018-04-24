package cn.itcast.bos.service.base;

import java.util.List;

import cn.itcast.bos.domain.system.User;

public interface UserService {

	User findByUserName(String username);

	List<User> findAll();

	void save(User model, String[] roleIds);

}
