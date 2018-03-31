package com.itheima.bos.service.take_delivery.impl;

import javax.crypto.Mac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.PromotionRepository;
import com.itheima.bos.domain.take_delivery.PageBean;
import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.bos.service.take_delivery.PromotionService;

/**  
 * ClassName:PromotionService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月31日 下午4:31:05 <br/>       
 */

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public void save(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    @Override
    public Page<Promotion> findAll(Pageable pageable) {
      
        return  promotionRepository.findAll(pageable);
    }

    @Override
    public PageBean<Promotion> findByPage(int pageIndex, int pageSize) {
          
        //查询数据
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        
        Page<Promotion> p = promotionRepository.findAll(pageable);
        
        //将查询结果封装给自定义PageBean
        PageBean<Promotion> pageBean = new PageBean<>();
        pageBean.setList(p.getContent());
        pageBean.setTotal(p.getTotalElements());
        
        return pageBean;
    }
    
    
    
}
  
