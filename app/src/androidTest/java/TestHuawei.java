import com.example.puyee.bean.token.Auth;
import com.example.puyee.bean.token.Domain;
import com.example.puyee.bean.token.Identity;
import com.example.puyee.bean.token.Password;
import com.example.puyee.bean.token.Project;
import com.example.puyee.bean.token.Scope;
import com.example.puyee.bean.token.TokenReq;
import com.example.puyee.bean.token.User;
import com.example.puyee.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestHuawei {
    public void testGetToken() {
        TokenReq req = new TokenReq();
        Auth auth = new Auth();
        Identity identity = new Identity();
        Password password = new Password();
        User user = new User();
        Domain domain = new Domain();
        domain.setName("hw_008615274953454_02");
        user.setName("liqiaofei");
        user.setDomain(domain);
        user.setPassword("puyee@liqiaofei");
        password.setUser(user);
        List<String> methods = new ArrayList<>();
        methods.add("password");
        identity.setMethods(methods);
        identity.setPassword(password);
        auth.setIdentity(identity);
        Scope scope = new Scope();
        Project project = new Project();
        project.setName("cn-south-1");
        scope.setProject(project);
        auth.setScope(scope);
        req.setAuth(auth);
        NetworkUtils.getHuaweiToken("myhuaweicloud.com/v3/auth/tokens", req);
    }
}
