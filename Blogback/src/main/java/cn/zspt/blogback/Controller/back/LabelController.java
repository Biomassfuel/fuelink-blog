package cn.zspt.blogback.Controller.back;

import cn.zspt.blogback.Service.LabelService;
import cn.zspt.blogback.dto.LabelDTO;
import cn.zspt.blogback.dto.LabelQueryParam;
import cn.zspt.blogback.entity.LabelEntity;
import cn.zspt.blogback.vo.PageResultVO;
import cn.zspt.blogback.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("backend/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @PostMapping
    public Result<String> addLabel(@RequestBody LabelDTO labelDTO){
//        log.info("添加标签{}", labelDTO);
        labelService.addLabel(labelDTO);
        return Result.success("添加成功");
    }

    @GetMapping
    public Result<PageResultVO> getLabel(LabelQueryParam labelQueryParam){
//        log.info("查询标签{}", labelQueryParam);
        PageResultVO<LabelEntity> pageResultVO = labelService.getLabel(labelQueryParam);
//        log.info("查询结果{}", pageResultVO.getRow());
        return Result.success(pageResultVO);
    }

    @GetMapping("/{id}")
    public Result<LabelEntity> getLabelById(@PathVariable Integer id){
        return Result.success(labelService.getLabelById(id));
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteLabel(@PathVariable Integer id){
        labelService.deleteLabel(id);
        return Result.success("删除成功");
    }

    @PutMapping("/{id}")
    public Result<String> updateLabel(@PathVariable Integer id, @RequestBody LabelDTO labelDTO){
        labelService.updateLabel(labelDTO, id);
        return Result.success("修改成功");
    }

    @GetMapping("/all")
    public Result<List<LabelEntity>> getAllLabel(){
        return Result.success(labelService.getAllLabel());
    }

    @PutMapping("/total")
    public Result<String> updateLabelTotal(){
        labelService.updateLabelTotal();
        return Result.success("更新成功");
    }
}
