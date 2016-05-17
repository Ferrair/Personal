package wqh.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import wqh.aop.PostIntercept;
import wqh.aop.UserIntercept;
import wqh.config.Result;
import wqh.model.Blog;
import wqh.model.Comment;
import wqh.service.BlogService;
import wqh.service.CommentService;
import wqh.service.ServiceAbs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.1 Add Some AOP
 */

public class BlogController extends Controller {

    private BlogService mBlogService = ServiceAbs.getInstance(BlogService.class, this);
    private CommentService mCommentService = ServiceAbs.getInstance(CommentService.class, this);
    private Result mResult = new Result();

    /**
     * Index of the blog,show the tag and abstracts of all the blog.
     */
    public void index() {
        Integer pageNum = getParaToInt("pageNum");
        if (pageNum == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        Page<Blog> blogAbstracts = mBlogService.queryWithoutContent(pageNum);
        if (blogAbstracts.getPageSize() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(blogAbstracts.getList());
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
        Integer belongTo = getParaToInt("belongTo");
        Integer pageNum = getParaToInt("pageNum");
        if (belongTo == null || pageNum == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        Page<Comment> commentList = mCommentService.queryByBelongId(belongTo, pageNum);
        if (commentList.getPageSize() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(commentList.getList());
        }
        renderJson(mResult);
    }

    @Before(PostIntercept.class)
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
        mResult.success(mBlogService.publish(title, tag, type, abstractStr, content));
        renderJson(mResult);
    }

    @Before(PostIntercept.class)
    @ActionKey("/blog/addTimes")
    public void addTimes() {
        Integer id = getParaToInt("id");
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        mResult.success(mBlogService.addTimes(id));
        renderJson(mResult);
    }

    /**
     * Publish comments MUST login first.
     */
    @Before({PostIntercept.class, UserIntercept.class})
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
        mResult.success(mCommentService.publish(createdBy, belongTo, content));
        renderJson(mResult);
    }


    /**
     * Search everywhere by given searchStr.And provided Fuzzy-Query.
     */
    @ActionKey("/blog/search")
    public void search() {
        String searchStr = getPara("search");
        BlogService mBlogService = ServiceAbs.getInstance(BlogService.class, this);
        Result mResult = new Result();
        List<Blog> blogList = new ArrayList<>();

        blogList.addAll(mBlogService.queryByTag(searchStr));
        blogList.addAll(mBlogService.queryByTitle(searchStr));
        blogList.addAll(mBlogService.queryByType(searchStr));
        //Query in Blog's content when searchStr.length() > 5.
        if (searchStr.length() > 5)
            blogList.addAll(mBlogService.queryByContent(searchStr));

        if (blogList.size() == 0) {
            mResult.fail(101);
        } else {
            mResult.success(blogList);
        }
        renderJson(mResult);
    }

}
