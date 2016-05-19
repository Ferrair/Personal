package wqh.config;

import wqh.util.CollectionUtil;

import java.util.HashMap;
import java.util.List;
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
        code.put(104, "Action Error"); // Wrong in HTTP action.
        code.put(105, "Access Denied"); // Access Denied.
        code.put(106, "Need Login"); // Need Login.
        code.put(107, "No More Data"); // No More Data
        code.put(108, "Duplicate User"); // Duplicate User
        code.put(109, "Duplicate User"); // Unknown Error
    }

    public void fail(int errorCode) {
        put("Code", errorCode);
        put("Msg", code.get(errorCode));
    }


    public <DataType> void success(DataType aData) {
        put("Code", 100);
        put("Msg", code.get(100));
        put("Result", CollectionUtil.of(aData));
    }


    public <DataType> void success(List<DataType> aList) {
        put("Code", 100);
        put("Msg", code.get(100));
        put("Result", aList);
    }
}
