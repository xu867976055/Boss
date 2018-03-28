package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:RoleAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午9:38:46 <br/>       
 */

@Namespace("/")
@Controller
@Scope("prototype")
@ParentPackage(value="struts-default")
public class RoleAction extends CommonAction<Role>{

    @Autowired
    private RoleService roleService;
    
    public RoleAction() {
        super(Role.class);  
    }

    @Action(value="roleAction_findByPage")
    public String findByPage() throws IOException{
        
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Role> page = roleService.findByPage(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"users","permissions","menus"});
        
        page2json(page, jsonConfig);
        return NONE;
    }
    
    //使用属性驱动获取菜单和权限的id
    private String menuIds;
    private Long[] permissionIds;
    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }
    public void setPermissionIds(Long[] permissionIds) {
        this.permissionIds = permissionIds;
    }
    
    @Action(value="roleAction_save", results = {@Result(name = "success", location = "/pages/system/role.html", type = "redirect") })
    public String save() throws IOException{
       
        roleService.save(menuIds,permissionIds,getModel());
        return SUCCESS;
    }
   
}
  
