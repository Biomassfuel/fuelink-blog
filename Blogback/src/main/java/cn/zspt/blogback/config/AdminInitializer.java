package cn.zspt.blogback.config;

import cn.zspt.blogback.Mapper.LoginMapper;
import cn.zspt.blogback.entity.LoginEntity;
import cn.zspt.blogback.properties.AdminInitializerProperties;
import cn.zspt.blogback.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final AdminInitializerProperties properties;
    private final LoginMapper loginMapper;
    private final PasswordUtil passwordUtil;

    public AdminInitializer(AdminInitializerProperties properties,
                            LoginMapper loginMapper,
                            PasswordUtil passwordUtil) {
        this.properties = properties;
        this.loginMapper = loginMapper;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public void run(String... args) {
        if (!properties.isEnabled()) {
            return;
        }

        if (!StringUtils.hasText(properties.getUsername()) || !StringUtils.hasText(properties.getPassword())) {
            throw new IllegalStateException("Admin initializer is enabled, but username or password is empty");
        }

        if (loginMapper.countByUsername(properties.getUsername()) > 0) {
            log.info("Admin user '{}' already exists, skip initialization", properties.getUsername());
            return;
        }

        LoginEntity admin = new LoginEntity();
        admin.setId(properties.getId());
        admin.setUsername(properties.getUsername());
        admin.setPassword(passwordUtil.hash(properties.getPassword()));
        loginMapper.insert(admin);

        log.warn("Admin user '{}' initialized. Please disable blog.admin-initializer.enabled after first startup.",
                properties.getUsername());
    }
}
