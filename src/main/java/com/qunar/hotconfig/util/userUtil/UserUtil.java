package com.qunar.hotconfig.util.userUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by kun.ji on 2017/4/20.
 */
public class UserUtil {
    private static final String COOKIE_KEY = "user_info";

    public static String getUserKey(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie c : cookies) {
            if (COOKIE_KEY.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

}
