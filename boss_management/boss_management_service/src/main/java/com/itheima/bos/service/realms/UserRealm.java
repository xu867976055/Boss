package com.itheima.bos.service.realms;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserRealm <br/>  
 * Function:  <br/>  
 * Date:     2018年3月26日 下午8:47:40 <br/>       
 */
@Component
public class UserRealm extends AuthorizingRealm{
   
    
    //注入角色和权限
    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        
        //授权的方法
        // 每一次访问需要权限的资源的时候,都会调用授权的方法
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 需要根据当前的用户去查询对应的权限和角色
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //判断是否是超级管理员,内置管理员的权限和角色是写死的
        if("admin".equals(user.getUsername())){
            
            List<Role> roles = roleRepository.findAll();
            for (Role role : roles) {
                info.addRole(role.getKeyword());
            }
            
            List<Permission> permissions = permissionRepository.findAll();
            for (Permission permission : permissions) {
                info.addRole(permission.getKeyword());
            }
        }else{
            //其他用户(这里by小写，避免被SpringJPA当做默认规则查找)
            List<Role> roles = roleRepository.findbyUid(user.getId());
            for (Role role : roles) {
                info.addRole(role.getKeyword());
            }
            
            List<Permission> permissions = permissionRepository.findbyUid(user.getId());
            for (Permission permission : permissions) {
                info.addRole(permission.getKeyword());
            }
        }
        
        
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        // 认证的方法
        // 参数中的token就是subject.login(token)方法中传入的参数
        
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        // 根据用户名查找用户
        User user = userRepository.findByUsername(username);
        
        if(user != null){
            //比对密码
            /**
             * @param principal 当事人,主体.通常是从数据库中查询到的用户
             * @param credentials 凭证,密码.是从数据库中查询出来的密码
             * @param realmName
             */
            AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
            // 比对成功 -> 执行后续的逻辑
            // 比对失败 -> 抛异常
            return info;
        }
        
        return null;
    }

}
  
