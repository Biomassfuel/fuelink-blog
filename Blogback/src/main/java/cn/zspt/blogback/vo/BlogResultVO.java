package cn.zspt.blogback.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogResultVO<T> {
    private Long total;
    private List<T> row;
}
