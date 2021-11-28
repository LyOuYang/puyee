import com.example.puyee.bean.token.TokenReq;
import com.example.puyee.utils.NetworkUtils;

import java.io.IOException;

public class TestHuawei {
    public void testGetToken() {
        TokenReq req = new TokenReq();
        NetworkUtils.getHuaweiToken("myhuaweicloud.com/v3/auth/tokens", req);
    }
}
