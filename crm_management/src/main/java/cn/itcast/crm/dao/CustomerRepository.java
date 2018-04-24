package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.itcast.crm.domain.Customer;
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	public List<Customer> findByFixedAreaIdIsNull();
	
	@Query("from Customer where fixedAreaId = ?")
	public List<Customer> findFixedAreaId(String fixedAreaId);
	
	@Query("update Customer set fixedAreaId=? where id=?")
	@Modifying
	public void associationCustomertoFixedAreaId(String fixedAreaId,int id);
	
	@Query("update Customer set fixedAreaId=null where fixedAreaId=?")
	@Modifying
	public void clearAssociation(String fixedAreaId);
	@Query("update Customer set type=1 where telephone=?")
	@Modifying
	public void updateCustomerType(String telephone);
	
	public Customer findByTelephone(String telephone);
	@Query("from Customer where telephone = ? and password=?")
	public Customer login(String telephong, String password);
	@Query("select fixedAreaId from Customer where address = ?")
	public String findByFixedAreaId(String address);
}
