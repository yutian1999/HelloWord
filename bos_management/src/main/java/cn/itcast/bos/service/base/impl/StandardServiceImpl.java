package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	@Autowired
	//@Qualifier("simpleJpaRepository")
	private StandardRepository standardRepository;
	@Override
	@CacheEvict(value="standard",allEntries=true)
	public void save(Standard s) {
		standardRepository.save(s);
	}
	@Override
	@Cacheable(value="standard",key="#pageable.pageNumber+'_'+#pageable.pageSize")
	public Page<Standard> findPage(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}
	@Override
	@Cacheable("standard")
	public List<Standard> findAll() {
		return standardRepository.findAll();
	}
	
}
