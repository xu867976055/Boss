package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
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
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CourierAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午7:08:03 <br/>       
 */
@Namespace("/")
@Controller
@Scope("prototype")
@ParentPackage(value="struts-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
    
    private Courier model = new Courier();
    
    @Autowired
    private CourierService courierService;
    
    @Override
    public Courier getModel() {
        return model;
    }

    @Action(value="courierAction_save",results={@Result(name="save_success",location="/pages/base/courier.html",type="redirect")})
    public String save(){
        courierService.save(model);
        return Constant.SAVE_SUCCESS;
    }
    
    private int page;
    private int rows;
    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value="courierAction_findByPage")
    public String findByPage() throws IOException{
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Courier> page = courierService.findAll(pageable);
        Long total = page.getTotalElements();
        List<Courier> list = page.getContent();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("rows", list);
    
        // 灵活控制输出的内容
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas","takeTime"});
        
        // 在实际开发的时候,为了提高服务器的性能,把前台页面不需要的数据都应该忽略掉
        String json = JSONObject.fromObject(map,jsonConfig).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        
        return NONE;
    }
    
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    @Action(value="courAction_delete",results={@Result(name="delete_by_id_success",location="/pages/base/courier.html",type="redirect")})
    public String delete(){
        courierService.deleteById(ids);
        return Constant.DELETE_BY_ID_SUCCESS;
    }
}
  
