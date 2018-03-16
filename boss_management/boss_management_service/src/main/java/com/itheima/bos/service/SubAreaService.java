package com.itheima.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:49:21 <br/>       
 */

public interface SubAreaService {

    void save(SubArea subArea);

    Page<SubArea> findAll(Pageable pageable);

   
}
  
