package cn.zspt.blogback.Mapper;

import cn.zspt.blogback.dto.BlogQueryParam;
import cn.zspt.blogback.dto.LabelQueryParam;
import cn.zspt.blogback.entity.BlogEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BlogMapper {
    /**
     * 添加文章
     * @param blogEntity
     */
    void addBlog(BlogEntity blogEntity);

    /**
     * 查询文章
     * @param blogQueryParam
     * @return
     */
    List<BlogEntity> getBlog(BlogQueryParam blogQueryParam);

    /**
     * 修改文章状态
     * @param id
     * @param status
     */
    @Update("update articles set status = #{status} where id = #{id}")
    void changeStatus(Integer id, Integer status);

    /**
     * 删除文章
     * @param id
     */
    @Delete("delete from articles where id = #{id}")
    void deleteBlog(Integer id);

    /**
     * 获取文章所有信息
     * @param id
     * @return
     */
    @Select("select * from articles where id = #{id}")
    BlogEntity getBlogAll(Integer id);

    /**
     * 修改文章
     * @param blogEntityNew
     */
    void updateBlog(BlogEntity blogEntityNew);

    /**
     * 修改文章置顶状态
     * @param id
     * @param isTop
     */
    @Update("update articles set isTop = #{isTop} where id = #{id}")
    void changeTop(Integer id, Integer isTop);
}
