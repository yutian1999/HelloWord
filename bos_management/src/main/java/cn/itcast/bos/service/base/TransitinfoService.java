package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.transit.TransitInfo;

public interface TransitinfoService {

	void save(String ids);

	Page<TransitInfo> findPage(Pageable pageable);
	
}
