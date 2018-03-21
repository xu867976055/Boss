package com.itheima.bos.fore.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.crm.domain.Customer;
import com.itheima.utils.SmsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 * ClassName:CustomerAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月20日 下午6:44:19 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer>{

    private Customer model = new Customer();
    
    
    @Override
    public Customer getModel() {
        return model;
    }
    
    @Action(value="customerAction_sendSMS")
    public String sendSMS() throws InterruptedException{
        
        //获取前台获得的手机号
        String telephone = model.getTelephone();
        //随机生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println("---------------------------------------------"+code);
        //存入session
        ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);
        try {
            //发送验证码
//            SmsUtils.sendSms(telephone, code);
            SmsUtils.SSM(telephone, code);
            
        } catch (ClientException e) {
            e.printStackTrace();  
        }
        return NONE;
    }
    
    //获取用户输入的验证码
    private String checkcode;
    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
    
    @Action(value="customerAction_regist",results={@Result(name="success",location="/signup-success.html",type="redirect"), @Result(name="error",location="/signup-fail.html",type="redirect")})
           
    public String regist(){
        //获取系统生成的域对象中的验证码
        String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");
        //校验验证码
        if(StringUtils.isNotEmpty(serverCode) && StringUtils.isNotEmpty(checkcode) && checkcode.equals(serverCode)){
            WebClient.create("http://localhost:8180/crm/webService/customerService/saveCustomer")
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .post(model);
            return SUCCESS;
        }
        
        return ERROR;
    }
    
    //校验手机号
    @Action(value="customerAction_findCustomer")
    public String findCustomer() throws IOException{
        //获取前台用户输入的手机号
        String telephone = model.getTelephone();
        Customer customer = WebClient.create("http://localhost:8180/crm/webService/customerService/findCustomer")
        .query("telephone", telephone)
        .accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON)
        .get(Customer.class);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer =response.getWriter();
        //用户已存在
        if(customer != null){
            writer.write("该用户已存在");
        }
        
        return NONE;
    }
}
  
