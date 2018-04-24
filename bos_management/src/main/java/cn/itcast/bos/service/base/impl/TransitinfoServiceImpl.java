package cn.itcast.bos.service.base.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.TransitinfoRepository;
import cn.itcast.bos.dao.base.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.base.TransitinfoService;
@Service
@Transactional
public class TransitinfoServiceImpl implements TransitinfoService{
	
	@Autowired
	private TransitinfoRepository tansitinfoRepository;
	
	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Override
	public void save(String ids) {
		if(StringUtils.isNotBlank(ids)){
			for (String id : ids.split(",")) {
				WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(id));
				System.out.println(wayBill);
				if (wayBill.getSignStatus() == 1) {
					TransitInfo transitInfo = new TransitInfo();
					transitInfo.setWayBill(wayBill);
					transitInfo.setStatus("出库中专");
					tansitinfoRepository.save(transitInfo);
					wayBill.setSignStatus(2);
				}
			}
		}
		
	}

	@Override
	public Page<TransitInfo> findPage(Pageable pageable) {
		return tansitinfoRepository.findAll(pageable);
	}
	
}
