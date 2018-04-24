package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.base.OrderService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order>{
	
	@Autowired
	private OrderService orderService;
	
	@Action(value="order_findByOrderNum",results={@Result(name="success",type="json")})
	public String findByOrderNum(){
		
		Order order = orderService.findByorderNum(model.getOrderNum());
		Map<String, Object> result = new HashMap<>();
		if(order != null){
			result.put("success", true);
			result.put("order",order);
		}else{
			result.put("success", false);
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}