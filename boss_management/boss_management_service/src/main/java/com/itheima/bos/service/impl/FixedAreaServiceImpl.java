package com.itheima.bos.service.impl;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.domain.base.SubArea;
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
    @Autowired
    private SubAreaRepository subAreaRepository;
    
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

    //关联分区到定区
    @Override
    public void assignSubArea2FixedArea(Long[] subAreaIds, Long fixedAreaId) {
        FixedArea fixedArea = fixedareaRepository.findOne(fixedAreaId);
        // 关系是由分区在维护
        // 先解绑，把当前定区绑定的所有分区全部解绑
        Set<SubArea> subareas = fixedArea.getSubareas();
        for (SubArea subArea : subareas) {
            subArea.setFixedArea(null);
        }
        
        //再绑定
        for (Long subAreaId : subAreaIds) {
            SubArea subArea = subAreaRepository.findOne(subAreaId);
            subArea.setFixedArea(fixedArea);
        }
    }

    @Override
    public void noAssignSubArea2FixedArea(Long fixedAreaId) {
        FixedArea fixedArea = fixedareaRepository.findOne(fixedAreaId);
        // 关系是由分区在维护
        // 全解绑
        Set<SubArea> subareas = fixedArea.getSubareas();
        for (SubArea subArea : subareas) {
            subArea.setFixedArea(null);
        }
    }

}
  
