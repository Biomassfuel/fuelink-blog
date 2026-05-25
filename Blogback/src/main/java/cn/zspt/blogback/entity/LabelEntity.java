package cn.zspt.blogback.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabelEntity {
    private Integer id;
    private String name;
    private LocalDateTime date;//创建时间
    private Integer status;
    private Integer total;//文章数
}
