package com.itheima.bos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.crm.domain.Customer;

/**  
 * ClassName:CustomerRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月20日 下午6:54:25 <br/>       
 */
public interface CustomerRepository extends JpaRepository<Customer , Long>{

}
  
