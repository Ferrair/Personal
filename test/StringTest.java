import org.junit.Test;

/**
 * Created on 2016/3/21.
 *
 * @author 王启航
 * @version 1.0
 */
@SuppressWarnings("Duplicates")
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
        String a = "AA" + "A";
        String b = new String("AAA");  //将"AAA"加入常量池 但返回是堆中的地址
        System.out.println(a == b.intern()); // 返回的是在常量池里面的地址
    }

    @Test
    public void test7() {
        String fre = "A";
        String a = "AA" + fre; //并没有把"AAA"加入到常量池里面
        String b = new String("AAA");
        String d = "AAA";
        System.out.println(a == b);
        System.out.println(a == b.intern()); // 返回的是在常量池里面的地址
        System.out.println(a == d);
        System.out.println(b == d);//b 指向堆中  d指向常量池
    }

    @Test
    public void test8() {
        final String fre = "A";
        String a = "AA" + fre; //将"AAA"添加到了常量池
        String b = new String("AAA");
        System.out.println(a == b); //a 指向堆中  b指向常量池
        System.out.println(a == b.intern()); // 返回的是在常量池里面的地址
    }

    @Test
    public void testIntern() {
        String a = new String("ab");
        String b = new String("ab");
        String c = "ab";
        String d = "a" + "b";
        String e = "b";
        String f = "a" + e;

        System.out.println(b.intern() == a);
        System.out.println(b.intern() == c);
        System.out.println(b.intern() == d);
        System.out.println(b.intern() == f);
        System.out.println(b.intern() == a.intern());
    }
}
