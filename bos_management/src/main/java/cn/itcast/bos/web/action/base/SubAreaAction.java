package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.service.base.SubAreaService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea>{
	
	private File file;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private SubAreaService subAreaService;
	
	
	@Action(value="sub_area_import")
	public String importBatch() throws IOException{
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = workbook.getSheetAt(0);
		List<SubArea> list = new ArrayList<>();
		for (Row row : sheet) {
			
			if(row.getRowNum() == 0){
				continue;
			}
			if(row.getCell(0) == null){
				continue;
			}
			SubArea subArea = new SubArea();
			subArea.setId(row.getCell(0).getStringCellValue());
			subArea.setArea(areaRepository.findOne(row.getCell(1).getStringCellValue()));
			subArea.setKeyWords(row.getCell(3).getStringCellValue());
			subArea.setStartNum(row.getCell(4).getStringCellValue());
			subArea.setEndNum(row.getCell(5).getStringCellValue());
			subArea.setSingle(row.getCell(6).getStringCellValue().charAt(0));
			subArea.setFixedArea(fixedAreaRepository.findOne(row.getCell(7).getStringCellValue()));
			list.add(subArea);
		}
		
		subAreaService.save(list);
		
		return NONE;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
