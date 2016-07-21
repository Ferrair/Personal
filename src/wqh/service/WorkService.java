package wqh.service;

import com.jfinal.plugin.activerecord.Page;
import wqh.model.Work;

import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class WorkService extends ServiceAbs {
    public Work publish(String title, String description, String url, String logoUrl) {
        Work aWork = new Work();
        aWork.set("title", title);
        aWork.set("description", description);
        aWork.set("url", url);
        aWork.set("logoUrl", logoUrl);
        if (aWork.save())
            return aWork;
        else return null;
    }

    public Work queryById(int id) {
        return Work.dao.findById(id);
    }

    public List<Work> queryByTitle(String title) {
        return Work.dao.find("SELECT * FROM work WHERE title = ?", title);
    }

    public Page<Work> queryAll(int pageNum) {
        return Work.dao.paginate(pageNum, 10, "SELECT *", "FROM work");
    }

    /**
     * @param id the work id which want to be updated
     */
    public boolean update(int id, String key, String value) {
        return Work.dao.findById(id).set(key, value).update();
    }

    public boolean delete(int id) {
        return Work.dao.deleteById(id);
    }
}
