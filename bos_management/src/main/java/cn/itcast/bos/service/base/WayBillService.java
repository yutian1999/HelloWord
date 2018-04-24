package cn.itcast.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillService {

	void save(WayBill model);

	Page<WayBill> find(WayBill wayBill,Pageable pageable);

	WayBill findByWayBillNum(String wayBillNum);
	
	public void synIndex();

	List<WayBill> find(WayBill model);
}
