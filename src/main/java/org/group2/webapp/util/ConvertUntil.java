package org.group2.webapp.util;

import org.apache.commons.lang3.StringUtils;
import org.group2.webapp.entity.Authority;
import org.group2.webapp.security.AuthoritiesConstants;

import java.util.HashSet;
import java.util.Set;

public class ConvertUntil {

    public static Long covertStringToLong(String str){
        if(StringUtils.isNumeric(str))
            return Long.parseLong(str);
        else
            return null;
    }

    public static Set<String> convertRoleIntoAuthories(String role){
        Set<String> authorities = new HashSet<>();
        if(role == null)
            return authorities;
        switch (role) {
            case AuthoritiesConstants.ADMIN:
                authorities.add(AuthoritiesConstants.ADMIN);
                authorities.add(AuthoritiesConstants.COORDINATOR);
                authorities.add(AuthoritiesConstants.MANAGER);
                authorities.add(AuthoritiesConstants.STUDENT);
                break;
            case AuthoritiesConstants.COORDINATOR:
                authorities.add(AuthoritiesConstants.COORDINATOR);
                authorities.add(AuthoritiesConstants.MANAGER);
                authorities.add(AuthoritiesConstants.STUDENT);
                break;
            case AuthoritiesConstants.MANAGER:
                authorities.add(AuthoritiesConstants.MANAGER);
                authorities.add(AuthoritiesConstants.STUDENT);
                break;
            case AuthoritiesConstants.STUDENT:
                authorities.add(AuthoritiesConstants.STUDENT);
                break;
        }

        return authorities;
    }
}
