package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.MenuService;
@Service
@Transactional
public class MenuServiceImpl implements MenuService{
	
	@Autowired
	private MenuRepository menuRepository;

	@Override
	public List<Menu> findAll() {
		return menuRepository.findAll();
	}

	@Override
	public void save(Menu model) {
		if (model.getParentMenu() != null && model.getParentMenu().getId() == 0) {
			model.setParentMenu(null);
		}
		menuRepository.save(model);
	}

	@Override
	public List<Menu> findByUser() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		if ("admin".equals(user.getUsername())) {
			return menuRepository.findAll();
		}
		
		return menuRepository.findByUser(user.getId());
	}

}
