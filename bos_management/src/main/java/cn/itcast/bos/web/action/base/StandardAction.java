package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends BaseAction<Standard>{
	@Autowired
	private StandardService standardService;
	//保存收派标准
	@Action(value="standard_save",
			results={
					@Result(name="success",location="/pages/base/standard.html",type="redirect")})
	public String save(){
		standardService.save(model);
		return "success";
	}
	
	//分页查询收派标准
	@Action(value="standard_findPage",results={@Result(name="success",type="json")})
	public String findByPage(){
		//默认从第0页开始
		Pageable pageable = new PageRequest(page -1, rows);
		Page<Standard> pageData = standardService.findPage(pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	//查询所有取派标准  显示快递员的下拉列表
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Standard> ss = standardService.findAll();
		ActionContext.getContext().getValueStack().push(ss);
		return SUCCESS;
	}
	
}
