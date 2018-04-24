package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;

@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area>{
	
	
	
	private File file;
	@Autowired
	private AreaService areaService;
	
	public void setFile(File file) {
		this.file = file;
	}
	private int page;
	private int rows;
	
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	//封装删除所需的id
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	//删除地区
	@Action(value="area_del",results={@Result(name="success",type="redirect",location="/pages/base/area.html")})
	public String del(){
		areaService.del(ids);
		return SUCCESS;
	}
	
	//添加地区
	@Action(value="area_save",results={@Result(name="success",location="/pages/base/area.html",type="redirect")})
	public String save(){
		areaService.save(model);
		return SUCCESS;
	}
	
	@Action(value="area_findByPage",results={@Result(name="success",type="json")})
	public String findAll(){
		Specification<Area> spe = new Specification<Area>() {
			List<Predicate> list = new ArrayList<>();
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(StringUtils.isNoneBlank(model.getProvince())){
					Predicate p1 = cb.like(root.get("province").as(String.class), 
							"%"+model.getProvince()+"%");
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCity())){
					Predicate p2 = cb.like(root.get("city").as(String.class), 
							"%"+model.getCity()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(model.getDistrict())){
					Predicate p3 = cb.like(root.get("district").as(String.class), 
							"%"+model.getDistrict()+"%");
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Area> pageData = areaService.findAll(spe,pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	//一键导入
	@Action(value="area_importBatch")
	public String importBatch() throws FileNotFoundException, IOException{
		HSSFWorkbook hssfWorkBook = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = hssfWorkBook.getSheetAt(0);
		List<Area> areas = new ArrayList();
		int num = sheet.getLastRowNum();
		System.out.println(num);
		for (Row row : sheet) {
			if(row.getRowNum() == 0){
				//跳过第一行
				continue;
			}
			if(row.getCell(0)==null || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
				//跳过最后行
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			String province = area.getProvince();//省
			String city = area.getCity();//市
			String district = area.getDistrict();//区
			province = province.substring(0, province.length()-1);
			city = city.substring(0, city.length()-1);
			district = district.substring(0, district.length()-1);
			String[] headArray = PinYin4jUtils.getHeadByString(province+city+district);//简码
			StringBuffer sb = new StringBuffer();
			for (String string : headArray) {
				sb.append(string);
			}
			String shortCode = sb.toString();
			area.setShortcode(shortCode);//简码
			String citycode = PinYin4jUtils.hanziToPinyin(city,"");
			area.setCitycode(citycode);
			areas.add(area);
		}
		System.out.println(file);
		System.out.println(areas);
		areaService.save(areas);
		return NONE;
	}
}
