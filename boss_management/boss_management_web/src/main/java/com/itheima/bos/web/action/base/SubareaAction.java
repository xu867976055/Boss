package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
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

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Constant;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.FixedAreaService;
import com.itheima.bos.service.SubAreaService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:SubareaAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:44:50 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class SubareaAction extends CommonAction<SubArea>{

    @Autowired
    private SubAreaService subAreaService;
    
    public SubareaAction() {
        super(SubArea.class);  
    }
    
    @Action(value="subareaAction_save",results={@Result(name="save_success",location="/pages/base/sub_area.html",type="redirect")})
    public String save(){
        
        subAreaService.save(getModel());
        return Constant.SAVE_SUCCESS;
        
    }

    
    @Action(value="subareaAction_findByPage")
    public String findByPage() throws IOException{
        
        Pageable pageable = new PageRequest(page-1, rows);
        Page<SubArea> page = subAreaService.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas","couriers"});
        
        page2json(page, jsonConfig);
        return NONE;
    }
    
    
    //查询未关联的定区的分区
    @Action(value="fixedAreaAction_findNoAssociationSelectSubArea")
    public String findNoAssociationSelectSubArea() throws IOException{
        
        List<SubArea> list =  subAreaService.findUnassociationSubArea();
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});

        list2json(list, jsonConfig);
        
        return NONE;
    }
    
  //查询已关联的定区的分区
    @Action(value="fixedAreaAction_findAssociationSelectSubArea")
    public String findAssociationSelectSubArea() throws IOException{
        List<SubArea> list =  subAreaService.findAssociationSubArea(model.getId());
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas","couriers"});
        
        list2json(list, jsonConfig);
        return NONE;
    }
    

    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    @Action(value="subAreaAction_delete",results={@Result(name="delete_by_id_success",location="/pages/base/sub_area.html",type="redirect")})
    public String delete(){
        subAreaService.deleteById(ids);
        return Constant.DELETE_BY_ID_SUCCESS;
    }
    
    
  
}
  
