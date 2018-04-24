package com.itcast.bos.web.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


public abstract class BaseAction <T> extends ActionSupport implements ModelDriven<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//重构这里应该是模型驱动封装  由于使用反射技术 并不需要实例化对象
	public T model;
	
	public BaseAction(){
		//获得子类对象的类型
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		//向下转型成
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		@SuppressWarnings("unchecked")
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//分页重构
	public Integer page;
	public Integer rows;

	public void setPage(Integer page) {
		this.page = page;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	
	public void pushPageDataToValueStack(Page<T> pageData){
		Map<String,Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(map);
	}
	
	@Override
	public T getModel() {
		return model;
	}

}
