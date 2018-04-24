package cn.itcast.bos.web.action.base;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion>{
	
	private File titleImgFile;
	private String titleImgFileFileName;
	
	@Autowired
	private PromotionService promotionService;
	
	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}



	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
	
	private String ids;
	
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;


	@Action(value="promotion_delete",results={@Result(name="success",location="/pages/take_delivery/promotion.html",type="redirect")})
	public String delete(){
		promotionService.del(ids);
		jmsTemplate.send("bos_del", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(ids);
				return textMessage;
			}
		});
		return SUCCESS;
	}
	
	@Action(value="promotion_findByPage",results={@Result(name="success",type="json")})
	public String findByPage(){
		//接受参数
		Pageable pageable = new PageRequest(page - 1, rows);
		//调用业务层去查询
		Page<Promotion> pageData = promotionService.findByPage(pageable);
		//压入值栈
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}


	@Action(value="promotion_save",
			results={@Result(name="success",
			location="/pages/take_delivery/promotion.html",type="redirect")})
	public String save(){
		
		String relpath = ServletActionContext.getServletContext().getRealPath("/upload");
		String filename = UUID.randomUUID() + titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		try {
			//将图片拷贝到工程内
			FileUtils.copyFile(titleImgFile, new File(relpath, filename));
			model.setTitleImg(ServletActionContext.getRequest().getContextPath()+"/upload/"+filename);
			//调用业务层保存宣传活动
			promotionService.save(model);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
}
