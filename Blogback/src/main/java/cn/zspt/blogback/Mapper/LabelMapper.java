package cn.zspt.blogback.Mapper;

import cn.zspt.blogback.dto.LabelDTO;
import cn.zspt.blogback.dto.LabelQueryParam;
import cn.zspt.blogback.entity.LabelEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LabelMapper {
    /**
     * 添加标签
     * @param labelEntity
     */

    @Insert("insert into label(name, date, status, total) values(#{name}, #{date}, #{status}, #{total})")
    void addLabel(LabelEntity labelEntity);

    /**
     * 查询标签
     * @param labelQueryParam
     * @return
     */
    List<LabelEntity> getLabel(LabelQueryParam labelQueryParam);

    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    @Select("select * from label where id = #{id}")
    LabelEntity getLabelById(Integer id);

    /**
     * 删除标签
     * @param id
     */
    @Insert("delete from label where id = #{id}")
    void deleteLabel(Integer id);


    /**
     * 修改标签
     * @param labelEntity
     */
    @Insert("update label set name = #{name}, status = #{status} where id = #{id}")
    void updateLabel(LabelEntity labelEntity);

    /**
     * 查询所有标签
     * @return
     */
    @Select("select * from label")
    List<LabelEntity> getAllLabel();

    /**
     * 修改标签文章数
     */
    void updateLabelTotal();
}
