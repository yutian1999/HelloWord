package cn.itcast.bos.service.base.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	
	@Autowired
	private AreaRepository areaRepository;
	@Override
	public void save(List<Area> areas) {
		areaRepository.save(areas);
	}
	@Override
	public Page<Area> findAll(Specification<Area> spe, Pageable pageable) {
		return areaRepository.findAll(spe,pageable);
	}
	@Override
	public void save(Area model) {
		if(model.getId() == "" || model.getId() == null|| "".equals(model.getId())){
			model.setId(UUID.randomUUID().toString());
		}
		areaRepository.save(model);
	}
	@Override
	public void del(String ids) {
		String[] split = ids.split(",");
		for (String id : split) {
			areaRepository.del(id);
		}
	}

}
