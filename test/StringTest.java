import org.junit.Test;

/**
 * Created on 2016/3/21.
 *
 * @author 王启航
 * @version 1.0
 */
public class StringTest {
    @Test
    public void test1() {
        String a = "AAA";
        String b = new String("AAA");
        System.out.println(a == b); // false
    }

    @Test
    public void test2() {
        String a = "AAA";
        String b = "AAA";
        System.out.println(a == b); // true
        a = "DDD";
        System.out.println(a == b); // false
        System.out.println(a); // DDD
        System.out.println(b); // DDD
    }

    @Test
    public void test3() {
        String a = "AA1";
        String b = "AA" + 1;
        System.out.println(a == b); // true
    }

    @Test
    public void test4() {
        String a = "AA1";
        int i = 1;
        String b = "AA" + i;
        System.out.println(a == b); // false
    }

    @Test
    public void test5() {
        String a = "AA1";
        final int i = 1;
        String b = "AA" + i;
        System.out.println(a == b); // true
    }

    @Test
    public void test6() {
        String a = "AAA";
        String b = new String("AAA");
        System.out.println(a == b.intern()); // true
    }
}
