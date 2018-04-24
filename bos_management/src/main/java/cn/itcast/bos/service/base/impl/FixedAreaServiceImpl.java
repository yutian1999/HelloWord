package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	
	@Override
	public void save(FixedArea model) {
		fixedAreaRepository.save(model);
	}

	@Override
	public Page<FixedArea> findByPage(Specification<FixedArea> spe, Pageable pageable) {
		return fixedAreaRepository.findAll(spe,pageable);
	}

	@Override
	public void del(String ids) {
		String[] split = ids.split("-");
		for (String id : split) {
			//System.err.println(id.length());
			fixedAreaRepository.del(id);
		}
	}
	
	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	@Override
	public void associationCourier(String id, Integer courierid, Integer taketimeid) {
		
		//此处利用的是持久化对象的自动更新技术
		
		//将快递员关联定区
		FixedArea fixedArea = fixedAreaRepository.findOne(id);
		Courier courier = courierRepository.findOne(courierid);
		//定区和快递员是多对多的关系   外键由定区管理  因此只需建立快递员与定区的关联即可
		fixedArea.getCouriers().add(courier);
		//将上班时间关联快递员  分班管理和快递员是一对多的关系  因此只需关联分班和快递员的关系  即可完成级联保存
		TakeTime takeTime = takeTimeRepository.findOne(taketimeid);
		courier.setTakeTime(takeTime);
	}
	
}
