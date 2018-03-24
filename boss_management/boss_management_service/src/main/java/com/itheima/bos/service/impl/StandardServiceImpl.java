package com.itheima.bos.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.StandardService;

/**  
 * ClassName:StandardServiceRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午2:54:00 <br/>       
 */
@Service
@Transactional

public class StandardServiceImpl implements StandardService {

    @Autowired
    private StandardRepository standardRepository;
    

//    保存
    @Override
    public void save(Standard standard) {
        standardRepository.save(standard);
    }


@Override
public Page<Standard> findAll(Pageable pageable) {
    
    return standardRepository.findAll(pageable);
}


@Override
public void deleteById(String ids) {
      
  if(StringUtils.isNotEmpty(ids)){
      String[] split = ids.split(",");
      for (String id : split) {
//          standardRepository.deleteById(Long.parseLong(id));
          standardRepository.delete(Long.parseLong(id));
      }
  }
}
    
    

}
  
