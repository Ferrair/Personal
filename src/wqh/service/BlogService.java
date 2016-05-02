package wqh.service;

import wqh.model.Blog;
import wqh.model.Tag;
import wqh.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class BlogService extends ServiceAbs {

    public List<Blog> queryById(int id) {
        List<Blog> mList = new ArrayList<>();
        mList.add(Blog.dao.findFirst("SELECT blog.*,tag.name AS tagName FROM blog,tag WHERE blog.tagId = tag.id AND blog.id = ?", id));
        return mList;
    }

    public List<Blog> queryAll() {
        //Todo: paginate in next Version
        return Blog.dao.find("SELECT blog.*,tag.name AS tagName FROM blog,tag WHERE blog.tagId = tag.id");
    }

    /**
     * Query without <attr>content<attr/>,because the content is large
     */
    public List<Blog> queryWithoutContent() {
        //Todo: paginate in next Version
        return Blog.dao.find("SELECT blog.id,blog.title,blog.type,blog.abstractStr,blog.createdAt,blog.times,tag.name AS tagName FROM blog,tag WHERE blog.tagId = tag.id");
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
    public boolean publish(String title, String tagName, String type, String abstractStr, String content) {
        Tag targetTag = Tag.dao.findFirst("SELECT id FROM tagName WHERE name = ?", tagName);
        if (targetTag == null) {
            return false;
        }
        Blog aBlog = new Blog();
        aBlog.set("title", title);
        aBlog.set("tagId", targetTag.get("id"));
        aBlog.set("type", type);
        aBlog.set("abstractStr", abstractStr);
        aBlog.set("content", content);
        aBlog.set("createdAt", TimeUtil.getDateTime(System.currentTimeMillis()));
        aBlog.set("times", 0);
        return aBlog.save();
    }

    /**
     * @param id the id of a blog which want to be updated
     */
    public boolean update(int id, String key, Object value) {
        return Blog.dao.findById(id).set(key, value).update();
    }

    /**
     * add the times which be read
     */
    public boolean addTimes(int id) {
        Blog targetBlog = queryById(id).get(0);
        int oldTimes = targetBlog.get("times");
        return update(id, "times", ++oldTimes);
    }
}
