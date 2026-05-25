package cn.zspt.blogback.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class FriendQueryParam {
    private Integer page =1;
    private Integer pageSize =10;
}
