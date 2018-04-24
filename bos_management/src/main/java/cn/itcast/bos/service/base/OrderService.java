package cn.itcast.bos.service.base;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import cn.itcast.bos.domain.take_delivery.Order;


public interface OrderService {
	
	@Path("/saveorder")
	@POST
	@Consumes({"application/json","application/xml"})
	@Produces({"application/json","application/xml"})
	public void save(Order order);

	public Order findByorderNum(String orderNum);
}
