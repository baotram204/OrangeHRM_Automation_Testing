package utils;

import org.apache.logging.log4j.*;

public class LogUtils {

    private static final Logger logger = LogManager.getLogger(LogUtils.class);

    /**
     * Generate log message with INFO
     * @param message
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Generate log message with ERROR
     * @param message
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }


}
