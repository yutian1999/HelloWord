package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.DeliveryRepository;
import cn.itcast.bos.dao.base.TransitinfoRepository;
import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.base.DeliveryService;
@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService{
	
	@Autowired
	private DeliveryRepository deliveryRepository;
	@Autowired
	private TransitinfoRepository transitinfoRepository;

	@Override
	public void save(DeliveryInfo model, String transitinfoId) {
		//保存配送单
		deliveryRepository.save(model);
		
		TransitInfo transitInfo = transitinfoRepository.findOne(Integer.parseInt(transitinfoId));
		transitInfo.setDeliveryInfo(model);
		transitInfo.setStatus("开始配送");
	}

}
