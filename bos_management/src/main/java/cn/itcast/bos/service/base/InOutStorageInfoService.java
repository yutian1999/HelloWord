package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoService {

	void save(InOutStorageInfo model, String transitinfoId);

}
