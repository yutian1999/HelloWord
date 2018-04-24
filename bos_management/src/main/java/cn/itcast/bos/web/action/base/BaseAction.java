package cn.itcast.bos.web.action.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	protected T model;
	
	@Override
	public T getModel() {
		return model;
	}

	public BaseAction() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();//获得子类的类型
		ParameterizedType type = (ParameterizedType) genericSuperclass;//向下转换成参数类型
		Class<T> modelClass = (Class<T>) type.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	protected int page;
	protected int rows;
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	protected void pushPageDataToValueStack(Page pageData){
		Map<String,Object> cs = new HashMap<String,Object>();
		cs.put("total",pageData.getTotalElements());
		cs.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(cs);
	}
	
}
