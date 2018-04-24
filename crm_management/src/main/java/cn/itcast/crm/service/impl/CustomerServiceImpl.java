package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public List<Customer> findnoAssociationCustomers() {
		return customerRepository.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findhasAssociationCustomers(String fixedAreaId) {
		return customerRepository.findFixedAreaId(fixedAreaId);
	}
	
	//将用户关联到定区
	@Override
	public void associationCustomertoFixedArea(String customerIdStr, String fixedAreaId) {
		
		customerRepository.clearAssociation(fixedAreaId);

		
		if(StringUtils.isBlank(customerIdStr) &&  "null".equals(customerIdStr)){
			return;
		}
		//解除关联
		String[] ids = customerIdStr.split(",");
		for (String idStr : ids) {
				if(StringUtils.isNotBlank(idStr) && !"null".equals(idStr)){
					System.out.println(idStr);
					int id = Integer.parseInt(idStr);
					customerRepository.associationCustomertoFixedAreaId(fixedAreaId,id);
				}
			}
	}

	@Override
	public void register(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return customerRepository.findByTelephone(telephone);
	}

	@Override
	public void updateCustomerType(String telephone) {
		customerRepository.updateCustomerType(telephone);
	}

	@Override
	public Customer login(String telephong, String password) {
		return customerRepository.login(telephong,password);
	}

	@Override
	public String findFixedAreaIdByAddress(String address) {
		System.out.println(customerRepository.findByFixedAreaId(address));
		return customerRepository.findByFixedAreaId(address);
	}

}
