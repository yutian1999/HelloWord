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

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.base.PermissionService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission>{
	
	@Autowired
	private PermissionService permissionService;
	
	//保存权限
	@Action(value="permission_save",results={@Result(name="success",type="redirect",location="/pages/system/permission.html")})
	public String save(){
		permissionService.save(model);
		return SUCCESS;
	}
	@Action(value="permission_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Permission> ps = permissionService.findAll();
		ServletActionContext.getContext().getValueStack().push(ps);
		return SUCCESS;
	}
}
