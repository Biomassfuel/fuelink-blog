package cn.zspt.blogback.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blog.admin-initializer")
@Data
public class AdminInitializerProperties {

    private boolean enabled = false;
    private Integer id = 1;
    private String username;
    private String password;
}
