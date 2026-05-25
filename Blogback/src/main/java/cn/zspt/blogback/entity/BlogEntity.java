package cn.zspt.blogback.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class BlogEntity {
    private Integer id;
    private String title;
    private String summary;
    private String content;
    private String imageUrl;
    private Integer isTop;
    private Integer status;
    private String tags;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private Integer viewCount;
}
