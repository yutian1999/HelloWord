package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

public interface CustomerService {
	
	
	//查询定区id
	@GET
	@Path("/findFixedAreaIdByAddress")
	@Produces({"application/xml","application/json"})
	public String findFixedAreaIdByAddress(@QueryParam("address")String address);
	
	//查询所有没有关联定区的客户
	@GET
	@Path("/noassociationCustomers")
	@Produces({"application/xml","application/json"})
	public List<Customer> findnoAssociationCustomers();
	//查询所有已经关联的客户
	@GET
	@Path("/hasassociationCustomers/{fixedAreaId}")
	@Produces({"application/xml","application/json"})
	public List<Customer> findhasAssociationCustomers(@PathParam("fixedAreaId") String fixedAreaId);
	
	//将客户关联到定区上
	@PUT
	@Path("/associationCustomerstoFixedArea")
	@Consumes({"application/xml","application/json"})
	public void associationCustomertoFixedArea(@QueryParam("customerIdStr")String customerIdStr,@QueryParam("fixedAreaId")String fixedAreaId);
	
	//客户注册
	@POST
	@Path("/register")
	@Consumes({"application/xml","application/json"})
	@Produces({"application/xml","application/json"})
	public void register(Customer customer);
	
	//通过电话查询客户
	@GET
	@Path("/telephone/{tel}")
	@Produces({"application/xml","application/json"})
	@Consumes({"application/xml","application/json"})
	public Customer findByTelephone(@PathParam("tel")String telephone);
	
	@PUT
	@Path("/update/{telephone}")
	public void updateCustomerType(@PathParam("telephone")String telephone);
	
	@GET
	@Path("/login")
	@Produces({"application/json","application/json"})
	public Customer login(@QueryParam("telephone")String telephong,@QueryParam("password")String password);
}
