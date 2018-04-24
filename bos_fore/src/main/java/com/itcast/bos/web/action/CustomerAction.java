package com.itcast.bos.web.action;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.helpers.MDCKeySetExtractor;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.itcast.bos.utils.MailUtils;

import cn.itcast.crm.domain.Customer;
@SuppressWarnings("serial")
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer>{
	
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;
	
	
	//登录
	@Action(value="customer_login",results={@Result(name="success",type="redirect",location="/index.html#/myhome"),
			@Result(name="login",location="/login.html",type="redirect")})
		public String login(){
		Customer customer = WebClient.
		create("http://localhost:8081/services/customerService/login?telephone="+model.getTelephone()+"&password="+model.getPassword())
		.accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if(customer != null){
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			return SUCCESS;
		}else{
			return "login";
		}
	}
	
	//发送验证码
	@Action(value="customer_sendMsg")
	public String sendMsg(){
		String random = RandomStringUtils.randomNumeric(4);
		//System.err.println("验证码:"+random);
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), random);
		
		final String message = "尊敬的用户您好,欢迎注册速运物流,您的验证码是:"+random;
		
		//代码重构 验证码由消息队列发送
		jmsTemplate.send("bos_sms", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", model.getTelephone());
				mapMessage.setString("msg", message);
				return mapMessage;
			}
		});
		
		return NONE;
	}
	
	private String checkCode;
	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
	//注入redis模板
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	//注册功能
	@Action(value="customer_register",results={
			@Result(name="success",type="redirect",location="signup-success.html"),
			@Result(name="input",type="redirect",location="signup.html")})
	public String register(){
		String sessionCheckCode = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
		if(sessionCheckCode != null && sessionCheckCode.equals(checkCode)){
			//通过校验   可以调用webService去保存用户
			System.out.println("注册成功");
			//生成激活码
			final String activeCode = RandomStringUtils.randomAlphanumeric(16);
			//将激活码存入redis缓冲中
			redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
			//邮件内容
			final String content = "尊敬的用户您好,请于24小时内绑定邮箱:<br/>"
					+ "<a href='"+MailUtils.activeUrl+"?telephone="+model.getTelephone()+"&activeCode="+activeCode+"'>邮箱绑定</a>";
			//将邮件内容发送到消息队列上
			jmsTemplate.send("bos_email",new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("content", content);
					mapMessage.setString("email", model.getEmail());
					mapMessage.setString("activeCode", activeCode);
					return mapMessage;
				}
			});
			//MailUtils.sendMail("速运快递邮箱绑定", content, model.getEmail(), activeCode);
			WebClient.create("http://localhost:8081/services/customerService/register").
			type(MediaType.APPLICATION_JSON).post(model);
		}else{
			System.out.println("短信校验失败");
			//this.addActionError("验证码输入错误");
			return INPUT;
		}
		return SUCCESS;
	}
	
	//邮箱绑定的功能
	private String activeCode;

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	@Action(value="customer_active")
	public String bindEmail() throws IOException{
		//先判断激活码是否有效
		String activeCodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		if(activeCodeRedis == null || !activeCodeRedis.equals(activeCode)){
			//激活码无效
				ServletActionContext.getResponse().getWriter().println("验证码无效");
		}else{
			Customer customer = WebClient.
			create("http://localhost:8081/services/customerService/telephone/"+model.getTelephone()).
			accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if(customer.getType() == null || customer.getType() != 1){
				//未绑定
				WebClient.create("http://localhost:8081/services/customerService/update/"+model.getTelephone()).
				type(MediaType.APPLICATION_JSON).put(null);
					ServletActionContext.getResponse().getWriter().println("绑定成功");
			}else{
					ServletActionContext.getResponse().getWriter().println("您已经绑定过了,请不要重复绑定");
			}
		}
		redisTemplate.delete(model.getTelephone());
		return NONE;
	}

}
