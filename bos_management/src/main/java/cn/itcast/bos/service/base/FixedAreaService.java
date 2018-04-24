package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

public interface FixedAreaService {

	void save(FixedArea model);

	Page<FixedArea> findByPage(Specification<FixedArea> spe, Pageable pageable);

	void del(String ids);

	void associationCourier(String id, Integer courierid, Integer taketimeid);

}
