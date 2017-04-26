package org.group2.webapp.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.group2.webapp.security.AuthoritiesConstants;

public class ConvertUntil {

    public static Long covertStringToLong(String str){
        if(StringUtils.isNumeric(str))
            return Long.parseLong(str);
        else
            return null;
    }

    public static Integer convertStringToInteger(String str){
        if(StringUtils.isNumeric(str))
            return Integer.parseInt(str);
        else
            return 0;
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

    public static Date increase14Day(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,14);
        return date;
    }
}
