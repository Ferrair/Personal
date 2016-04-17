package wqh.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import wqh.aop.AdminIntercept;
import wqh.service.AdminService;
import wqh.service.ServiceAbs;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
@Clear
@Before(AdminIntercept.class)
public class AdminController extends Controller {

    AdminService mService = ServiceAbs.getInstance(AdminService.class, this);

    /**
     * Attempt to login.Other operation can be done after login(which means this method is the only method without login)
     * So <code>@Clear<code/> here.
     */
    @Clear
    public void index() {
        String adminUserName = getPara("username");
        String adminPassword = getPara("password");

        boolean isSuccess = mService.attemptLogin(adminUserName, adminPassword);
        if (isSuccess) {
            validateToken("Token");
            redirect("pager/admin.jsp");
        } else {
            createToken("Token", 30 * 60);
            redirect("pager/adminLogin.jsp");
        }
    }

    @ActionKey("/admin/logout")
    public void logout() {
        mService.logout();
    }


}
