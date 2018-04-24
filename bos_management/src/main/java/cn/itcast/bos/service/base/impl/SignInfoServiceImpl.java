package cn.itcast.bos.service.base.impl;

import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.mail.handlers.text_html;

import cn.itcast.bos.dao.base.SigninfoRepository;
import cn.itcast.bos.dao.base.TransitinfoRepository;
import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.base.SignInfoService;
@Service 
@Transactional
public class SignInfoServiceImpl implements SignInfoService{
	
	@Autowired
	private  SigninfoRepository signinfoRepository;
	
	@Autowired
	private TransitinfoRepository transitinfoRepository;

	@Override
	public void save(SignInfo model, String transitinfoId) {
		signinfoRepository.save(model);
		TransitInfo transitInfo = transitinfoRepository.findOne(Integer.parseInt(transitinfoId));
		transitInfo.setSignInfo(model);
		
		//更改状态
		if(model.getSignType().equals("正常")){
			transitInfo.setStatus("正常签收");
			transitInfo.getWayBill().setSignStatus(3);
		}else{
			transitInfo.setStatus("异常");
			transitInfo.getWayBill().setSignStatus(4);
		}
	}

}
