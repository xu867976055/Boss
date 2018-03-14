package com.itheima.bos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;

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
    
    

}
  
