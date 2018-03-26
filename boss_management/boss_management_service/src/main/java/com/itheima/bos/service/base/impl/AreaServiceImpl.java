package com.itheima.bos.service.base.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;

/**  
 * ClassName:AreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午4:43:30 <br/>       
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public void save(List<Area> list) {
          
        areaRepository.save(list);
    }

    @Override
    public Page<Area> findAll(Pageable pageable) {
        
        return areaRepository.findAll(pageable);
    }

    @Override
    public List<Area> findByQ(String q) {
        
        q="%"+q.toUpperCase()+"%";
        return areaRepository.findByQ(q);
    }

    @Override
    public void addArea(Area area) {
        areaRepository.save(area);
    }

    @Override
    public void deleteById(String ids) {
        //逻辑删除，判断是否为空
        if(StringUtils.isNotEmpty(ids)){
            String[] split = ids.split(",");
            for (String id : split) {
                areaRepository.delete(Long.parseLong(id));
            }
        }
    }

    
}
  
