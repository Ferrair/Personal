package wqh.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import wqh.aop.AdminIntercept;
import wqh.aop.PostIntercept;
import wqh.config.Result;
import wqh.service.AdminService;
import wqh.service.ServiceAbs;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
@Before(AdminIntercept.class)
public class AdminController extends Controller {

    private AdminService mService = ServiceAbs.getInstance(AdminService.class, this);
    private Result mResult = new Result();

    public void index() {
    }

    /**
     * Attempt to login.Other operation can be done after login(which means this method is the only method without login)
     * So <code>@Clear<code/> here.
     */
    @Before(PostIntercept.class)
    @ActionKey("/api/admin/login")
    public void login() {
        String adminUserName = getPara("username");
        String adminPassword = getPara("password");
        if (adminUserName == null || adminPassword == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        mResult.success(mService.attemptLogin(adminUserName, adminPassword));
        renderJson(mResult);
    }

    @ActionKey("/api/admin/logout")
    public void logout() {
        mService.logout();
    }


}
