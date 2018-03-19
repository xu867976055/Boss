package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.TakeTimeService;
import com.itheima.bos.web.action.CommonAction;

/**  
 * ClassName:TakeTimeAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午3:54:53 <br/>       
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class TakeTimeAction extends CommonAction<TakeTime>{

    @Autowired
    private TakeTimeService taketimeservice;
    
    public TakeTimeAction() {
        super(TakeTime.class);  
    }
    
    @Action(value="takeTimeAction_findAll")
    public String findAll() throws IOException{
        
        List<TakeTime> list = taketimeservice.findAll();
        list2json(list, null);
        return NONE;
    }
    
}
  
