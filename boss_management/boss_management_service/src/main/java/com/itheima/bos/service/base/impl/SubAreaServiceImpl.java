package com.itheima.bos.service.base.impl;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;
import com.itheima.crm.domain.Customer;

/**  
 * ClassName:SubAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:52:00 <br/>       
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

    @Autowired
    private SubAreaRepository subAreaRepository;
   
    @Override
    public void save(SubArea subArea) {

        subAreaRepository.save(subArea);
    }

    @Override
    public Page<SubArea> findAll(Pageable pageable) {
        return subAreaRepository.findAll(pageable);
    }

    @Override
    public List<SubArea> findUnassociationSubArea() {
        
        return subAreaRepository.findByFixedAreaIsNull();
    }

    @Override
    public List<SubArea> findAssociationSubArea(Long fixedAreaId) {
          
        FixedArea fixedArea = new FixedArea();
        fixedArea.setId(fixedAreaId);
        return subAreaRepository.findByFixedArea(fixedArea);
    }

    @Override
    public void deleteById(String ids) {
      //逻辑删除，判断是否为空
        if(StringUtils.isNotEmpty(ids)){
            String[] split = ids.split(",");
            for (String id : split) {
                subAreaRepository.delete(Long.parseLong(id));
            }
        }
    }
    

}
  
