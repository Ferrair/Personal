package wqh.service;

import wqh.model.User;
import wqh.util.CollectionUtil;
import wqh.util.TimeUtil;
import wqh.util.TokenManager;

import java.util.List;

/**
 * Created on 2016/5/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class UserService extends ServiceAbs {
    public User register(String username, String password) {
        User aUser = new User();
        aUser.set("username", username);
        aUser.set("password", password);
        aUser.set("avatarUrl", "");
        aUser.set("coverUrl", "");
        aUser.set("lastModified", TimeUtil.getDateTime(System.currentTimeMillis()));
        if (aUser.save()) {
            return aUser;
        } else {
            return null;
        }
    }

    public User login(String username, String password) {
        User aUser = User.dao.findFirst("SELECT * FROM user WHERE username = ? AND password = ?", username, password);
        if (aUser == null)
            return null;
        aUser.set("lastModified", TimeUtil.getDateTime(System.currentTimeMillis()));
        aUser.set("token", TokenManager.MD5(username + password + TimeUtil.getDateTime(System.currentTimeMillis())));
        aUser.update();
        return aUser;
    }

    public User queryById(int id) {
        return User.dao.findFirst("SELECT * FROM user WHERE id = ?", id);
    }


    public boolean isExist(String username) {
        return User.dao.findFirst("SELECT * FROM user WHERE username = ?", username) != null;
    }

    public User logout(Integer id) {
        User aUser = User.dao.findById(id);
        if (aUser == null)
            return null;
        aUser.set("token", null);
        aUser.update();
        return aUser;
    }

    public User changeCover(Integer id, String coverUrl) {
        User aUser = User.dao.findById(id);
        if (aUser == null)
            return null;
        aUser.set("coverUri", coverUrl);
        aUser.update();
        return aUser;
    }

    public User changeAvatar(Integer id, String avatarUrl) {
        User aUser = User.dao.findById(id);
        if (aUser == null)
            return null;
        aUser.set("avatarUri", avatarUrl);
        aUser.update();
        return aUser;
    }

    public String avatarURI(int id) {
        User aUser = User.dao.findById(id);
        if (aUser == null)
            return null;
        return aUser.getStr("avatarUri");
    }

    public String coverURI(int id) {
        User aUser = User.dao.findById(id);
        if (aUser == null)
            return null;
        return aUser.getStr("coverUri");
    }
}
