package wqh.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class TimeUtil {
    public final static String TIME = "HH:mm";
    public final static String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE = "yyyy-MM-dd";

    /**
     * 2016-2-3 19:23:34
     */
    public static String getDateTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME);
        return format.format(new Date(time));
    }

    /**
     * 23:23
     */
    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(TIME);
        return format.format(new Date(time));
    }

    /**
     * 2016-2-3
     */
    public static String getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat(DATE);
        return format.format(new Date(time));
    }
}
