package wqh.service;

import com.jfinal.plugin.activerecord.Page;
import wqh.model.Comment;
import wqh.model.User;
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
    public Comment publish(String createdBy, int belongTo, String content) {
        Comment aComment = new Comment();
        aComment.set("createdBy", createdBy);
        aComment.set("content", content);
        aComment.set("belongTo", belongTo);
        aComment.set("createdAt", TimeUtil.getDateTime(System.currentTimeMillis()));
        if (aComment.save())
            return aComment;
        else return null;
    }

    /**
     * @param replyTo ： reply to which Comment.
     */
    public Comment reply(String createdBy, Integer belongTo, String content, Integer replyTo) {
        Comment aComment = new Comment();
        Comment replyComment = Comment.dao.findById(replyTo);
        aComment.set("createdBy", createdBy);
        aComment.set("content", content + " //@" + replyComment.get("creatorName") + ":" + replyComment.get("content"));
        aComment.set("belongTo", belongTo);
        aComment.set("createdAt", TimeUtil.getDateTime(System.currentTimeMillis()));
        aComment.set("replyTo", replyTo);
        if (aComment.save())
            return aComment;
        else return null;
    }

    /**
     * comment.createdBy = user.id
     * comment.belongTo = blog.id = belongTo
     */
    public Page<Comment> queryByBelongId(int belongTo, int pageNum) {
        return Comment.dao.paginate(pageNum, 10, "SELECT comment.*,user.username AS creatorName,user.avatarUri AS creatorAvatarUri", "FROM comment,blog,user WHERE comment.belongTo = blog.id AND comment.createdBy = user.id AND comment.belongTo = ?", belongTo);
    }

    public boolean delete(int id) {
        return Comment.dao.deleteById(id);
    }

    /**
     * Query comment in one blog by given queryStr.
     */
    public Page<Comment> query(int belongTo, String queryStr, int pageNum) {
        return Comment.dao.paginate(pageNum, 10, "SELECT comment.*,user.username AS creatorName,user.avatarUri AS creatorAvatarUri", "FROM comment,blog,user WHERE comment.belongTo = blog.id AND comment.createdBy = user.id AND comment.belongTo = ? AND content LIKE %?%", belongTo, queryStr);
    }

}
