package com.sageburner.im.android.util;

import com.sageburner.im.android.core.User;

/**
 * Created by Ryan on 12/17/2014.
 */
public class UserUtils {

    public static String parseUserAlias(User user) {
        String username = user.getUsername();
        int separatorPos = username.lastIndexOf("@");
        String userAlias = username.substring(0, separatorPos);
        return userAlias;
    }

    public static String parseUserDomain(User user) {
        String username = user.getUsername();
        int separatorPos = username.lastIndexOf("@");
        String userDomain = username.substring(separatorPos + 1);
        return userDomain;
    }
}
