package com.mip.framework.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;

public class CustomMISPUser extends User{
	
	private static final long serialVersionUID = 1L;
	
	public CustomMISPUser(String username, String password, boolean isEnabled,
			 boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
			 GrantedAuthority[] authorities) {
			
		 super(username, password, isEnabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
					
	 }
}
