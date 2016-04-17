import org.junit.Test;

import java.lang.ref.WeakReference;

/**
 * Created on 2016/3/21.
 *
 * @author 王启航
 * @version 1.0
 */
public class RefrenceTest {
    @Test
    public void WeakReferenceTest() {
        String s = new String("WQH");
        WeakReference<String> wr = new WeakReference<>(s);
        s = null;
        while (wr.get() != null) {
            System.out.println("WeakReference get :" + wr.get());
            System.gc();
            System.out.println("System.gc() " + wr.get());
        }
    }
}
