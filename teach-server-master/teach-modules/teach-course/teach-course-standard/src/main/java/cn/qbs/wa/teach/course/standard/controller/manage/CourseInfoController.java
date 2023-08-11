package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.common.entity.CourseStatisticOverview;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseContentResponse;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.*;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview.CourseStatisticOverviewDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseCategoryDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import cn.qbs.wa.teach.course.standard.service.CourseInfoService;
import cn.qbs.wa.teach.course.standard.service.CourseService;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import cn.qbs.wa.teach.course.standard.service.RemoteService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 【课程信息】(CourseInfo)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Api(tags = "课程信息展示")
@RestController
@RequestMapping("/info")
public class CourseInfoController {

    /**
     * 课程服务对象
     */
    @Resource
    private CourseService courseService;
    @Resource
    private CourseInfoService courseInfoService;
    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;
    @Resource
    private RemoteService remoteService;

    /**
     * 分页查询【课程信息】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "分页")
    @PostMapping("/page")
    public R<IPage<CourseInfoPageResponse>> page(@RequestBody CourseInfoPageRequest params) {
        return R.ok(this.courseInfoService.page(params));
    }

    /**
     * 查询【课程信息】详情
     *
     * @param request 主键
     * @return
     */
    @ApiOperation(value = "详情")
    @PostMapping
    public R<CourseInfoDetailResponse> detail(@RequestBody @Validated IdRequest request) {
        Long courseId = request.getId();
        CourseContentResponse courseContentResponse = this.courseService.detailContent(courseId);
        CourseInfoDetailResponse detail = this.courseInfoService.detail(courseId);
        detail.setContent(courseContentResponse);
        return R.ok(detail);
    }

    /**
     * 查询【课程信息】详情
     *
     * @param request 主键
     * @return
     */
    @ApiOperation(value = "详情")
    @PostMapping("/detail")
    public R<CourseInfoDetailResponse> info(@RequestBody @Validated IdRequest request) {
        CourseInfoDetailResponse detail = this.courseInfoService.detail(request.getId());
        if (CollUtil.isNotEmpty(detail.getLecturers())) {
            CourseInfoDTO courseInfoDTO = new CourseInfoDTO();
            courseInfoDTO.setLecturers(detail.getLecturers());
            // 获取讲师信息
            remoteService.setLecturers(courseInfoDTO);
            detail.setLecturers(courseInfoDTO.getLecturers());
            String categoryName = detail.getCategories().stream().map(CourseCategoryDTO::getCategoryName).collect(Collectors.joining(","));
            detail.setCategoryName(categoryName);
        }
        return R.ok(detail);
    }

    /**
     * 修改【课程信息】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    //@RequiresPermissions("courseInfo:update")
    //@Log(title = "更新【课程信息】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseInfoUpdateRequest params) {
        return R.ok(this.courseInfoService.update(params));
    }

    @ApiOperation(value = "章节模式修改")
    @PostMapping("/setting-mode")
    public R<Boolean> settingMode(@RequestBody @Validated CourseModeSettingRequest params) {
        return R.ok(this.courseInfoService.settingMode(params));
    }

    @ApiOperation(value = "课程统计信息-详情")
    @PostMapping("/statistic")
    //@RequiresPermissions("courseStatisticOverview:details")
//    @Log(title = "【课程统计信息】详情", businessType = BusinessType.OTHER)
    public R<CourseStatisticOverviewDetailResponse> statistic(@RequestBody @Validated IdRequest params) {
        CourseStatisticOverviewDetailResponse detailResponse = new CourseStatisticOverviewDetailResponse();
        CourseStatisticOverview statisticOverview = this.courseStatisticOverviewService.getByCourseId(params.getId());
        BeanUtil.copyProperties(statisticOverview, detailResponse);
        Optional.ofNullable(this.courseService.getById(params.getId())).ifPresent(course -> {
            detailResponse.setCourseName(course.getCourseName());
            detailResponse.setCoverUrl(course.getCoverUrl());
        });
        return R.ok(detailResponse);
    }

    @ApiOperation(value = "报名密令验证")
    @PostMapping("/signAuth")
    public R<Boolean> signAuth(@RequestBody @Validated SiginAuthRequest params) {
        return R.ok(this.courseInfoService.signAuth(params));
    }
}

