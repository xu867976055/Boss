package com.itheima.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.itheima.crm.domain.Customer;

/**  
 * ClassName:CustomerService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:52:24 <br/>       
 */

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CustomerService {
    @GET
    @Path("/findAll")
    public List<Customer> findAll();
    
    //查找未关联定区的客户
    @GET
    @Path("/findUnassociationCustomer")
    List<Customer> findUnassociationCustomer();
    
    //查找关联到指定定区的客户
    @GET
    @Path("/findAssociationCustomer")
    List<Customer> findAssociationCustomer(@QueryParam("fixedAreaId")String fixedAreaId);
    
// 定区ID,要关联的数据
// 根据定区ID,把关联到这个定区的所有客户全部解绑
// 要关联的数据和定区Id进行绑定
    @PUT
    @Path("/assignCustomers2FixedArea")
    public void assignCustomers2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId,@QueryParam("customerIds") Long[] customerIds);
   
    @PUT
    @Path("/noCustomers2FixedArea")
    public void noCustomers2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);
    
    @POST
    @Path("/saveCustomer")
    public void saveCustomer(Customer customer);
    
    //校验手机号
    @GET
    @Path("/findCustomer")
    public Customer findCustomer(@QueryParam("telephone") String telephone);
    
    //激活
    @PUT
    @Path("/active")
    public void active(@QueryParam("telephone") String telephone);
    
    //激活
    @GET
    @Path("/login")
    public Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
    
    
    //查找定区id
    @GET
    @Path("/findFixedAreaId")
    public String findFixedAreaId(@QueryParam("address") String address);
  
   
}
  
