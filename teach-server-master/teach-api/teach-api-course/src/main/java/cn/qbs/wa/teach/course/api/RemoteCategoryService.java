package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCategoryFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.category.CategoryAddRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.category.CategoryDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.category.CategoryTreeResultDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.category.CategoryTreeSearchDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteCourseCategoryService", name = "teach-course", path = "course/category", fallbackFactory = RemoteCategoryFallbackFactory.class)
public interface RemoteCategoryService {

    /**
     * 【课程分类】列表
     */
    @ApiOperation(value = "树形列表查询")
    @PostMapping("/tree")
    R<List<CategoryTreeResultDTO>> tree(@RequestBody CategoryTreeSearchDTO params);

    /**
     * 新增【课程分类】
     *
     */
    @ApiOperation(value = "初始化")
    @PostMapping("/init")
    R<CategoryDTO> init(@RequestBody CategoryAddRequestDTO params);
}
