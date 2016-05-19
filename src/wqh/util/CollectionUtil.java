package wqh.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @SafeVarargs
    public static <T> List<T> of(T... data) {
        return Stream.of(data).collect(Collectors.toList());
    }
}
