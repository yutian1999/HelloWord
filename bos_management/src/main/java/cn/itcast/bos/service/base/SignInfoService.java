package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.transit.SignInfo;

public interface SignInfoService {

	void save(SignInfo model, String transitinfoId);

}
