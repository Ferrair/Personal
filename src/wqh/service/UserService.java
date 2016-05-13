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
    public List<User> register(String username, String password) {
        User aUser = new User();
        aUser.set("username", username);
        aUser.set("password", password);
        aUser.set("lastModified", TimeUtil.getDateTime(System.currentTimeMillis()));
        if (aUser.save()) {
            return CollectionUtil.of(aUser);
        } else {
            return null;
        }
    }

    public List<User> login(String username, String password) {
        User aUser = User.dao.findFirst("SELECT * FROM user WHERE username = ? AND password = ?", username, password);
        if (aUser == null)
            return null;
        aUser.set("lastModified", TimeUtil.getDateTime(System.currentTimeMillis()));
        aUser.set("token", TokenManager.MD5(username + password + TimeUtil.getDateTime(System.currentTimeMillis())));
        aUser.update();
        return CollectionUtil.of(aUser);
    }

    public User queryById(int id) {
        return User.dao.findFirst("SELECT * FROM user WHERE id = ?", id);
    }

    public List<User> logout(Integer id) {
        User aUser = User.dao.findById(id);
        if (aUser == null)
            return null;
        aUser.set("token", null);
        aUser.update();
        return CollectionUtil.of(aUser);
    }
}
