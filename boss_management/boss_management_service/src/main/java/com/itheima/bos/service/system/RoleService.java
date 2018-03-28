package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Role;

/**  
 * ClassName:RoleService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午9:37:37 <br/>       
 */
public interface RoleService {

    Page<Role> findAll(Pageable pageable);

    void save(String menuIds, Long[] permissionIds, Role model);

}
  
