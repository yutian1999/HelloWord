package cn.itcast.bos.service.base;

import java.util.List;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

public interface RolesService {

	List<Role> findByUser(User user);

	List<Role> findAll();

	void save(Role model, String menuIds, String[] permissionIds);

}
