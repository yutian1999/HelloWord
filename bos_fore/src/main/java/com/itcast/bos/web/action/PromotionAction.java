package com.itcast.bos.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.PageBean;
import cn.itcast.bos.domain.constants.Constant;
import cn.itcast.bos.domain.take_delivery.Promotion;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion>{
	
	//查询促销详情
	@Action(value="promotion_detail")
	public String showDetail() throws IOException, TemplateException{
		String htmlRealpath = ServletActionContext.getServletContext().getRealPath("/freeMark");
		File htmlFile = new File(htmlRealpath+"/"+model.getId()+".html");
		if(!htmlFile.exists()){
			//模板不存在
			//获取模板位置
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
			configuration.setDirectoryForTemplateLoading
			(new File(
					ServletActionContext.getServletContext().
					getRealPath("WEB-INF/freeMark_template")));
			//获取模板对象
			Template template = configuration.getTemplate("promotion_detail.ftl");
			//动态数据  webService
			Promotion promotion = WebClient
			.create("http://localhost:8080/services/promotionService/promotion_detail?id="+model.getId())
			.accept(MediaType.APPLICATION_JSON).get(Promotion.class);
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("promotion", promotion);
			//整合生成静态页面
			template.process(paramMap, new OutputStreamWriter(new FileOutputStream(htmlFile),"utf-8"));
		}
		ServletActionContext.getResponse().setContentType("html/text;charset=utf-8");
		//相应页面
		FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
		return NONE;
	}
	
	//分页显示促销
	@Action(value="promotion_query",results={@Result(name="success",type="json")})
	public String queryPage(){
		
		@SuppressWarnings("unchecked")
		PageBean<Promotion> pageBean = WebClient.
		create(Constant.BOS_MANAGER_URL+"/services/promotionService/queryPromotion?page="+page+"&rows="+rows)
		.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}
}
