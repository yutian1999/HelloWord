package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.InOutStorageInfoRepository;
import cn.itcast.bos.dao.base.TransitinfoRepository;
import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.base.InOutStorageInfoService;
@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService{
	
	@Autowired
	private InOutStorageInfoRepository inOutStorageInfoRepository;
	@Autowired
	private TransitinfoRepository transitinfoRepository;
	
	@Override
	public void save(InOutStorageInfo model, String transitinfoId) {
		//第一步保存出入库信息
		inOutStorageInfoRepository.save(model);
		//关联出入库信息到中专单
		TransitInfo transitInfo = transitinfoRepository.findOne(Integer.parseInt(transitinfoId));
		transitInfo.getInOutStorageInfos().add(model);
		
		if (model.getOperation().equals("到达网点")) {
			transitInfo.setOutletAddress(model.getAddress());
			transitInfo.setStatus("到达网点");
		}
	}

}
