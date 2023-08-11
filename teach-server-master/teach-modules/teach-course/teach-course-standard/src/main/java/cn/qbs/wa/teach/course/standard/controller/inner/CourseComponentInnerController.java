package cn.qbs.wa.teach.course.standard.controller.inner;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentExtraDTO;
import cn.qbs.wa.teach.course.standard.service.CourseComponentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Api(tags = "讲次内容")
@RestController
@RequestMapping("inner/courseComponent")
public class CourseComponentInnerController {

    @Resource
    private CourseComponentService courseComponentService;

    @ApiOperation(value = "附件内容-列表-V2")
    @PostMapping("/v2/listByLesson")
    @Log(title = "附件内容-列表", businessType = BusinessType.OTHER)
    public R<List<CourseComponentExtraDTO>> listByLessonV2(@RequestBody @Validated IdRequest request) {
        List<CourseComponent> courseComponents=courseComponentService.lambdaQuery().eq(CourseComponent::getCourseId,request.getId()).list();
        List<CourseComponentExtraDTO> courseComponentList=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(courseComponents)){
            List<Long> lecturerIds=courseComponents.stream().map(CourseComponent::getLessonId).collect(Collectors.toList());
            List<Long> newLecturerIds=new ArrayList<>(new HashSet<>(lecturerIds));
            for (Long id:newLecturerIds) {
                List<CourseComponentExtraDTO> list= this.courseComponentService.listByLessonV2(id);
                courseComponentList.addAll(list);
            }
        }
        return R.ok(courseComponentList);
    }
}

