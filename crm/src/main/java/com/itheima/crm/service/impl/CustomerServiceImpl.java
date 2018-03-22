package com.itheima.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;


/**  
 * ClassName:CustomerServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:52:51 <br/>       
 */
@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerrepository;
    
    @Override
    public List<Customer> findAll() {
       
        return customerrepository.findAll();
    }

    @Override
    public List<Customer> findUnassociationCustomer() {
        
        return customerrepository.findByFixedAreaIdIsNull();
    }

    @Override
    public List<Customer> findAssociationCustomer(String fixedAreaId) {
          
        return customerrepository.findByFixedAreaId(fixedAreaId);
    }

    @Override
    public void assignCustomers2FixedArea(String fixedAreaId, Long[] subAreaIds) {
          
        if(StringUtils.isNotEmpty(fixedAreaId)){
            //先根据定区id把所有定区的客户全部解绑
            customerrepository.unBindByFixedAreaId(fixedAreaId);
            //再把需要进行绑定的客户进行定区绑定
            if(subAreaIds != null && subAreaIds.length>0){
                for (Long id : subAreaIds) {
                    customerrepository.BindByFixedAreaById(fixedAreaId,id);
                }
                
            }
        }
    }

    @Override
    public void noCustomers2FixedArea(String fixedAreaId) {
          
        customerrepository.unBindByFixedAreaId(fixedAreaId);
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerrepository.save(customer);
    }

    @Override
    public Customer findCustomer(String telephone) {
        
        return customerrepository.findByTelephone(telephone);
    }

    @Override
    public void active(String telephone) {
          
        customerrepository.active(telephone);
    }

    @Override
    public Customer login(String telephone, String password) {
        
        return customerrepository.findByTelephoneAndPassword(telephone,password);
    }
    
    


}
  
