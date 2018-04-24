package cn.itcast.bos.web.action.base;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.service.MenuService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu>{
	
	@Autowired
	private MenuService menuService;
	
	//根据用户展示菜单
	@Action(value="menu_showMenu",results={@Result(name="success",type="json")})
	public String showMenu(){
		List<Menu> ms = menuService.findByUser();
		ServletActionContext.getContext().getValueStack().push(ms);
		return SUCCESS;
	}
	
	//添加订单
	@Action(value="menu_save",results={
			@Result(name="success",type="redirect",location="pages/system/menu.html")})
	public String save(){
		menuService.save(model);
		return SUCCESS ;
	}
	
	@Action(value="menu_list",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Menu> ms = menuService.findAll();
		ServletActionContext.getContext().getValueStack().push(ms);
		return SUCCESS;
	}
}
