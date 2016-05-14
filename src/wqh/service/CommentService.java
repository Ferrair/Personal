package wqh.service;

import wqh.model.Comment;
import wqh.util.CollectionUtil;
import wqh.util.TimeUtil;

import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class CommentService extends ServiceAbs {

    /**
     * @param createdBy the user name of the comment
     * @param belongTo  the blog id that the comment point to
     * @param content   the content of this comment
     */
    public List<Comment> publish(String createdBy, int belongTo, String content) {
        Comment aComment = new Comment();
        aComment.set("createdBy", createdBy);
        aComment.set("content", content);
        aComment.set("belongTo", belongTo);
        aComment.set("createdAt", TimeUtil.getDateTime(System.currentTimeMillis()));
        if (aComment.save())
            return CollectionUtil.of(aComment);
        else return null;
    }

    /**
     * comment.createdBy = user.id
     * comment.belongTo = blog.id = belongTo
     */
    public List<Comment> queryByBelongId(int belongTo) {
        return Comment.dao.find("SELECT comment.*,user.username AS creatorName,user.avatarUrl AS creatorAvatarUrl FROM comment,blog,user WHERE comment.belongTo = blog.id AND comment.createdBy = user.id AND comment.belongTo = ?", belongTo);
    }

    public boolean delete(int id) {
        //Todo: Not consider in this version
        return false;
    }

    public boolean query() {
        //Todo: Not consider in this version
        return false;
    }

}
