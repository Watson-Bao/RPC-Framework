import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import lombok.SneakyThrows;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Date;


public class TestCase {
    @SneakyThrows
    @Test
    public void testFastJson2() {
        Class<?>[] arrays = new Class[]{int.class, Date.class, String.class};
        Wrapper wrapper = new Wrapper();
        wrapper.setParameterTypes(arrays);
        String s = JSON.toJSONString(wrapper);
        System.out.println(s);
        Wrapper parse = JSON.parseObject(s, Wrapper.class, JSONReader.Feature.SupportClassForName);
        System.out.println(parse);
    }

    @Test
    public void testAddress() {
        InetSocketAddress address = new InetSocketAddress("192.111.123.1", 2222);
        System.out.println(address.getAddress().getHostAddress());
    }
}
