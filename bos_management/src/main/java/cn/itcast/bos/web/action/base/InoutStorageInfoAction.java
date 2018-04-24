package cn.itcast.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.service.base.InOutStorageInfoService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class InoutStorageInfoAction extends BaseAction<InOutStorageInfo>{
	
	@Autowired
	private InOutStorageInfoService inOutStorageInfoService;
	
	//属性封装中专单id
	private String transitinfoId;
	
	public void setTransitinfoId(String transitinfoId) {
		this.transitinfoId = transitinfoId;
	}
	
	

	@Action(value="inoutStorageInfo_save",results=
		{@Result(name="success",type="redirect",location="/pages/transit/transitinfo.html")})
	public String InoutStorage_save(){
		inOutStorageInfoService.save(model,transitinfoId);
		return SUCCESS;
	}
}
