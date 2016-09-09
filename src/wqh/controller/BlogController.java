package wqh.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import wqh.aop.DeleteIntercept;
import wqh.aop.PostIntercept;
import wqh.aop.UserIntercept;
import wqh.config.Result;
import wqh.model.Blog;
import wqh.model.Comment;
import wqh.service.BlogService;
import wqh.service.CommentService;
import wqh.service.ServiceAbs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.1 Add Some AOP
 */

@SuppressWarnings("Duplicates")
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
        if (blogAbstracts.getList().size() == 0) {
            mResult.fail(107);
        } else {
            mResult.success(blogAbstracts.getList());
        }
        renderJson(mResult);
    }

    @ActionKey("/api/blog/queryById")
    public void queryById() {
        Integer id = getParaToInt("id");
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        Blog aBlog = mBlogService.queryById(id);
        if (aBlog == null) {
            mResult.fail(101);
        } else {
            mResult.success(aBlog);
        }
        renderJson(mResult);
    }

    /**
     * Provided Fuzzy-Query:return the blog whose title contains the <code>title<code/>
     * So this method is used as search
     */
    @ActionKey("/api/blog/queryByTitle")
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
    @ActionKey("/api/blog/queryByCreatedAt")
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

    @ActionKey("/api/blog/queryByTag")
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


    @ActionKey("/api/blog/queryByType")
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

    @ActionKey("/api/blog/queryComment")
    public void queryComment() {
        Integer belongTo = getParaToInt("belongTo");
        Integer pageNum = getParaToInt("pageNum");
        if (belongTo == null || pageNum == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        Page<Comment> commentList = mCommentService.queryByBelongId(belongTo, pageNum);
        if (commentList.getList().size() == 0) {
            mResult.fail(107);
        } else {
            mResult.success(commentList.getList());
        }
        renderJson(mResult);
    }

    @Before(PostIntercept.class)
    @ActionKey("/api/blog/addTimes")
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
    @ActionKey("/api/blog/appendComment")
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
     * Publish comments MUST login first.
     */
    @Before({PostIntercept.class, UserIntercept.class})
    @ActionKey("/api/blog/replyComment")
    public void replyComment() {
        Integer belongTo = getParaToInt("belongTo"); //MUST
        String content = getPara("content");         //MUST
        String createdBy = getPara("createdBy");     //MUST
        Integer replyTo = getParaToInt("replyTo");   //MUST

        if (belongTo == null || content == null || createdBy == null || replyTo == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        mResult.success(mCommentService.reply(createdBy, belongTo, content, replyTo));
        renderJson(mResult);
    }

    @Before({DeleteIntercept.class})
    @ActionKey("/api/blog/deleteById")
    public void deleteById() {
        Integer id = getParaToInt("id"); //MUST
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        mResult.success(mCommentService.delete(id));
        renderJson(mResult);
    }


    @Before(PostIntercept.class)
    @ActionKey("/api/blog/publish")
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
    @ActionKey("/api/blog/publishFile")
    public void publishFile() {
        String type = getPara("type");
        String abstractStr = getPara("abstractStr");
        String tag = getPara("tag");         // MUST
        File blogFile = getFile("blog").getFile();
        if (blogFile == null) {
            mResult.fail(110);
            renderJson(mResult);
            return;
        }
        if (tag == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        if (!blogFile.getName().endsWith(".md")) {
            mResult.fail(110);
            renderJson(mResult);
            return;
        }
        String title = blogFile.getName();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(blogFile), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while (line != null) {
                line = reader.readLine();
                builder.append(line);
            }
            mResult.success(mBlogService.publish(title, tag, type, abstractStr, builder.toString()));
            renderJson(mResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mResult.fail(109);
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @ActionKey("/api/blog/update")
    public void update() {
        Integer id = getParaToInt("id");
        String key = getPara("key");
        String value = getPara("value");

        if (id == null || value == null || key == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        mResult.success(mBlogService.update(id, key, value));
        renderJson(mResult);
    }

    /**
     * Update blog content via given File.
     */
    @ActionKey("/api/blog/updateFile")
    public void updateFile() throws IOException {
        Integer id = getParaToInt("id");
        File blogFile = getFile("blog").getFile();
        if (id == null) {
            mResult.fail(102);
            renderJson(mResult);
            return;
        }
        if (!blogFile.getName().endsWith(".md")) {
            mResult.fail(110);
            renderJson(mResult);
            return;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(blogFile), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while (line != null) {
                line = reader.readLine();
                builder.append(line);
            }
            mResult.success(mBlogService.update(id, "content", builder.toString()));
            renderJson(mResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mResult.fail(109);
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
