package com.itheima.bos.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.crm.domain.Customer;

/**  
 * ClassName:SubAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:49:21 <br/>       
 */

public interface SubAreaService {

    void save(SubArea subArea);

    Page<SubArea> findAll(Pageable pageable);
    
    //查找未关联定区的客户
    List<SubArea> findUnassociationSubArea();
    
   

    List<SubArea> findAssociationSubArea(Long fixedAreaId);
    
    //查找关联到指定定区的客户

   
}
  
