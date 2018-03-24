package com.itheima.bos.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.CourierService;

/**  
 * ClassName:CourierServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午7:41:01 <br/>       
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;


    @Override
    public void save(Courier courier) {
          courierRepository.save(courier);
    }


    @Override
    public Page<Courier> findAll(Pageable pageable) {
          
        return courierRepository.findAll(pageable);
    }


    @Override
    public void deleteById(String ids) {
          
//        逻辑删除，判断是否为空
        if(StringUtils.isNotEmpty(ids)){
            String[] split = ids.split(",");
            for (String id : split) {
                courierRepository.delete(Long.parseLong(id));
            }
        }
    }


    //有条件分页查询
    @Override
    public Page<Courier> findAll(Specification<Courier> specification, Pageable pageable) {
          
        return courierRepository.findAll(specification, pageable);
    }


    @Override
    public List<Courier> findCourier() {
        
        return courierRepository.findByDeltagIsNull();
    }
    
    

}
  
