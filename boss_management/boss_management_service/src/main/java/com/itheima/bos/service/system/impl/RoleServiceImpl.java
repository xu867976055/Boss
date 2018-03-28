package com.itheima.bos.service.system.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;

/**  
 * ClassName:RoleServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午9:37:56 <br/>       
 */
@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Page<Role> findAll(Pageable pageable) {
          
        return roleRepository.findAll(pageable);
    }

    @Override
    public void save(String menuIds, Long[] permissionIds, Role role) {
        roleRepository.save(role);
        
        if(StringUtils.isNoneEmpty(menuIds)){
            String[] split = menuIds.split(",");
            for (String menuId : split) {
                Menu menu = new Menu();
                menu.setId(Long.parseLong(menuId));
            }
        }
        
        if(permissionIds != null && permissionIds.length>0){
            for (Long permissionId : permissionIds) {
                Permission permission = new Permission();
                permission.setId(permissionId);
            }
        }
        
    }
    
}
  
