package com.itheima.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.take_delivery.Promotion;

/**  
 * ClassName:PromotionService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月31日 下午4:30:53 <br/>       
 */
public interface PromotionService {

    void save(Promotion promotion);

    Page<Promotion> findAll(Pageable pageable);
    
}
  
