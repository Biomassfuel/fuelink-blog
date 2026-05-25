package cn.zspt.blogback.Service;

import cn.zspt.blogback.dto.BlogDTO;
import cn.zspt.blogback.dto.BlogQueryParam;
import cn.zspt.blogback.dto.LabelQueryParam;
import cn.zspt.blogback.entity.BlogEntity;
import cn.zspt.blogback.vo.BlogResultVO;

public interface BlogService {
    void addBlog(BlogDTO blogDTO);

    BlogResultVO<BlogEntity> getBlog(BlogQueryParam blogQueryParam);

    void changeStatus(Integer id, Integer status);

    void deleteBlog(Integer id);

    BlogDTO getBlogAll(Integer id);

    void updateBlog(BlogEntity blogEntity);

    void changeTop(Integer id, Integer isTop);
}
