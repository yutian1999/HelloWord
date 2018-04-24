package cn.itcast.bos.domain.base;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import cn.itcast.bos.domain.take_delivery.Promotion;

@XmlRootElement(name="pageBean")
@XmlSeeAlso(Promotion.class)
public class PageBean<T> {
	
	private long totalcount;
	private List<T> pageData;
	
	public long getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(long totalcount) {
		this.totalcount = totalcount;
	}
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	
}
