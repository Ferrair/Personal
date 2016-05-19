package wqh.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import wqh.aop.PostIntercept;
import wqh.config.Result;
import wqh.model.User;
import wqh.service.ServiceAbs;
import wqh.service.UserService;

/**
 * Created on 2016/5/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class UserController extends Controller {

    private UserService mUserService = ServiceAbs.getInstance(UserService.class, this);
    private Result mResult = new Result();

    @Before(PostIntercept.class)
    @ActionKey("/user/register")
    public void register() {
        String username = getPara("username");
        String password = getPara("password");
        if (username == null || password == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        // Duplicate User
        if (mUserService.isExist(username)) {
            mResult.fail(108); // Duplicate User
            renderJson(mResult);
            return;
        }
        User aUser = mUserService.register(username, password);
        // Unknown Error........
        if (aUser == null) {
            mResult.fail(109);
            renderJson(mResult);
            return;
        }
        mResult.success(aUser);
        renderJson(mResult);
    }

    @Before(PostIntercept.class)
    @ActionKey("/user/login")
    public void login() {
        String username = getPara("username");
        String password = getPara("password");
        if (username == null || password == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        User aUser = mUserService.login(username, password);
        // Wrong Username Or password.
        if (aUser == null) {
            mResult.fail(101);
            renderJson(mResult);
            return;
        }
        mResult.success(aUser);
        renderJson(mResult);
        // Session Work.
        Integer id = aUser.get("id"); // Make the type of return-value is Integer.
        this.setSessionAttr(String.valueOf(id), true);
    }

    @ActionKey("/user/logout")
    public void logout() {
        Integer id = getParaToInt("id");
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        mResult.success(mUserService.logout(id));
        renderJson(mResult);

        // Session Work.
        this.removeSessionAttr("user");
        this.removeSessionAttr("userID");
    }

    /**
     * Get the currentUser's status.(The Result set will always null whether the status is logged or not logged)
     */
    @ActionKey("/user/status")
    public void status() {
        Integer id = getParaToInt("id");
        User targetUser = mUserService.queryById(id);
        if (targetUser == null) {
            mResult.fail(101);
            renderJson(mResult);
            return;
        }
        Boolean userStatus = getSessionAttr(String.valueOf(id));
        if (userStatus != null && userStatus) {
            mResult.success(null);
            renderJson(mResult);
            return;
        }

        mResult.fail(106);
        renderJson(mResult);
    }

}
