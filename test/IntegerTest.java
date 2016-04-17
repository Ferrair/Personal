import org.junit.Test;

/**
 * Created on 2016/3/21.
 *
 * @author 王启航
 * @version 1.0
 */
public class IntegerTest {
    @Test
    public void test() {
        Integer a = new Integer(1);
        Integer b = new Integer(1);

        System.out.println(a == b); //false
    }

    // 当基本类型包装类与基本类型值进行==运算时，包装类会自动拆箱。即比较的是基本类型值。
    // 具体实现上，是调用了Integer.intValue()方法实现拆箱。
    @Test
    public void test1() {
        int a = 1;
        Integer b = 1;
        Integer c = new Integer(1);

        System.out.println(a == b); //true
        System.out.println(a == c); //true
        System.out.println(c == b); //false
    }

    // Integer a = 1; =>  Integer a = Integer.valueOf(1);
    // Integer已经默认创建了数值【-128-127】的Integer常量池
    // 在之间的值就直接在里面取
    @Test
    public void test2() {
        Integer a = 127;
        Integer b = 127;
        System.out.println(a == b); //true

    }

    @Test
    public void test3() {
        Integer a = 128;
        Integer b = 128;
        System.out.println(a == b); //false

    }

    // Java的数学计算是在内存栈里操作的
    // c1 + c2 会进行拆箱，比较还是基本类型
    @Test
    public void test4() {
        int a = 0;

        Integer b1 = 1000;
        Integer c1 = new Integer(1000);

        Integer b2 = 0;
        Integer c2 = new Integer(0);

        System.out.println(b1 == b1 + b2); //true
        System.out.println(c1 == c1 + c2); //true

        System.out.println(b1 == b1 + a); //true
        System.out.println(c1 == c1 + a); //true
    }
}
