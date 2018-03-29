package com.itheima.bos.web.action.system;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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

import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:UserAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月26日 下午8:07:09 <br/>       
 */
@Namespace("/") 
@ParentPackage("struts-default")
@Controller 
@Scope("prototype")
public class UserAction extends CommonAction<User> {

    @Autowired
    private UserService userService;
    
    public UserAction() {
        super(User.class);
    }

    // 用户输入的验证码
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Action(value = "userAction_login",
            results = {
                    @Result(name = "success", location = "/index.html",
                            type = "redirect"),
                    @Result(name = "login", location = "/login.html",
                            type = "redirect")})
    public String login() {
        
        
        String serverCode = (String) ServletActionContext.getRequest()
                .getSession().getAttribute("validateCode");
        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(checkcode)
                && serverCode.equals(checkcode)) {

            // 主体,代表当前用户
            Subject subject = SecurityUtils.getSubject();

            AuthenticationToken token = new UsernamePasswordToken(
                    getModel().getUsername(), getModel().getPassword());
            
            try {
                
                // 执行登录
                subject.login(token);
                // 方法的返回值由Realm中doGetAuthenticationInfo方法定义SimpleAuthenticationInfo对象的时候,第一个参数决定的
                User user = (User) subject.getPrincipal();
                ServletActionContext.getRequest().getSession()
                        .setAttribute("user", user);

                return SUCCESS;
            } catch (UnknownAccountException e) {
                // 用户名写错了
                e.printStackTrace();
                System.out.println("用户名写错了");
            } catch (IncorrectCredentialsException e) {
                // 用户名写错了
                e.printStackTrace();
                System.out.println("密码错误");
            } catch (Exception e) {
                // 用户名写错了
                e.printStackTrace();
                System.out.println("其他错误");
            }
        }

        return LOGIN;
    }
    
    @Action(value = "userAction_logout",results = { @Result(name = "success", location = "/login.html",type = "redirect")})
    public String logout() {
        
      Subject subject = SecurityUtils.getSubject();
      //退出登入
      subject.logout();
      //清空session
      ServletActionContext.getRequest().getSession().removeAttribute("user");
        return SUCCESS;
    }
    
    //用属性驱动获取用户id
    private Long[] roleIds;
    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }
    
    @Action(value="userAction_save", results = {@Result(name = "success", location = "/pages/system/userlist.html", type = "redirect") })
    public String save() throws IOException{
       
        userService.save(roleIds,getModel());
        return SUCCESS;
    }
    
    @Action(value="userAction_findByPage")
    public String findByPage() throws IOException{
        
        Pageable pageable = new PageRequest(page-1, rows);
        Page<User> page = userService.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles"});
        
        page2json(page, jsonConfig);
        return NONE;
    }

}
  
