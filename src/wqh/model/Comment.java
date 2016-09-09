package wqh.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created on 2016/3/12.
 *
 * id INT(11) PK
 * content VARCHAR(200) NN
 * createdBy INT(11) NN FK->user
 * createdAt DATETIME NN
 * belongTo INT(11) NN FK->blog
 * replyToCommentId INT(11) FK->comment
 *
 * @author 王启航
 * @version 1.0
 */
public class Comment extends Model<Comment> {
    public static final Comment dao = new Comment();

}
