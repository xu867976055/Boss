package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.springframework.ui.Model;

import com.itheima.bos.domain.base.Constant;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.StandardService;
import com.itheima.bos.web.action.CommonAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ClassName:StandardAction <br/>
 * Function: <br/>
 * Date: 2018年3月14日 上午8:56:48 <br/>
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class StandardAction extends CommonAction<Standard> {

    public StandardAction() {

        super(Standard.class);

    }

    @Autowired
    private StandardService standardService;

    @Action(value = "standardAction_save", results = {@Result(name = "save_success",
            location = "/pages/base/standard.html", type = "redirect")})
    public String save() {
       
        standardService.save(getModel());
        return Constant.SAVE_SUCCESS;
    }

    @Action(value = "standardAction_findByPage")
    public String findByPage() throws IOException {
        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Standard> page = standardService.findAll(pageable);
        // 总数据条数

        // JSONObject : 封装对象或map集合
        // JSONArray : 数组,list集合
        // 把对象转化为json字符串
        page2json(page, null);
        return NONE;

    }

    @Action(value = "standardAction_findAll")
    public String findAll() throws IOException {

        Page<Standard> page = standardService.findAll(null);
        List<Standard> list = page.getContent();
        String json = JSONArray.fromObject(list).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

        return NONE;
    }
    
    
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    
    @Action(value = "standardAction_delete",results={@Result(name="success",location="/pages/base/standard.html",type="redirect")})
    public String delete() throws IOException {
        standardService.deleteById(ids);
        return SUCCESS;
    }

}
