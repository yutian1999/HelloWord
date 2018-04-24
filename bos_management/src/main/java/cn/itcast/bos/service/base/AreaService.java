package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Area;

public interface AreaService {

	void save(List<Area> areas);

	Page<Area> findAll(Specification<Area> spe, Pageable pageable);

	void save(Area model);

	void del(String ids);
	
}
