package cn.itcast.bos.service.base.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	@Autowired
	private CourierRepository courierRepository;
	@Override
	@RequiresPermissions("courier_add")
	public void save(Courier courier) {
		courierRepository.save(courier);
	}
	@Override
	public Page<Courier> findByPage(Specification<Courier> spe,Pageable pageable) {
		return courierRepository.findAll(spe,pageable);
	}
	@Override
	public void updatedelight(String ids,String flag) {
		if(flag.equals("toDel")){
			String[] id = ids.split(",");
			for (String s : id) {
				int sid = Integer.parseInt(s);
				courierRepository.updateDelight(sid);
			}
		}else{
			String[] id = ids.split(",");
			for (String s : id) {
				int sid = Integer.parseInt(s);
				courierRepository.update(sid);
			}
		}
			
	}
	@Override
	public List<Courier> findAll() {
		return courierRepository.findName();
	}
	@Override
	public List<Courier> findnoassociation() {
		Specification<Courier> spe = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p1 = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				Predicate p2 = cb.equal(root.get("deltag").as(Character.class),'0');
				
				return cb.and(p1,p2);
			}
		};
		return courierRepository.findAll(spe);
	}
	
}
