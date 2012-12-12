package com.jaysan1292.groupproject.android.util;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import org.apache.log4j.Level;

/**
 * Created with IntelliJ IDEA.
 * Date: 11/12/12
 * Time: 7:34 PM
 *
 * @author Jason Recillo
 */
public class Log4jConfigurator {
    private Log4jConfigurator() {}

    public static void configure() {
        LogConfigurator config = new LogConfigurator();

        config.setRootLevel(Level.ALL);
        config.setUseFileAppender(false);
        config.setLogCatPattern("(%F:%L): %m%n");

        config.configure();
    }
}
