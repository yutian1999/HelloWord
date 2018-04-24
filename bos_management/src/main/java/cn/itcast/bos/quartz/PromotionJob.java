package cn.itcast.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.base.PromotionService;
import cn.itcast.bos.service.base.WayBillService;

public class PromotionJob implements Job{
	
	@Autowired
	private PromotionService promotionService;
	

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("定期清理过期促销.........");
		promotionService.updateOutDate(new Date());
	}
	
}
