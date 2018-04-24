package cn.itcast.bos.web.action.base;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.base.WayBillService;
import cn.itcast.bos.utils.FileUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ReportAction extends BaseAction<WayBill>{
	
	@Autowired
	private WayBillService  wayBillService;
	
	@Autowired
	private DataSource dataSource;
	
	//导出pdf格式 基于jasper
		@Action(value="report")
		public String ireport() throws Exception{
			//通过查询方法查询出所有的所有的运单,并导出
			List<WayBill> ws = wayBillService.find(model);
			// 读取 jrxml 文件
			String jrxml = ServletActionContext.getServletContext().getRealPath("/WEB-INF/jasper/waybill.jrxml");
			// 准备需要数据
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("company", "传智播客");
			// 准备需要数据
			JasperReport report = JasperCompileManager.compileReport(jrxml);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(ws));

			HttpServletResponse response = ServletActionContext.getResponse();
			OutputStream ouputStream = response.getOutputStream();
			// 设置相应参数，以附件形式保存PDF
			response.setContentType("application/pdf");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + FileUtils.encodeDownloadFilename("工作单.pdf",
					ServletActionContext.getRequest().getHeader("user-agent")));
			// 使用JRPdfExproter导出器导出pdf
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
			exporter.exportReport();// 导出
			ouputStream.close();// 关闭流
			return NONE;
		}
	
	//导出pdf格式
	@Action(value="report_exportPdf")
	public String exportPfd() throws IOException, DocumentException{
		//通过查询方法查询出所有的所有的运单,并导出
		List<WayBill> ws = wayBillService.find(model);
		//创建pdf文档
		ServletActionContext.getResponse().setContentType("application/pdf");
		String filename = "运单管理.pdf";
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().
		setHeader("Content-disposition", "attachment;filename="+filename);
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);
		document.open();
		//生成七个单元格为一行
		Table table = new Table(7);
		table.setWidth(80);
		table.setBorder(1);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
		
		//设置表格格式
		BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false) ;
		Font font = new Font(baseFont,10,Font.NORMAL,Color.BLUE);
		//设置表头
		table.addCell(buildCell("运单号",font));
		table.addCell(buildCell("寄件人",font));
		table.addCell(buildCell("寄件人电话",font));
		table.addCell(buildCell("寄件人地址",font));
		table.addCell(buildCell("收件人",font));
		table.addCell(buildCell("收件人电话",font));
		table.addCell(buildCell("收件人地址",font));
		//添加文档数据
		for (WayBill wayBill : ws) {
			table.addCell(buildCell(wayBill.getWayBillNum(),font));
			table.addCell(buildCell(wayBill.getSendName(),font));
			table.addCell(buildCell(wayBill.getSendMobile(),font));
			table.addCell(buildCell(wayBill.getSendAddress(),font));
			table.addCell(buildCell(wayBill.getRecName(),font));
			table.addCell(buildCell(wayBill.getRecMobile(),font));
			table.addCell(buildCell(wayBill.getRecAddress(),font));
		}
		//将表格添加到文档
		document.add(table);
		document.close();
		return NONE;
	}
	
	private Cell buildCell(String content, Font font) throws BadElementException {
		Phrase phrase = new Phrase(content, font);
		return new Cell(phrase);
	}

	

	@Action(value="report_exportExcel")
	public String exportExcel() throws IOException{
		//通过查询方法查询出所有的所有的运单,并导出
		List<WayBill> ws = wayBillService.find(model);
		//创建表格
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创新建一个sheet
		HSSFSheet sheet = workbook.createSheet("运单管理");
		HSSFRow headSheet = sheet.createRow(0);
		headSheet.createCell(0).setCellValue("运单号");
		headSheet.createCell(1).setCellValue("寄件人");
		headSheet.createCell(2).setCellValue("寄件电话");
		headSheet.createCell(3).setCellValue("寄件地址");
		headSheet.createCell(4).setCellValue("收件人");
		headSheet.createCell(5).setCellValue("收件人电话");
		headSheet.createCell(6).setCellValue("收件人地址");
		
		for (WayBill wayBill : ws) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
			dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
			dataRow.createCell(1).setCellValue(wayBill.getSendName());
			dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
			dataRow.createCell(3).setCellValue(wayBill.getSendAddress());
			dataRow.createCell(4).setCellValue(wayBill.getRecName());
			dataRow.createCell(5).setCellValue(wayBill.getRecMobile());
			dataRow.createCell(6).setCellValue(wayBill.getRecAddress());
		}
		
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
		String filename = "运单管理.xls";
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().
		setHeader("Content-disposition", "attachment;filename="+filename);
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return NONE;
	}
}
