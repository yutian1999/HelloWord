package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea>{
	@Autowired
	private FixedAreaService fixedAreaService;
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	//属性驱动封装数据customerids  option中带过来的id
	private String [] customerIds;
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	
	private Integer courierId;
	private Integer takeTimeId;
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	//定区关联快递员
	@Action(value="fixedArea_associationCourierToFixedArea",
			results={@Result(name="success",type="redirect",location="/pages/base/fixed_area.html")})
	public String associationCourier(){
		fixedAreaService.associationCourier(model.getId(),courierId,takeTimeId);
		return SUCCESS;
	}
	
	//将客户关联到定区
	@Action(value="fixedArea_associationCustomers",
			results={@Result(name="success",
			location="/pages/base/fixed_area.html")})
	public String associationCustomer(){
		//System.out.println(customerIds);
		String ids = StringUtils.join(customerIds,",");
		//System.err.println(ids);
		WebClient.
		create("http://localhost:8081/services/customerService/associationCustomerstoFixedArea?customerIdStr="+ids+"&fixedAreaId="+model.getId()).
		type(MediaType.APPLICATION_JSON).put(null);
		
		return SUCCESS;
	}
	
	

	//查询关联未关联定区的客户
	@Action(value="fixedArea_noAssociationCustomers",
			results={@Result(name="success",type="json")})
	public String noAssociationCustomers(){
		//请求webService
		Collection<? extends Customer> collection = WebClient.
				create("http://localhost:8081/services/customerService/noassociationCustomers").
		accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		//数据压入栈顶
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	
	//查询关联定区的客户
	@Action(value="fixedArea_hasAssociationCustomers",
			results={@Result(name="success",type="json")})
	public String hasAssociationCustomers(){
		Collection<? extends Customer> collection = WebClient.
		create("http://localhost:8081/services/customerService/hasassociationCustomers/"+model.getId()).
		type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	//删除定区
	@Action(value="fixedAera_dele",
			results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
	public String delete(){
		System.err.println(ids.length());
		String id = ServletActionContext.getRequest().getParameter("ids");
		System.out.println(id.length());
		fixedAreaService.del(ids);
		return SUCCESS;
	}
	
	//分页查询
	@Action(value="fixedarea_findByPage",results={@Result(name="success",type="json")})
	public String findBypage(){
		Specification<FixedArea> spe = new Specification<FixedArea>() {
			
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				//条件一:定区编号
				if(StringUtils.isNotBlank(model.getId())){
					Predicate p1 = cb.equal(root.get("id").as(String.class), 
							model.getId());
					list.add(p1);
				}
				//条件二 :所属公司
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					list.add(p2);
				}
				//条件三:分区
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Pageable pageable = new PageRequest(page-1, rows);
		Page<FixedArea> pageData = fixedAreaService.findByPage(spe,pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	//保存分区信息
	@Action(value="fixedarea_save",
			results={
					@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
	public String save(){
		fixedAreaService.save(model);
		return SUCCESS;
	}
}
