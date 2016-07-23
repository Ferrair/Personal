package wqh.config;

import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import wqh.aop.AvoidAttackHandler;
import wqh.controller.*;
import wqh.model.*;

/**
 * Created on 2016/3/12.
 *
 * @author 王启航
 * @version 1.0
 */


public class MainConfig extends JFinalConfig {

    @SuppressWarnings("WeakerAccess")
    public static final String DOWNLOAD_PATH = "file";
    public static final String UPLOAD_PATH = "file";

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setViewType(ViewType.JSP);
        me.setBaseDownloadPath(DOWNLOAD_PATH);
        me.setBaseUploadPath(UPLOAD_PATH);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", IndexController.class);
        me.add("/blog", BlogController.class);
        me.add("/work", WorkController.class);
        me.add("/admin", AdminController.class);
        me.add("/user", UserController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
        PropKit.use("Jdbc.properties");
        C3p0Plugin cp = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
        me.add(cp);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
        me.add(arp);

        arp.addMapping("admin", "username", Admin.class);
        arp.addMapping("blog", Blog.class);
        arp.addMapping("work", Work.class);
        arp.addMapping("comment", Comment.class);
        arp.addMapping("tag", Tag.class);
        arp.addMapping("user", User.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
    }

    //在Jfinal里面是倒序执行的(即在后面添加的Handler先执行，ActionHandler最后执行)
    @Override
    public void configHandler(Handlers me) {
        //me.add(new ContextPathHandler("basePath"));
        //me.add(new AvoidAttackHandler());
    }

}
