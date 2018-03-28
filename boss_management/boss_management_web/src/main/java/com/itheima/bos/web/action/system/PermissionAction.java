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
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.PermissionService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:PermissionAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午9:18:11 <br/>       
 */
@Namespace("/")
@Controller
@Scope("prototype")
@ParentPackage(value="struts-default")
public class PermissionAction extends CommonAction<Permission>{

    @Autowired
    private PermissionService permissionService;
    
    public PermissionAction() {
        super(Permission.class);  
    }

    
    @Action(value="permission_findByPage")
    public String findByPage() throws IOException{
        
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Permission> page = permissionService.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles"});
        
        page2json(page, jsonConfig);
        return NONE;
    }
    
    @Action(value="permissionAction_save", results = {@Result(name = "success", location = "/pages/system/permission.html", type = "redirect") })
    public String save() throws IOException{
        permissionService.save(getModel());
        return SUCCESS;
    }
    
    @Action(value="permissionAction_findAll")
    public String findParentMenu() throws IOException{
        
       Page<Permission> page = permissionService.findAll(null);
       List<Permission> list = page.getContent();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles"});
        list2json(list, jsonConfig);
        return NONE;
    }
}
  
