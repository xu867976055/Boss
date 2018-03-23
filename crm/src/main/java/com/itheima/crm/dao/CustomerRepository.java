package com.itheima.crm.dao;

import java.util.List;

import org.apache.commons.lang.ObjectUtils.Null;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

/**  
 * ClassName:CustomerRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午5:54:15 <br/>       
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);
    
    @Query("update Customer set fixedAreaId=null where fixedAreaId=?")
    @Modifying
    void unBindByFixedAreaId(String fixedAreaId);

    @Query("update Customer set fixedAreaId=? where id=?")
    @Modifying
    void BindByFixedAreaById(String fixedAreaId, Long customerIds);

    Customer findByTelephone(String telephone);

    @Query("update Customer set type=1 where telephone=?")
    @Modifying
    void active(String telephone);

    Customer findByTelephoneAndPassword(String telephone, String password);

    @Query("select fixedAreaId from Customer where address=?")
    String findFixedAreaId(String address);

}
  
