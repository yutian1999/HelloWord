package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends BaseAction<Courier>{
	@Autowired
	private CourierService courierService;
	
	
	private String ids;
	
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	//批量还原
	@Action(value="courier_toUse",
			results={@Result(name="success",location="/pages/base/courier.html",type="redirect")})
	public String toUse(){
		courierService.updatedelight(ids,"toUse");
		return SUCCESS;
	}
	
	//查询没有关联定区的快递员
	@Action(value="courier_findnoassociation",results={@Result(name="success",type="json")})
	public String findBynoAssociation(){
		List<Courier> list = courierService.findnoassociation();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	//批量作废快递员
	@Action(value="courier_delBatch",
			results={@Result(name="success",type="redirect",location="/pages/base/courier.html")})
	public String delBatch(){
		courierService.updatedelight(ids,"toDel");
		return SUCCESS; 
	}
	
	//查询所有   
	@Action(value="courier_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Courier> list = courierService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}

	//分页查询
	@Action(value="courier_findByPage",results={@Result(name="success",type="json")})
	public String findByPage(){
		Pageable pageable = new PageRequest(page - 1, rows);
		Specification<Courier> spe = new Specification<Courier>() {
			List<Predicate> list = new ArrayList();
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				if(StringUtils.isNoneBlank(model.getCourierNum())){
					//按快递员工号条件查询
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class),
							model.getCourierNum());
					list.add(p1);
				}
				
				if(StringUtils.isNoneBlank(model.getCompany())){
					//按公司模糊查询
					Predicate p2 = cb.like(root.get("company").as(String.class),
							"%"+model.getCompany()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(model.getType())){
					//按快递类型
					Predicate p3 = cb.equal(root.get("type").as(String.class), 
							model.getType());
					list.add(p3);
				}
				
				//按快递员是否在职类型
				if(model.getDeltag() != null){
					Predicate p= cb.equal(root.get("deltag").as(Character.class), 
							model.getDeltag());
					list.add(p);
				}
				
				
				//多表查询
				Join<Object,Object> standardRoot = root.join("standard",JoinType.INNER);
				if(model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())){
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class),
							"%"+model.getStandard().getName()+"%");
					list.add(p4);
				}
				return cb.and(list.toArray(new Predicate[0]));
				
			}
		};
		Page<Courier> pageData = courierService.findByPage(spe,pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	//保存快递员
	@Action(value="courier_save",results={@Result(name="success",location="/pages/base/courier.html")})
	public String save(){
		courierService.save(model);
		//System.err.println(courier);
		return SUCCESS;
	}
	
	
}
