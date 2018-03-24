package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午3:12:23 <br/>       
 */
public interface StandardRepository extends JpaRepository<Standard, Long>{

//    @Query("delete from Standard where id= ?")
//    @Modifying
//    void deleteById(long parseLong);

}
  
