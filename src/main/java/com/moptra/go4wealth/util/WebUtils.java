package com.moptra.go4wealth.util;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.moptra.go4wealth.security.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class WebUtils {

    private static ApplicationContext ctx;
    
    public static final String ADMIN_ROLE_NAME = "ADMIN_ROLE";
	public static final String AGENT_ROLE_NAME = "AGENT_ROLE";
	public static final String MANAGER_ROLE_NAME = "MANAGER_ROLE";
	public static final String USER_ROLE_NAME = "USER_ROLE";

    public static ApplicationContext getSpringContext() {
        return ctx;
    }
    
    public static void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    public static HttpServletRequest getCurrentRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public static UserPrincipal getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserPrincipal) {
                return ((UserPrincipal) principal);
            }
        }
        return null;
    }

    public static boolean userHasAuthority(String authority) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(authority));
        }
        return false;
    }

    public static boolean userHasAuthoritiesOr(String[] authorities) {
        List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>(authorities.length);
        for (int i = 0; i < authorities.length; i++) {
            authList.add(new SimpleGrantedAuthority(authorities[i]));
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return CollectionUtils.containsAny(SecurityContextHolder.getContext().getAuthentication().getAuthorities(), authList);
        }
        return false;
    }

    public static boolean userHasAuthoritiesAnd(String[] authorities) {
        List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>(authorities.length);
        for (int i = 0; i < authorities.length; i++) {
            authList.add(new SimpleGrantedAuthority(authorities[i]));
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getAuthorities().containsAll(authList);
        }
        return false;
    }

    public static String getRealFilePath(String filePath){
        return ((WebApplicationContext)WebUtils.getSpringContext()).getServletContext().getRealPath(filePath);
    }

}
