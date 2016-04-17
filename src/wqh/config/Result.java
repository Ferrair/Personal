package wqh.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2016/4/8.
 *
 * @author 王启航
 * @version 1.0
 */
public class Result extends HashMap<String, Object> {
    private static Map<Integer, String> code = new HashMap<>();

    public Result() {
        code.put(100, "OK");
        code.put(101, "Can't Find Object");
        code.put(102, "Lack Of Action Parameters"); // The action param form client is lack
        code.put(103, "Insert Error"); // Occurred when call 'Publish' method fail
    }

    public void fail(int errorCode) {
        put("Code", errorCode);
        put("Msg", code.get(errorCode));
        put("Result", null);
    }


    public void success(Object object) {
        put("Code", 100);
        put("Msg", code.get(100));
        put("Result", object);
    }
}
