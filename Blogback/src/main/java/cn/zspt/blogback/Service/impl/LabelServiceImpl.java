package cn.zspt.blogback.Service.impl;

import cn.zspt.blogback.Mapper.LabelMapper;
import cn.zspt.blogback.Service.LabelService;
import cn.zspt.blogback.dto.LabelDTO;
import cn.zspt.blogback.dto.LabelQueryParam;
import cn.zspt.blogback.entity.LabelEntity;
import cn.zspt.blogback.vo.PageResultVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelMapper labelMapper;
    @Override
    public void addLabel(LabelDTO labelDTO) {
        LabelEntity labelEntity = new LabelEntity();
        BeanUtils.copyProperties(labelDTO, labelEntity);
        labelEntity.setDate(LocalDateTime.now());
        labelEntity.setTotal(0);
        labelMapper.addLabel(labelEntity);

    }

    @Override
    public PageResultVO<LabelEntity> getLabel(LabelQueryParam labelQueryParam) {
        PageHelper.startPage(labelQueryParam.getPage(), labelQueryParam.getPageSize());
        List<LabelEntity> labelList= labelMapper.getLabel(labelQueryParam);
        Page<LabelEntity> page = (Page<LabelEntity>) labelList;
        return new PageResultVO<LabelEntity>(page.getTotal(), page.getResult());
    }

    @Override
    public LabelEntity getLabelById(Integer id) {
        return labelMapper.getLabelById(id);
    }

    @Override
    public void deleteLabel(Integer id) {
        labelMapper.deleteLabel(id);
    }

    @Override
    public void updateLabel(LabelDTO labelDTO, Integer id) {
        LabelEntity labelEntity = labelMapper.getLabelById(id);
        labelEntity.setName(labelDTO.getName());
        labelEntity.setStatus(labelDTO.getStatus());
        labelMapper.updateLabel(labelEntity);
    }

    @Override
    public List<LabelEntity> getAllLabel() {
        return labelMapper.getAllLabel();
    }

    @Override
    public void updateLabelTotal() {
        labelMapper.updateLabelTotal();
    }
}
