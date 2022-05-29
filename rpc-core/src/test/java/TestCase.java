import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;

import java.util.Date;

public class TestCase {
    public static void main(String[] args) {
        Class<?>[] arrays = new Class[]{int.class, Date.class, String.class};
        Wrapper wrapper = new Wrapper();
        wrapper.setParameterTypes(arrays);
        String s = JSON.toJSONString(wrapper);
        System.out.println(s);
        Wrapper parse = JSON.parseObject(s, Wrapper.class, JSONReader.Feature.SupportClassForName);
        System.out.println(parse);
    }
}
