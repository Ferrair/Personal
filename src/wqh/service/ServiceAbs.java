package wqh.service;

import com.jfinal.core.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2016/3/12.
 * <p>
 * This class is the base-abstract class for all <code>Service<code/> in this package.
 * It can be used to generate a <code>ServiceImpl<code/> which do the work belong to <code>Controller<code/> before.
 * So decrease the number of the work of <code>Controller<code/>
 *
 * @author 王启航
 * @version 1.0
 */
public abstract class ServiceAbs {
    protected Controller mController;
    private static Map<Class<? extends ServiceAbs>, ServiceAbs> serviceMap = new HashMap<>();

    /**
     * @param clazz      The Service that want to be instanced
     * @param controller The Controller that can control the Service
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static <Service extends ServiceAbs> Service getInstance(Class<Service> clazz, Controller controller) {
        Service mService = (Service) serviceMap.get(clazz);
        if (mService == null) {
            try {
                mService = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            serviceMap.put(clazz, mService);
        }
        mService.mController = controller;
        return mService;

    }
}
