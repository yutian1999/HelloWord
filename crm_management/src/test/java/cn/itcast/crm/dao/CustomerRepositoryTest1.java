package cn.itcast.crm.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerRepositoryTest1 {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	//测试没有关联定区的客户
	@Test
	public void test() {
		List<Customer> list = customerRepository.findByFixedAreaIdIsNull();
		System.out.println(list);
	}

	//测试定区关联客户
	@Test
	@Transactional
	@Rollback(false)
	public void test2(){
		customerRepository.associationCustomertoFixedAreaId("db001", 1);
	}
	
	//测试关联定区的客户
	@Test
	public void test3(){
		String list = customerRepository.findByFixedAreaId("db001");
		System.out.println(list);
	}
}
