package cn.itcast.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.service.base.SignInfoService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SignInfoAction extends BaseAction<SignInfo>{
	
	@Autowired
	private SignInfoService signInfoService;
	
	private String transitinfoId;
	
	
	public void setTransitinfoId(String transitinfoId) {
		this.transitinfoId = transitinfoId;
	}


	@Action(value="sign_save",results={@Result(name="success",type="redirect",location="/pages/transit/transitinfo.html")})
	public String save(){
		signInfoService.save(model,transitinfoId);
		return SUCCESS;
	}
}
