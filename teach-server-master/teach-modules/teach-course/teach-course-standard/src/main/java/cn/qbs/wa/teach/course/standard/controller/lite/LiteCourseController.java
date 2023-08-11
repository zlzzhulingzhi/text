package cn.qbs.wa.teach.course.standard.controller.lite;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.common.enums.ShelfStatusEnum;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.*;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityResponse;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.LiteBannerListRequest;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.LiteBannerListResponse;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.TBannerPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.TBannerResponse;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentResponse;
import cn.qbs.wa.teach.course.standard.service.LiteCourseService;
import cn.qbs.wa.teach.course.standard.service.TBannerService;
import cn.qbs.wa.teach.course.standard.service.lite.LiteActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程中心(小程序)
 *
 * @author yjx
 * @version 1.0
 */
@Api(tags = "课程(小程序)")
@Slf4j
@RestController
@RequestMapping("/lite")
public class LiteCourseController {

    @Resource
    private LiteCourseService liteCourseService;

    @Resource
    private LiteActivityService liteActivityService;

    @Resource
    private TBannerService tBannerService;

    @ApiOperation(value = "课程中心分页")
    @PostMapping("/page")
    public R<IPage<PageCourseVO>> page(@RequestBody PageCourseDTO request) {
        return R.ok(this.liteCourseService.pageCourse(request));
    }

    @ApiOperation(value = "课程信息-详情")
    @PostMapping("/info")
    public R<CourseInfoDTO> info(@RequestBody @Validated IdRequest request) {
        CourseInfoDTO info = this.liteCourseService.info(request.getId());
        if (ShelfStatusEnum.UP.getCode().equals(info.getShelfStatus())) {
            // 判断当前用户是否报名该课程
            setCourseSignUp(info);
        }
        return R.ok(info);
    }

    @ApiOperation(value = "课程-报名")
    @PostMapping("/sign-up")
    public R<Boolean> signUp(@RequestBody @Validated SignUpDTO request) {
        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
        return R.ok(liteCourseService.signUp(request));
    }

    @ApiOperation(value = "我的课程-分页")
    @PostMapping("/personal/page")
    public R<IPage<MyCoursePageResponse>> page(@RequestBody MyCoursePageRequest params) {
        params.setMemberId(SecurityContextHolder.getUserId());
        return R.ok(this.liteCourseService.pageMyCourse(params));
    }

    @ApiOperation(value = "我的课程-详情")
    @PostMapping("/personal/info")
    public R<CourseInfoDTO> courseInfo(@RequestBody @Validated IdRequest request) {
        CourseInfoDTO info = this.liteCourseService.courseInfo(request.getId());
        info.setIsSignUp(Boolean.TRUE);
        return R.ok(info);
    }

    private void setCourseSignUp(CourseInfoDTO info) {
        info.setIsSignUp(Boolean.FALSE);
        Long userId = SecurityContextHolder.getUserId();
        if (userId != null) {
            SecurityContextHolder.setSelectOrgId(info.getOrgId().toString());
            info.setIsSignUp(liteCourseService.checkCourseSignUp(info.getCourseId(), userId));
        }
    }

    @ApiOperation(value = "机构课程-分页")
    @PostMapping("/org/page")
    public R<IPage<PageCourseVO>> orgPageCourse(@RequestBody PageCourseDTO request) {
        return R.ok(this.liteCourseService.orgPageCourse(request));
    }

    @ApiOperation(value = "机构课程-预报名学员-分页")
    @PostMapping("/org/page-reserve-student")
    public R<IPage<TCourseStudentResponse>> orgPagePreStudent(@RequestBody TCourseStudentPageRequest request) {
        return R.ok(this.liteCourseService.orgPagePreStudent(request));
    }

    @ApiOperation(value = "活动-分页")
    @PostMapping("/activity/page")
    public R<IPage<LiteActivityPageResponse>> pageActivity(@RequestBody LiteActivityPageRequest request) {
        IPage<LiteActivityPageResponse> page = liteActivityService.page(request);
        return R.ok(page);
    }

    @ApiOperation(value = "活动-详情")
    @PostMapping("/activity/detail")
    public R<LiteActivityResponse> pageActivity(@RequestBody @Validated IdRequest request) {
        LiteActivityResponse detail = liteActivityService.detail(request.getId());
        return R.ok(detail);
    }

    @ApiOperation(value = "banner图")
    @PostMapping("/banner")
    public R<List<LiteBannerListResponse>> listBanner(@RequestBody LiteBannerListRequest params) {
        TBannerPageRequest request = new TBannerPageRequest();
        request.setEnabled(Constants.DB_TRUE);
        request.setSection(params.getSection());
        List<TBannerResponse> list = tBannerService.list(request);
        if (CollUtil.isNotEmpty(list)) {
            return R.ok(list.stream().map(e -> {
                LiteBannerListResponse response = new LiteBannerListResponse();
                response.setImgUrl(e.getImgUrl());
                return response;
            }).collect(Collectors.toList()));
        }
        return R.ok(Collections.emptyList());
    }

}
