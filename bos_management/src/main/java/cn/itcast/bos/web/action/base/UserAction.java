package cn.itcast.bos.web.action.base;



import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.UserService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User>{
	
	@Autowired
	private UserService userService;
	
	private String[] roleIds;
	
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	//保存用户
	@Action(value="user_save",
			results={@Result(name="success",type="redirect",location="/pages/system/userlist.html")})
	public String save(){
		userService.save(model,roleIds);
		return SUCCESS;
	}
	
	//用户列表
	@Action(value="user_list",results={@Result(name="success",type="json")})
	public String findAll(){
		List<User> us = userService.findAll();
		ServletActionContext.getContext().getValueStack().push(us);
		return SUCCESS;
	}
	
	//退出登录
	@Action(value="user_logout",results={
			@Result(name="success",type="redirect",location="login.html")})
	public String Logout(){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return SUCCESS;
	}
	
	
	@Action(value="user_login",results={
			@Result(name="login",type="redirect",location="login.html"),
			@Result(name="success",type="redirect",location="index.html")})
	public String login(){
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		try{
			subject.login(token);
			return SUCCESS;
		}catch(AuthenticationException e){
			e.printStackTrace();
			return LOGIN;
		}
	}
}
