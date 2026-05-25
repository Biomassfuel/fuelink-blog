package cn.zspt.blogback.dto;

import lombok.Data;

@Data
public class BlogAllQueryParam {
    private Integer page =1;
    private Integer pageSize =10;
    private Integer tagId;
    private String titleSearch;
}
