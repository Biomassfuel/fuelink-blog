package cn.zspt.blogback.vo;

import cn.zspt.blogback.entity.BlogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogArchiveResultVO {
    private Integer year;
    private Integer count;
    private List<BlogEntity> articles;
}
