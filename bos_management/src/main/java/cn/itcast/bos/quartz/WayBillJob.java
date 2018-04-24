package cn.itcast.bos.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.service.base.WayBillService;

public class WayBillJob implements Job{
	
	@Autowired
	private WayBillService wayBillService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("定时同步索引");
		wayBillService.synIndex();
	}

}
