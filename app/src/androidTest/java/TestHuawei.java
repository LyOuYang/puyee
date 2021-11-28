import com.example.puyee.utils.NetworkUtils;

import org.junit.Test;


public class TestHuawei {
    @Test
    public void test() {
//        NetworkUtils.getQiaofeiToken();
        byte[] bytes = new byte[2];
        NetworkUtils.getPuyeeRecognize(bytes);
    }
}