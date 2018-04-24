package com.itcast.bos.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order>{
	
	//属性封装寄件地址和收货地址
	private String sendAreaInfo;
	private String recAreaInfo;
	
	
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}


	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}


	@Action(value="order_save",results={
			@Result(name="login",type="redirect",location="/login.html"),
			@Result(name="success",type="redirect",location="/index.html#/myhome")})
	public String save(){
		String[] sendAreaData = sendAreaInfo.split("/");
		String[] recAreaData= recAreaInfo.split("/");
		//封装寄件地址
		Area sendArea = new Area();
		sendArea.setProvince(sendAreaData[0]);
		sendArea.setCity(sendAreaData[1]);
		sendArea.setDistrict(sendAreaData[2]);
		//封装收件地址
		Area recArea = new Area();
		recArea.setProvince(recAreaData[0]);
		recArea.setCity(recAreaData[1]);
		recArea.setDistrict(recAreaData[2]);
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		if(customer == null){
			return LOGIN;
		}
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		model.setCustomer_id(customer.getId());
		//System.out.println(model);
		WebClient.create("http://localhost:8080/services/orderService/saveorder")
		.type(MediaType.APPLICATION_JSON).post(model);
		return SUCCESS;
	}
}
