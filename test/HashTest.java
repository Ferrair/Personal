import org.junit.Test;

/**
 * Created on 2016/4/26.
 *
 * @author 王启航
 * @version 1.0
 */
public class HashTest {

    //hash算法产生的不是定长的int类型 但是和length - 1求并之后，会映射在数组之上，，所以会把这些数据存在一个length的数组之上
    @Test
    public void testHash() {
        System.out.println(hash("jioj") & 15);
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
