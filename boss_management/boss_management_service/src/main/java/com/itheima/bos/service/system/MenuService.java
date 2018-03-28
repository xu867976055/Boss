package com.itheima.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Menu;

/**  
 * ClassName:MenuService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午6:01:28 <br/>       
 */
public interface MenuService {

    List<Menu> findParentMenu();

    void save(Menu model);

    Page<Menu> findByPage(Pageable pageable);

}
  
