package com.itheima.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Courier;

/**  
 * ClassName:CourierService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午7:40:13 <br/>       
 */
public interface CourierService {


    void save(Courier courier);

    Page<Courier> findAll(Pageable pageable);

    void deleteById(String ids);

}
  
