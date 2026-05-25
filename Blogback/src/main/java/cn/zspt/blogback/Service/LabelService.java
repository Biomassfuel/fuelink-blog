package cn.zspt.blogback.Service;

import cn.zspt.blogback.dto.LabelDTO;
import cn.zspt.blogback.dto.LabelQueryParam;
import cn.zspt.blogback.entity.LabelEntity;
import cn.zspt.blogback.vo.PageResultVO;

import java.util.List;

public interface LabelService {
    void addLabel(LabelDTO labelDTO);

    PageResultVO<LabelEntity> getLabel(LabelQueryParam labelQueryParam);

    LabelEntity getLabelById(Integer id);

    void deleteLabel(Integer id);

    void updateLabel(LabelDTO labelDTO, Integer id);

    List<LabelEntity> getAllLabel();

    void updateLabelTotal();
}
