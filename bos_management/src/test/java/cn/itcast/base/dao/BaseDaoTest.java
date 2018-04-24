package cn.itcast.base.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.web.action.base.BaseAction;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class BaseDaoTest {
	@Autowired
	private StandardRepository standardRepository;
	@Test
	public void findByNameTest(){
		//List<Standard> name = standardRepository.findByName("10-20");
		List<Standard> name = standardRepository.queryName2("10-20");
		System.err.println(name);
	}
	@Test
	@Transactional
	@Rollback(false)
	public void updateTest(){
		standardRepository.updateMinLength(1, 10);
	}
	
	@Test
	public void test2(){
		//BaseAction ba = new BaseAction();
		try {
			Class<?> clazz = Class.forName("cn.itcast.bos.web.action.base.BaseAction<T>");
			try {
				Object newInstance = clazz.newInstance();
				System.out.println(newInstance);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
