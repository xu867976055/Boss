package com.itheima.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.CustomerRepository;
import com.itheima.bos.service.CustomerSerivce;

/**  
 * ClassName:CustomerSerivceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月20日 下午6:53:25 <br/>       
 */
@Transactional
@Service
public class CustomerSerivceImpl implements CustomerSerivce {

    @Autowired
    private CustomerRepository customerRepository;
    
}
  
