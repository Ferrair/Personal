package wqh.aop;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import wqh.config.Result;

/**
 * Created on 2016/5/12.
 *
 * Intercept the HTTP method that is not POST.
 *
 * @author 王启航
 * @version 1.0
 */
public class PostIntercept implements Interceptor {
    private Result mResult = new Result();

    @Override
    public void intercept(Invocation inv) {

        Controller mController = inv.getController();
        if (!mController.getRequest().getMethod().equals("POST")) {
            mResult.fail(104);
            mController.renderJson(mResult);
        } else {
            inv.invoke();
        }
    }
}
