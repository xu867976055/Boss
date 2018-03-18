package com.itheima.bos.web.action.base;

import java.io.IOException;

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

import com.itheima.bos.domain.base.Constant;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.service.FixedAreaService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:FixedAreaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午2:53:49 <br/>       
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class FixedAreaAction extends CommonAction<FixedArea>{

    @Autowired
    private FixedAreaService fixedareaservice;
    
    public FixedAreaAction() {
        super(FixedArea.class);  
    }

    @Action(value = "fixedAreaAction_save", results = {@Result(name = "save_success",
            location = "/pages/base/fixed_area.html", type = "redirect")})
    public String save(){
        
        fixedareaservice.save(getModel());
        return Constant.SAVE_SUCCESS;
    }
    
   
    @Action(value = "fixedAreaAction_findByPage")
    public String findByPage() throws IOException{
        
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<FixedArea> page = fixedareaservice.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas","couriers"});
        
        page2json(page, jsonConfig);
        return NONE;
    }

}
  
