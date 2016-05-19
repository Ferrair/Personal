package wqh.service;

import com.jfinal.plugin.activerecord.Page;
import wqh.model.Blog;
import wqh.model.Tag;
import wqh.util.CollectionUtil;
import wqh.util.TimeUtil;

import java.util.Collection;
import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class BlogService extends ServiceAbs {

    public Blog queryById(int id) {
        return Blog.dao.findFirst("SELECT blog.*,tag.name AS tagName FROM blog,tag WHERE blog.tagId = tag.id AND blog.id = ?", id);
    }

    public Page<Blog> queryAll(int pageNum) {
        return Blog.dao.paginate(pageNum, 10, "SELECT blog.*,tag.name AS tagName", "FROM blog,tag WHERE blog.tagId = tag.id");
    }

    /**
     * Query without <attr>content<attr/>,because the content is large
     */
    public Page<Blog> queryWithoutContent(int pageNum) {
        return Blog.dao.paginate(pageNum, 10, "SELECT blog.id,blog.title,blog.type,blog.abstractStr,blog.createdAt,blog.times,tag.name AS tagName", "FROM blog,tag WHERE blog.tagId = tag.id");
    }

    /**
     * Regular-Expression like "%title%"
     */
    public List<Blog> queryByTitle(String title) {
        return Blog.dao.find("SELECT * FROM blog WHERE title LIKE '%" + title + "%'");
    }

    /**
     * @param createdAt Format: "2016-03" Query by Regular-Expression like "2016-03%"
     */
    public List<Blog> queryByTime(String createdAt) {
        return Blog.dao.find("SELECT blog.*,tag.name AS tagName FROM blog,tag WHERE blog.tagId = tag.id AND createdAt LIKE '" + createdAt + "%'");
    }

    public List<Blog> queryByTag(String tag) {
        return Blog.dao.find("SELECT blog.*,tag.name AS tagName FROM blog,tag WHERE blog.tagId = tag.id AND tag.name = ?", tag);
    }

    public List<Blog> queryByType(String type) {
        return Blog.dao.find("SELECT blog.*,tag.name AS tagName FROM blog,tag WHERE blog.tagId = tag.id AND type = ?", type);
    }

    /**
     * @param tagName the name attr of the tag. The tag MUST exists in the database,otherwise return false(that means this publish is fail)
     */
    public Blog publish(String title, String tagName, String type, String abstractStr, String content) {
        Tag targetTag = Tag.dao.findFirst("SELECT id FROM tagName WHERE name = ?", tagName);
        if (targetTag == null) {
            return null;
        }
        Blog aBlog = new Blog();
        aBlog.set("title", title);
        aBlog.set("tagId", targetTag.get("id"));
        aBlog.set("type", type);
        aBlog.set("abstractStr", abstractStr);
        aBlog.set("content", content);
        aBlog.set("createdAt", TimeUtil.getDateTime(System.currentTimeMillis()));
        aBlog.set("times", 0);
        if (aBlog.save())
            return aBlog;
        else return null;
    }

    public List<Blog> queryByContent(String content) {
        return Blog.dao.find("SELECT * FROM blog WHERE title LIKE '%" + content + "%'");
    }

    /**
     * @param id the id of a blog which want to be updated.
     */
    public boolean update(int id, String key, Object value) {
        return Blog.dao.findById(id).set(key, value).update();
    }

    /**
     * add the times which will be read.
     */
    public Blog addTimes(int id) {
        Blog targetBlog = queryById(id);
        int oldTimes = targetBlog.get("times");
        if (update(id, "times", ++oldTimes))
            return targetBlog;
        else return null;
    }

}
