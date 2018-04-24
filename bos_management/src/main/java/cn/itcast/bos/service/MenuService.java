package cn.itcast.bos.service;

import java.util.List;

import cn.itcast.bos.domain.system.Menu;

public interface MenuService {

	List<Menu> findAll();

	void save(Menu model);

	List<Menu> findByUser();

}
