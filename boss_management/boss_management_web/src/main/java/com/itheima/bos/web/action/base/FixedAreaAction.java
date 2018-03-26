package com.itheima.bos.web.action.base;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import com.itheima.bos.domain.base.Constant;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.crm.domain.Customer;

import net.sf.json.JsonConfig;

/**  
 * ClassName:FixedAreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午2:53:49 <br/>       
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class FixedAreaAction extends CommonAction<FixedArea>{

    @Autowired
    private FixedAreaService fixedareaservice;
    
    public FixedAreaAction() {
        super(FixedArea.class);  
    }

    @Action(value = "fixedAreaAction_save", results = {@Result(name = "save_success",
            location = "/pages/base/fixed_area.html", type = "redirect")})
    public String save(){
        
        fixedareaservice.save(getModel());
        return Constant.SAVE_SUCCESS;
    }
    
   
    @Action(value = "fixedAreaAction_findByPage")
    public String findByPage() throws IOException{
        
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<FixedArea> page = fixedareaservice.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas","couriers"});
        
        page2json(page, jsonConfig);
        return NONE;
    }
    
    //向CRM系统发起请求，查询未关联的定区的客户
    @Action(value="fixedAreaAction_findNoAssociationSelectCustomer")
    public String findUnAssociatedCustomers() throws IOException{
        
        List<Customer> list =  (List<Customer>) WebClient.create("http://localhost:8180/crm/webService/customerService/findUnassociationCustomer")
        .type(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .getCollection(Customer.class);
        
        list2json(list, null);
        
        return NONE;
    }
    
  //向CRM系统发起请求，查询已关联的定区的客户
    @Action(value="fixedAreaAction_findAssociationSelectCustomer")
    public String findAssociatedCustomers() throws IOException{
        
        List<Customer> list =  (List<Customer>) WebClient.create("http://localhost:8180/crm/webService/customerService/findAssociationCustomer")
        .query("fixedAreaId", getModel().getId())
        .type(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .getCollection(Customer.class);
        list2json(list, null);
        return NONE;
    }
    
    
    
   //需要获得传递过来的定区id和客户id
    private Long[] customerIds;
    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }
    
    
    //向Crm系统发送关联请求,关联客户到指定区域
    @Action(value="fixedAreaAction_assignCustomers2FixedArea",results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String assignCustomers2FixedArea(){
        if(customerIds != null){
            WebClient.create("http://localhost:8180/crm/webService/customerService/assignCustomers2FixedArea")
            .query("customerIds", customerIds)
            .query("fixedAreaId", getModel().getId())
            .type(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .put(null);
        }else{
            WebClient.create("http://localhost:8180/crm/webService/customerService/noCustomers2FixedArea")
            .query("fixedAreaId", getModel().getId())
            .type(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .put(null);
        }
       
        return SUCCESS;
    }
    
    
    private Long courierId;
    private Long takeTimeId;
    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }
    public void setTakeTimeId(Long takeTimeId) {
        this.takeTimeId = takeTimeId;
    }
    
    @Action(value="fixedAreaAction_associationFixedAreaToCourier",results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String associationFixedAreaToCourier(){
        
        
        fixedareaservice.associationFixedAreaToCourier(getModel().getId(),courierId,takeTimeId);
        return SUCCESS;
    }
    
    
    
    
    //关联分区
    //需要获得传递过来的定区id和分区id
     private Long[] subAreaIds;
     public void setSubAreaIds(Long[] subAreaIds) {
         this.subAreaIds = subAreaIds;
     }
     
     
     //向Crm系统发送关联请求,关联客户到指定区域
     @Action(value="fixedAreaAction_assignSubArea2FixedArea",results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
     public String assignSubArea2FixedArea(){
         if(subAreaIds != null){
             fixedareaservice.assignSubArea2FixedArea(subAreaIds,model.getId());
         }else{
             fixedareaservice.noAssignSubArea2FixedArea(model.getId());
         }
        
         return SUCCESS;
     }
     
     
    
}
    
    


  
