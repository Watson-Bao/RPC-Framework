import com.alibaba.fastjson.JSON;
import com.watson.rpc.api.HelloObject;

public class TestCase {
    public static void main(String[] args) {
        HelloObject helloObject = new HelloObject();
        Class<?> clazz = helloObject.getClass();
        String s = JSON.toJSONString(clazz);
        System.out.println(s);
        Class<?> parse = JSON.parseObject(s, Class.class);
        System.out.println(parse);
    }
}
