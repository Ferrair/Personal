package wqh.controller;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import wqh.config.Result;
import wqh.model.Blog;
import wqh.service.BlogService;
import wqh.service.ServiceAbs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */

public class IndexController extends Controller {

    @ActionKey("/")
    public void index() {
        System.out.println(PathKit.getWebRootPath());
        renderText("Welcome to DA Wizards");
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
