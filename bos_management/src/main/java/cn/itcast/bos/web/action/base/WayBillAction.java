package cn.itcast.bos.web.action.base;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.base.WayBillService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill>{
	
	private static final Logger logger = Logger.getLogger(WayBillAction.class);
	
	@Autowired
	private WayBillService wayBillService;
	
	//运单数据回显
	@Action(value="WayBill_findByWayBillNum",results={@Result(name="success",type="json")})
	public String findByWayBillNum(){
		WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
		Map<String, Object> result = new HashMap<>();
		
		if(wayBill != null){
			//可以回显
			result.put("success", true);
			result.put("wayBill", wayBill);
		}else{
			//表单重置
			result.put("success", false);
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	//分页查询
	@Action(value="wayBill_page",results={@Result(name="success",type="json")})
	public String Query(){
		Pageable pageable = new PageRequest(page -1, rows,new Sort(new Sort.Order(Sort.Direction.DESC, "wid")));
		Page<WayBill> page = wayBillService.find(model,pageable);
		pushPageDataToValueStack(page);
		return SUCCESS;
	}
	//保存运单
	@Action(value="wayBill_save",results={@Result(name="success",type="json")})
	public String save(){
		Map<String, Object> result  = new HashMap<>();
		try{
			System.out.println(model);
			wayBillService.save(model);
			result.put("success", true);
			result.put("msg", "保存成功");
			logger.info("保存成功,运单号为:"+model.getWayBillNum());
		}catch(Exception e){
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存失败");
			logger.error("保存失败:运单号为:"+model.getWayBillNum());
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
}
