package wqh.controller;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import wqh.config.Result;
import wqh.model.Blog;
import wqh.model.Comment;
import wqh.service.BlogService;
import wqh.service.CommentService;
import wqh.service.ServiceAbs;

import java.sql.Date;
import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */

public class BlogController extends Controller {

    private BlogService mBlogService = ServiceAbs.getInstance(BlogService.class, this);
    private CommentService mCommentService = ServiceAbs.getInstance(CommentService.class, this);
    Result mResult = new Result();

    /**
     * Index of the blog,show the tag and abstracts of all the blog.
     */

    public void index() {
        List<Blog> blogAbstracts = mBlogService.queryWithoutContent();
        if (blogAbstracts.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(blogAbstracts);
        }
        renderJson(mResult);
    }

    @ActionKey("/blog/queryById")
    public void queryById() {
        Integer id = getParaToInt("id");
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        List<Blog> blogList = mBlogService.queryById(id);
        if (blogList == null) {
            mResult.fail(101);
        } else {
            mResult.success(blogList);
        }
        renderJson(mResult);
    }

    /**
     * Provided Fuzzy-Query:return the blog whose title contains the <code>title<code/>
     * So this method is used as search
     */
    @ActionKey("/blog/queryByTitle")
    public void queryByTitle() {
        String title = getPara("title");
        if (title == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        List<Blog> blogList = mBlogService.queryByTitle(title);
        if (blogList.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(blogList);
        }
        renderJson(mResult);
    }

    /**
     * Provided Fuzzy-Query:return the blog whose time is in <code>createdAt<code/>
     * Format: "2016-03" MUST contains "0"
     */
    @ActionKey("/blog/queryByCreatedAt")
    public void queryByTime() {
        String createdAt = getPara("createdAt");
        if (createdAt == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        List<Blog> blogList = mBlogService.queryByTime(createdAt);
        if (blogList.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(blogList);
        }
        renderJson(mResult);
    }

    @ActionKey("/blog/queryByTag")
    public void queryByTag() {
        String tag = getPara("tag");
        if (tag == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        List<Blog> blogList = mBlogService.queryByTag(tag);
        if (blogList.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(blogList);
        }
        renderJson(mResult);
    }


    @ActionKey("/blog/queryByType")
    public void queryByType() {
        String type = getPara("type");
        if (type == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        List<Blog> blogList = mBlogService.queryByType(type);
        if (blogList.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(blogList);
        }
        renderJson(mResult);
    }

    @ActionKey("/blog/queryComment")
    public void queryComment() {
        Integer id = getParaToInt("id");
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        List<Comment> commentList = mCommentService.queryByBelongId(id);
        if (commentList.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(commentList);
        }
        renderJson(mResult);
    }


    @ActionKey("/blog/publish")
    public void publish() {
        String title = getPara("title");     //MUST
        String type = getPara("type");
        String content = getPara("content"); //MUST
        String tag = getPara("tag");         //MUST
        String abstractStr = getPara("abstractStr");

        if (title == null || content == null || tag == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        boolean success = mBlogService.publish(title, tag, type, abstractStr, content);
        if (success) {
            mResult.success(null);
        } else {
            mResult.fail(103);
        }
        renderJson(mResult);
    }


    @ActionKey("/blog/addTimes")
    public void addTimes() {
        Integer id = getParaToInt("id");
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        boolean success = mBlogService.addTimes(id);
        if (success) {
            mResult.success(null);
        } else {
            mResult.fail(103);
        }
        renderJson(mResult);
    }

    @ActionKey("/blog/appendComment")
    public void appendComment() {
        Integer belongTo = getParaToInt("belongTo"); //MUST
        String content = getPara("content");         //MUST
        String createdBy = getPara("createdBy");     //MUST

        if (belongTo == null || content == null || createdBy == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        boolean success = mCommentService.publish(createdBy, belongTo, content);
        if (success) {
            mResult.success(null);
        } else {
            mResult.fail(103);
        }
        renderJson(mResult);
    }
}
