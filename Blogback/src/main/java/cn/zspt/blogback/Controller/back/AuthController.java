package cn.zspt.blogback.Controller.back;

import cn.zspt.blogback.Mapper.LoginMapper;
import cn.zspt.blogback.entity.LoginEntity;
import cn.zspt.blogback.properties.JwtProperties;
import cn.zspt.blogback.result.Result;
import cn.zspt.blogback.utils.JwtUtil;
import cn.zspt.blogback.utils.PasswordUtil;
import cn.zspt.blogback.vo.LoginResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/backend")
public class AuthController {

    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private LoginMapper loginMapper;

    @PostMapping("/login")
    public Result<LoginResultVO> login(@RequestBody LoginEntity loginEntity) {
        try {
            LoginEntity login = loginMapper.getLogin(loginEntity.getUsername());
            if (login != null && passwordUtil.verify(login.getPassword(), loginEntity.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", login.getId());
                String token = JwtUtil.createJWT(
                        jwtProperties.getAdminSecretKey(),
                        jwtProperties.getAdminTtl(),
                        claims);

                LoginResultVO loginResultVO = LoginResultVO.builder()
                        .id(login.getId())
                        .username(login.getUsername())
                        .token(token)
                        .build();
                return Result.success(loginResultVO);
            }
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            return Result.error("登录失败");
        }
    }
}
