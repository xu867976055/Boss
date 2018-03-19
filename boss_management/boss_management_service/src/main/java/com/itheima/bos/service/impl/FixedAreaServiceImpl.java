package com.itheima.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.domain.base.TakeTime;
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
    private FixedAreaRepository fixedareaRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    
    @Override
    public void save(FixedArea fixedArea) {
        fixedareaRepository.save(fixedArea);
    }

    @Override
    public Page<FixedArea> findAll(Pageable pageable) {
        
        return fixedareaRepository.findAll(pageable);
    }

    @Override
    public void associationFixedAreaToCourier(Long id, Long courierId, Long takeTimeId) {
       
        FixedArea fixedArea = fixedareaRepository.findOne(id);
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
        
        //关联快递员时间
        courier.setTakeTime(takeTime);
        
        //定区关联快递员
        fixedArea.getCouriers().add(courier);
        
    }

}
  
