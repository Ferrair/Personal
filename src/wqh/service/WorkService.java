package wqh.service;

import wqh.model.Work;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */
public class WorkService extends ServiceAbs {
    public boolean publish(String title, String description, String url, String logoUrl) {
        Work aWork = new Work();
        aWork.set("title", title);
        aWork.set("description", description);
        aWork.set("url", url);
        aWork.set("logoUrl", logoUrl);
        return aWork.save();
    }

    public List<Work> queryById(int id) {
        List<Work> mList = new ArrayList<>();
        mList.add(Work.dao.findById(id));
        return mList;
    }

    public List<Work> queryByTitle(String title) {
        return Work.dao.find("SELECT * FROM work WHERE title = ?", title);
    }

    public List<Work> queryAll() {
        return Work.dao.find("SELECT * FROM work");
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
