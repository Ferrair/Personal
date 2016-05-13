package wqh.aop;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import wqh.config.Result;
import wqh.model.User;
import wqh.service.ServiceAbs;
import wqh.service.UserService;

/**
 * Created on 2016/5/12.
 * Interceptor of token
 *
 * @author 王启航
 * @version 1.0
 */
public class UserIntercept implements Interceptor {
    private Result mResult = new Result();
    private Controller mController;

    @Override
    public void intercept(Invocation inv) {
        try {
            mController = inv.getController();
            UserService mUserService = ServiceAbs.getInstance(UserService.class, mController);

            // Header Param.
            Integer userIDHeader = Integer.valueOf(mController.getRequest().getHeader("userID"));
            String preToken = mController.getRequest().getHeader("token");

            Boolean userIDSession = mController.getSessionAttr(String.valueOf(userIDHeader));

            // Login by Token
            if (userIDHeader != null && preToken != null) {
                User aUser = mUserService.queryById(userIDHeader);
                if (aUser == null) {
                    mResult.fail(101);
                    mController.renderJson(mResult);
                    return;
                }

                if (aUser.get("token").equals(preToken)) {
                    inv.invoke();
                    return;
                } else {
                    mResult.fail(106);
                    mController.renderJson(mResult);
                    return;
                }
            }
            // Session is out of time.
            if (userIDSession == null || !userIDSession) {
                mResult.fail(105);
                mController.renderJson(mResult);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            mResult.fail(102);
            mController.renderJson(mResult);
        }
    }
}
