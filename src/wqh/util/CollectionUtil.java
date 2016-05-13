package wqh.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 2016/5/3.
 *
 * @author 王启航
 * @version 1.0
 */
public class CollectionUtil {

    /**
     * Convert item data  to List
     */
    public static <T> List<T> of(T... data) {
        List<T> mList = new ArrayList<>();
        Collections.addAll(mList, data);
        return mList;
    }
}
