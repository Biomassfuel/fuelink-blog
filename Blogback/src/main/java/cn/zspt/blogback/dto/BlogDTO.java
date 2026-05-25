package cn.zspt.blogback.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogDTO {
    private Integer id;
    private String title;
    private String summary;
    private String content;
    private String imageUrl;
    private Integer isTop;
    private String tags;
}
