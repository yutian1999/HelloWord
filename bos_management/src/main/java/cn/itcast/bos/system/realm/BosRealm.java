package cn.itcast.bos.system.realm;


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
import org.springframework.stereotype.Service;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.base.PermissionService;
import cn.itcast.bos.service.base.RolesService;
import cn.itcast.bos.service.base.UserService;

//@Service
public class BosRealm extends AuthorizingRealm{
	//realm继承AuthorizingRealm并重写doGetAuthorizationinfo进行授权
	@Autowired
	private RolesService rolesService;
	@Autowired
	private PermissionService permissionService;
	
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		//权限验证
		SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//根据当前用户查询角色和权限
		System.out.println("授权...............");
		List<Role> rs = rolesService.findByUser(user);
		for (Role role : rs) {
			authenticationInfo.addRole(role.getKeyword());;
		}
		List<Permission> ps = permissionService.findByUser(user);
		for (Permission permission : ps) {
			authenticationInfo.addStringPermission(permission.getKeyword());
		}
		return authenticationInfo;
	}
	
	@Autowired
	private UserService userService;
	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		User user = userService.findByUserName(usernamePasswordToken.getUsername());
		if (user == null) {
			return null;
		}else {
			return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
		}
	}

}
