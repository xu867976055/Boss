package com.itheima.bos.web.action.base;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Constant;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.CourierService;
import com.itheima.bos.web.action.CommonAction;
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
public class CourierAction extends CommonAction<Courier>{
    
    public CourierAction() {
        super(Courier.class);  
    }
    
    @Autowired
    private CourierService courierService;
    

    @Action(value="courierAction_save",results={@Result(name="save_success",location="/pages/base/courier.html",type="redirect")})
    public String save(){
        courierService.save(model);
        return Constant.SAVE_SUCCESS;
    }

    @Action(value="courierAction_findByPage")
    public String findByPage() throws IOException{
        
//        构造条件查询
        Specification<Courier> specification = new Specification<Courier>() {

            //root:泛型的对象  cb:查询条件的对象
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                
//              //从模型驱动中获取到传过来的查询条件
                String courierNum = model.getCourierNum();  
                String company = model.getCompany();
                String type = model.getType();
                Standard standard = model.getStandard();
                
                ArrayList<Predicate> list = new ArrayList<>();
                
//                判断条件(工号)
                if(StringUtils.isNotEmpty(courierNum)){
                 // where courierNum = ?
                   Predicate p1 =  cb.equal(root.get("courierNum").as(String.class), courierNum);
                    list.add(p1);
                }
                
                //所属单位
                if(StringUtils.isNotEmpty(company)){
                    // where courierNum = ?
                      Predicate p2 =  cb.like(root.get("company").as(String.class), company);
                       list.add(p2);
                   }
                //类型
                if(StringUtils.isNotEmpty(type)){
                    // where courierNum = ?
                      Predicate p3 =  cb.equal(root.get("type").as(String.class), type);
                       list.add(p3);
                   }
                
                if(standard!=null){
                    String name = standard.getName();
                    if(StringUtils.isNotEmpty(name)){
                        //连接查询
                        Join<Object, Object> join = root.join("standard");
                        Predicate p4 =  cb.equal(join.get("name").as(String.class), name);
                        list.add(p4);
                       }
                }
                
                if(list.size() == 0){   //用户没有输入条件
                    return null;
                }
                
                //用户输入了条件
                //构造数组
                Predicate[] arr = new Predicate[list.size()];
                list.toArray(arr);
                return cb.and(arr);
                
            }
            
        };
        
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Courier> page = courierService.findAll(specification,pageable);
    
        // 灵活控制输出的内容
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas","takeTime"});
        
        // 在实际开发的时候,为了提高服务器的性能,把前台页面不需要的数据都应该忽略掉
        page2json(page, jsonConfig);
        
        return NONE;
    }
    
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    
    @Action(value="courierAction_delete",results={@Result(name="delete_by_id_success",location="/pages/base/courier.html",type="redirect")})
    public String delete(){
        courierService.deleteById(ids);
        return Constant.DELETE_BY_ID_SUCCESS;
    }
    
//    查询在职的快递员
    @Action(value="courierAction_findCourier")
    public String findCourier() throws IOException{
        
        List<Courier> list = courierService.findCourier();
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas","takeTime"});
        
        list2json(list, jsonConfig);
        return NONE;   
    }
}
  
