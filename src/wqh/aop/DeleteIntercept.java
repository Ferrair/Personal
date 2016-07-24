package wqh.aop;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import wqh.config.Result;

/**
 * Created on 2016/7/24.
 *
 * @author 王启航
 * @version 1.0
 */
public class DeleteIntercept implements Interceptor {
    private Result mResult = new Result();

    @Override
    public void intercept(Invocation inv) {

        Controller mController = inv.getController();
        if (!mController.getRequest().getMethod().equals("DELETE")) {
            mResult.fail(104);
            mController.renderJson(mResult);
        } else {
            inv.invoke();
        }
    }
}
