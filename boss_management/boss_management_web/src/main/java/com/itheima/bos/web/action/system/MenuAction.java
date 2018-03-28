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

import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.service.system.MenuService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:MenuAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午5:59:15 <br/>       
 */

@Namespace("/")
@Controller
@Scope("prototype")
@ParentPackage(value="struts-default")
public class MenuAction extends CommonAction<Menu>{

    @Autowired
    private MenuService menuService;
    
    public MenuAction() {
        super(Menu.class);  
    }

    @Action(value="menuAction_findParentMenu")
    public String findParentMenu() throws IOException{
        List<Menu> list = menuService.findParentMenu();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles","childrenMenus","parentMenu"});
        list2json(list, jsonConfig);
        return NONE;
    }
    
    
    @Action(value="menuAction_save", results = {@Result(name = "success", location = "/pages/system/menu.html", type = "redirect") })
    public String save() throws IOException{
        menuService.save(getModel());
        return SUCCESS;
    }
    
    @Action(value="menuAction_findByPage")
    public String findByPage() throws IOException{
        
        Pageable pageable = new PageRequest(Integer.parseInt(model.getPage())-1, rows);
        Page<Menu> page = menuService.findByPage(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles","childrenMenus","parentMenu"});
        
        page2json(page, jsonConfig);
        return NONE;
    }
    
}
  
