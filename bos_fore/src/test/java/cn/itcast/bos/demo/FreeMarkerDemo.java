package cn.itcast.bos.demo;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerDemo {
	
	@Test
	public void demo1(){
		//获取模板位置
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		try {
			configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/template"));
			//加载模板
			Template template = configuration.getTemplate("hello.ftl");
			//动态数据对象
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("title", "黑马程序员");
			parameterMap.put("msg", "hello everyone");
			//合并输出
			template.process(parameterMap, new PrintWriter(System.out));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
