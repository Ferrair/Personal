package wqh.service;

import wqh.model.Admin;
import wqh.util.TimeUtil;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class AdminService extends ServiceAbs {

    public boolean attemptLogin(String adminUserName, String adminPassword) {
        Admin mAdmin = Admin.dao.findFirst("SELECT * FROM admin WHERE username =? AND password =?", adminUserName, adminPassword);
        if (mAdmin == null)
            return false;

        //Set the time last modify
        Admin.dao.findById(adminUserName).set("lastModified", TimeUtil.getDateTime(System.currentTimeMillis())).update();

        //Set Session
        mController.setSessionAttr("admin", adminUserName);
        return true;
    }


    public void logout() {
        mController.removeSessionAttr("admin");
    }
}
