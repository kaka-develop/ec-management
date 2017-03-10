package org.group2.webapp.util;

import org.apache.commons.lang3.StringUtils;

public class ConvertUntil {

    public static Long covertStringToLong(String str){
        if(StringUtils.isNumeric(str))
            return Long.parseLong(str);
        else
            return null;
    }
}
