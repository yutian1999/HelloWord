package cn.itcast.bos.service.base.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.PromotionRepository;
import cn.itcast.bos.domain.base.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
	
	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion model) {
		promotionRepository.save(model);
	}

	@Override
	public Page<Promotion> findByPage(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}

	@Override
	public PageBean<Promotion> queryPageBean(int page, int rows) {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setTotalcount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}

	@Override
	public Promotion findbyId(Integer id) {
		return promotionRepository.findOne(id);
	}

	@Override
	public void updateOutDate(Date date) {
		promotionRepository.update(date);
	}

	@Override
	public void del(String ids) {
		String[] split = ids.split(",");
		for (String id : split) {
			promotionRepository.delete(Integer.parseInt(id));
		}
	}
	
}
