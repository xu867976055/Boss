package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:CommonAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月15日 下午8:15:20 <br/>       
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T>{

    protected T model;
    
    public CommonAction(Class<T> clazz){
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
              
            e.printStackTrace();  
            
        }
    }
    @Override
    public T getModel() {
          
        return model;
    }
    
    //使用属性驱动获取分页数据
    protected int page;//第几页
    protected int rows;//一页显示几行
    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    
//    把page对象转json数组
    public void page2json(Page<T> page,JsonConfig jsonConfig) throws IOException{
        
        Long total = page.getTotalElements();
        List<T> list = page.getContent();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("rows", list);
        
        String json;
        if(jsonConfig != null){
            json=JSONObject.fromObject(map,jsonConfig).toString();
        }else{
            json=JSONObject.fromObject(map).toString();
        }
        
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
    
    public void list2json(List<T> list,JsonConfig jsonConfig) throws IOException{
        String json;
        if(jsonConfig != null){
            json = JSONArray.fromObject(list,jsonConfig).toString();
        }else{
            json = JSONArray.fromObject(list).toString();
        }
        
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

}
  
