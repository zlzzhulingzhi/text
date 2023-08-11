package cn.qbs.wa.train.logistics.controller.inner;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCourseService;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CoursePageResultDTO;
import cn.qbs.wa.train.logistics.pojo.clazz.IntegrateClazzResponse;
import cn.qbs.wa.train.logistics.pojo.lecturer.LecturerClazzMap;
import cn.qbs.wa.train.logistics.service.ClazzLessonArrangeService;
import cn.qbs.wa.train.logistics.service.platform.ClazzPlatService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/clazz/inner")
@Api(tags = "班级内部接口")
public class ClazzInnerController {

    @Resource
    private ClazzPlatService clazzPlatService;

    @Resource
    private ClazzLessonArrangeService lessonArrangeService;

    @Resource
    private RemoteCourseService remoteCourseService;

    @PostMapping("/listClazzByMemberId")
    R<List<IntegrateClazzResponse>> listClazzByMemberId(@RequestBody IdRequest id) {
        List<IntegrateClazzResponse> list = clazzPlatService.listClazzByMemberId(id.getId());
        return R.ok(injectCourseName(list));
    }

    @PostMapping("/listClazzByLecturerId")
    R<List<IntegrateClazzResponse>> listClazzByLecturerId(@RequestBody IdRequest id) {
        List<IntegrateClazzResponse> list = lessonArrangeService.listClazzByLecturerId(id.getId());
        return R.ok(injectCourseName(list));
    }

    @PostMapping("/queryClazzLastByLecturerIds")
    R<Map<Long, String>> queryClazzLastByLecturerIds(@RequestBody IdListRequest request) {
        Map<Long, String> map = new HashMap<>(request.getIdList().size());
        if (CollUtil.isNotEmpty(request.getIdList())) {
            List<LecturerClazzMap> l = lessonArrangeService.queryClazzLastByLecturerIds(request.getIdList());
            for (LecturerClazzMap lecturerClazzMap : l) {
                map.put(lecturerClazzMap.getLecturerId(), lecturerClazzMap.getClazzName());
            }
        }
        return R.ok(map);
    }

    private List<IntegrateClazzResponse> injectCourseName(List<IntegrateClazzResponse> list) {
        if (CollUtil.isNotEmpty(list)) {
            // 远程获取课程名称
            List<Long> courseIds = list.stream().map(IntegrateClazzResponse::getCourseId).distinct().collect(Collectors.toList());
            R<List<CoursePageResultDTO>> r = remoteCourseService.listByIds(new IdListRequest(courseIds, null));
            if (!r.isOk()) {
                log.error("课程接口异常: {}", r.getMsg());
            }
            Map<Long, String> map = r.getData().stream().collect(Collectors.toMap(CoursePageResultDTO::getId, CoursePageResultDTO::getCourseName, (a, b) -> a));
            for (IntegrateClazzResponse response : list) {
                response.setCourseName(map.get(response.getCourseId()));
            }
        }
        return list;
    }
}
