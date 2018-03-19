package com.itheima.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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

    Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

    List<Courier> findCourier();

}
  
