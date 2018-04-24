package cn.itcast.bos.service.base;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> findByPage(Pageable pageable);
	
	@GET
	@Path("/queryPromotion")
	@Produces({"application/json","application/xml"})
	public PageBean<Promotion> queryPageBean(@QueryParam("page") int page,@QueryParam("rows") int rows);
	
	@GET
	@Path("/promotion_detail")
	@Produces({"application/json","application/xml"})
	public Promotion findbyId(@QueryParam("id")Integer id);

	void updateOutDate(Date date);

	void del(String ids);
}
