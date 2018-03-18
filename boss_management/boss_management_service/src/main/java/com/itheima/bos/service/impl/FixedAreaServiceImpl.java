package com.itheima.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.FixedAreaService;

/**  
 * ClassName:FixedAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午2:58:54 <br/>       
 */

@Transactional
@Service
public class FixedAreaServiceImpl implements FixedAreaService{

    @Autowired
    private FixedAreaRepository fixedarearepository;
    
    @Override
    public void save(FixedArea fixedArea) {
        fixedarearepository.save(fixedArea);
    }

    @Override
    public Page<FixedArea> findAll(Pageable pageable) {
        
        return fixedarearepository.findAll(pageable);
    }

}
  
