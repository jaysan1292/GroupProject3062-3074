package com.jaysan1292.groupproject;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 03/12/12
 * Time: 11:37 AM
 */
public class WebAppCommon {
    public static final String JSP_LOGIN = "/WEB-INF/jsp/login.jsp";
    public static final String SRV_LOGIN = "/login";
    public static final String ATTR_LOGIN = "login";
    public static final Logger log = Logger.getLogger(WebAppCommon.class);

    private WebAppCommon() {}
}
