package com.itheima.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.service.system.MenuService;

/**  
 * ClassName:MenuServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午6:01:52 <br/>       
 */

@Transactional
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;
    
    @Override
    public List<Menu> findParentMenu() {
        
        return menuRepository.findByParentMenuIsNull();
    }

    @Override
    public void save(Menu menu) {
          
        //判断添加的是否一级菜单,如果不判断会瞬时态异常，后台会自动new一个parentMenu对象
        Menu parentMenu = menu.getParentMenu();
        if(parentMenu != null && parentMenu.getId() == null){
           menu.setParentMenu(null);
        }
        menuRepository.save(menu);
    }

    @Override
    public Page<Menu> findByPage(Pageable pageable) {
          
        return menuRepository.findAll(pageable);
    }

}
  
