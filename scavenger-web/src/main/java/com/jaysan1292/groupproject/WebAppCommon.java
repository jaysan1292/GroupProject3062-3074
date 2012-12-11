package com.jaysan1292.groupproject;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 03/12/12
 * Time: 11:37 AM
 */
public class WebAppCommon {
    public static final String SRV_LOGIN = "/login";
    public static final String ATTR_CLIENT = "client";
    public static final String ATTR_LOGIN = "loggedIn";
    public static final Logger log = Logger.getLogger(WebAppCommon.class);

    private WebAppCommon() {}

    private static final Pattern QUERY_STRING_AMP = Pattern.compile("&");
    private static final Pattern QUERY_STRING_VALUE = Pattern.compile("=");

    public static Map<String, String> queryStringToMap(HttpServletRequest request) {
        // If there is no query string, return an empty Map
        String queryString = request.getQueryString();
        if ((queryString == null) || queryString.isEmpty()) return new HashMap<String, String>();

        final String[] queryParams = QUERY_STRING_AMP.split(queryString);

        return new HashMap<String, String>() {{
            for (String param : queryParams) {
                String[] value = QUERY_STRING_VALUE.split(param);
                put(value[0], value[1]);
            }
        }};
    }
}
