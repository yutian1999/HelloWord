package cn.itcast.bos.web.action.base;

import java.util.List;

import org.apache.shiro.web.util.SavedRequest;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.base.RolesService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role>{
	
	@Autowired
	private RolesService roleService;
	
	private String menuIds;
	private String[] permissionids;
	
	
	
	
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}


	
	
	public void setPermissionids(String[] permissionids) {
		this.permissionids = permissionids;
	}




	@Action(value="role_save",results={@Result(name="success",type="redirect",location="/pages/system/role.html")})
	public String save(){
		roleService.save(model,menuIds,permissionids);
		return SUCCESS;
	}

	@Action(value="role_list",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Role> rs = roleService.findAll();
		ServletActionContext.getContext().getValueStack().push(rs);
		return SUCCESS;
	}
}
