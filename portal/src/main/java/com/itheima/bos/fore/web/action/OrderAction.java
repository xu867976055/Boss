package com.itheima.bos.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 * ClassName:OrderAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月23日 下午3:50:45 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order>{

    private Order model = new Order();
   
    @Override
    public Order getModel() {
        return model;
    }
    
    
    private String recAreaInfo;
    private String sendAreaInfo;
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }
    
    @Action(value="orderAction_add",results={@Result(name="success",location="/index.html",type="redirect")})
    public String saveOrder(){
        
        // 获取发件区域数据
        if(StringUtils.isNotEmpty(recAreaInfo)){
            //切割数据
            String[] split = recAreaInfo.split("/");
            //去掉省市区
            String province = split[0].substring(0, split.length-1);
            String city = split[1].substring(0, split.length-1);
            String district = split[2].substring(0, split.length-1);
            
            //封装数据
            //注意此时的area是瞬时态，需要转化成持久态。保存数据操作
            Area area = new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            //设置数据
            model.setRecArea(area);
        }
        
        if(StringUtils.isNotEmpty(sendAreaInfo)){
            //切割数据
            String[] split = sendAreaInfo.split("/");
            //去掉省市区
            String province = split[0].substring(0, split.length-1);
            String city = split[1].substring(0, split.length-1);
            String district = split[2].substring(0, split.length-1);
            
            //封装数据
            //注意此时的area是瞬时态，需要转化成持久态。保存数据操作
            Area area = new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            //设置数据
            model.setSendArea(area);
        }
     // 调用WebService保存订单
        WebClient.create("http://localhost:8080/boss_management_web/webService/orderService/saveOrder")
        .accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON)
        .post(model);
        
        return SUCCESS;
    }

}
  
