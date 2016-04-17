package wqh.aop;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2016/3/20.
 * This class is avoid the attack form other,for example,a ip address can enter once during some time
 * <p>
 * Chain-of-Responsibility Design Pattern in Handler.
 * MUST call <code>next.handle(target, request, response, isHandled)<code/> in the end of <code>handle<code/> method;
 *
 * @author 王启航
 * @version 1.0
 */
public class AvoidAttackHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        System.out.println("AvoidAttackHandler=> " + target);
        next.handle(target, request, response, isHandled);
    }
}
