package wqh.controller;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

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
        render("index.html");
    }

    @ActionKey("/search_everywhere")
    public void searchEverywhere() {

    }
}
