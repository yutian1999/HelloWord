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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.base.TransitinfoService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitinfoAction extends BaseAction<TransitInfo>{
	
	@Autowired
	private TransitinfoService transitinfoService;
	
	private String ids;
	
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	
	//分页查询的方法
	@Action(value="transitinfo_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		Page<TransitInfo> pageData = transitinfoService.findPage(pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	

	@Action(value="transitinfo_create",results={@Result(name="success",type="json")})
	public String createTransitinfo(){
		//System.out.println(ids+".........");
		transitinfoService.save(ids);
		Map<String, Object> result = new HashMap<>();
		try {
			result.put("success", true);
			result.put("msg", "生成中专单成功");
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "生成中转单失败");
			e.printStackTrace();
		}
		ServletActionContext.getContext().getValueStack().push(result);
		return SUCCESS ;
	}
	
}
