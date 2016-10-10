package wqh.aop;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */

public class AdminIntercept implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        inv.invoke();
    }
}
