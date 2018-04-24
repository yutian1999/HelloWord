package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.transit.DeliveryInfo;

public interface DeliveryService {

	void save(DeliveryInfo model, String transitinfoId);
	
}
