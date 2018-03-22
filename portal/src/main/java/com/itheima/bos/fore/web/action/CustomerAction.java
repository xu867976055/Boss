package com.itheima.bos.fore.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.crm.domain.Customer;
import com.itheima.utils.MailUtils;
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
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    
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
        return NONE;
    }
    
    //获取用户输入的验证码
    private String checkcode;
    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
    
    
    
    //注册
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
            //生成激活码
            String activeCode = RandomStringUtils.randomNumeric(32);
            //储存激活码
            //以电话号为key
            redisTemplate.opsForValue().set(model.getTelephone(),activeCode,1,TimeUnit.DAYS);
          
            String emailBody = "感谢您注册本网站，请在24小时之内点击<a href='http://localhost:8280/portal/customerAction_active.action?activeCode="
                    + activeCode + "&telephone=" + model.getTelephone()
                    + "'>本链接</a>激活您的帐号";
            //发送激活邮件
            MailUtils.sendMail(model.getEmail(), "激活邮件", emailBody);
            return SUCCESS;
        }
        return ERROR;
    }
    
    //用属性驱动获取激活码
    private String activeCode;
    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
    
    //激活
    @Action(value="customerAction_active",results={@Result(name="success",location="/login.html",type="redirect"), @Result(name="error",location="/signup-fail.html",type="redirect")})
    public String active(){
        
        //系统生成的激活码
        String serverCode = redisTemplate.opsForValue().get(model.getTelephone());
        
        if(StringUtils.isNotEmpty(activeCode)  && StringUtils.isNotEmpty(serverCode) && serverCode.equals(activeCode)){
            WebClient.create("http://localhost:8180/crm/webService/customerService/active")
            .query("telephone", model.getTelephone())
            .accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .put(null);
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
    
    
    private String remember;
    public void setRemember(String remember) {
        this.remember = remember;
    }
    
    @Action(value="customerAction_login",results={@Result(name="success",location="/index.html",type="redirect"), 
            @Result(name="error",location="/login.html",type="redirect"),
            @Result(name="unactive",location="/waitactive.html",type="redirect")})
    public String login() throws IOException{
        
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        //1 校验验证码
        //系统生成的验证码
        String validateCode = (String) request.getSession().getAttribute("validateCode");
        if(StringUtils.isNotEmpty(checkcode) && StringUtils.isNotEmpty(validateCode) && validateCode.equalsIgnoreCase(checkcode)){
        //2 判断用户是否激活（根据电话获取用户判断）
            Customer customer = WebClient.create("http://localhost:8180/crm/webService/customerService/findCustomer")
                    .query("telephone", model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .get(Customer.class);
            // 空指针异常
            // Integer int
            
            if(customer != null && customer.getType() != null){
                if(customer.getType() == 1){
                 //激活
                Customer customer2 = WebClient.create("http://localhost:8180/crm/webService/customerService/login")
                        .query("telephone", model.getTelephone())
                        .query("password", model.getPassword())
                        .accept(MediaType.APPLICATION_JSON)
                        .type(MediaType.APPLICATION_JSON)
                        .get(Customer.class);
                if(customer2 != null){
                    //登入成功
                    request.getSession().setAttribute("customer", customer2);
                    return SUCCESS;
                }else{
                    return ERROR;
                }
                    
                }
            }else{
                return "unactive";
            }
            
        }else{
            writer.print("验证码错误");
            System.out.println("验证码错误");
            return ERROR;
        }
      return ERROR;
    }
}
  
