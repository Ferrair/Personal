package wqh.model;


import com.jfinal.plugin.activerecord.Model;

/**
 * Created on 2016/5/12.
 *
 * id INT(11) PK
 * username VARCHAR(40) NN
 * password VARCHAR(40) NN
 * avatarUri VARCHAR(200)
 * token VARCHAR(100)
 * lastModified DATETIME NN
 * coverUri VARCHAR(100)
 *
 * @author 王启航
 * @version 1.0
 */
public class User extends Model<User> {
    public static final User dao = new User();
}
