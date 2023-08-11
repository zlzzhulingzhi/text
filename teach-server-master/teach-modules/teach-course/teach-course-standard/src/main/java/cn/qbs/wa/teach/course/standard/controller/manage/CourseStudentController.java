package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.teach.course.common.entity.CourseInfo;
import cn.qbs.wa.teach.course.common.entity.CourseStatisticOverview;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseStudentExcelDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseStudentLearnDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.StudySnapshotDTO;
import cn.qbs.wa.teach.course.standard.service.CourseInfoService;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentStudySnapshotService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.Collator;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【课程学员】(CourseStudent)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Slf4j
@Api(tags = "课程学员")
@RestController
@RequestMapping("/student")
public class CourseStudentController {

    /**
     * 服务对象
     */
    @Resource
    private CourseStudentService courseStudentService;
    @Resource
    private CourseInfoService courseInfoService;
    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;
    @Resource
    private CourseStudentStudySnapshotService studySnapshotService;


    /**
     * 新增【课程学员】
     */
    @ApiOperation(value = "新增课程学员")
    @PostMapping("/add")
    @AccessAuth({UserConstants.USER_EMPLOYEE, SecurityConstants.INNER})
    //@RequiresPermissions("courseStudent:add")
    @Log(title = "新增【课程学员】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseStudentAddRequest params) {
        if (params.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        }
        // 存在的情况下，无需再次新增
        Long courseId = params.getCourseId();
        CourseStudent one = this.courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, params.getUserId()).one();
        if (one != null) {
            return R.ok(true);
        }
        // 报名人数已满
        //CourseInfo courseInfo = courseInfoService.getByCourseId(courseId);
        //if (courseInfo != null) {
        //    Integer signUpLimit = courseInfo.getSignUpLimit();
        //    if (signUpLimit > 0) {
        //        CourseStatisticOverview overview = courseStatisticOverviewService.getByCourseId(courseId);
        //        if (overview != null && overview.getBuyerNum() >= signUpLimit) {
        //            return R.fail("抱歉，课程学习名额已满！");
        //        }
        //    }
        //}
        if (params.getStudentId() == null) {
            params.setStudentId(SecurityContextHolder.getStudentId());
        }
        boolean b = this.courseStudentService.add(params);
        // 刷新课程学员
        this.courseStatisticOverviewService.refreshStatistic(courseId);
        return R.ok(b);
    }

    /**
     * 分页查询【课程学员】
     */
    @ApiOperation(value = "分页查询课程学员的基本信息")
    @PostMapping("/page")
    //@RequiresPermissions("courseStudent:page")
    @Log(title = "分页查询【课程学员】", businessType = BusinessType.OTHER)
    public R<IPage<CourseStudentPageResponse>> page(@RequestBody CourseStudentPageRequest params) {
        return R.ok(this.courseStudentService.page(params));
    }

    /**
     * 查询【课程学员】详情
     *
     * @param request 主键
     */
    @AutoSelectOrg
    @ApiOperation(value = "查询某个学员的详细信息")
    @PostMapping("/detail")
    //@RequiresPermissions("courseStudent:details")
    @Log(title = "【课程学员】详情", businessType = BusinessType.OTHER)
    public R<CourseStudentDetailResponse> detail(@RequestBody @Validated IdOrgRequest request) {
        return R.ok(this.courseStudentService.detail(request.getId()));
    }

    /**
     * 修改【课程学员】
     */
    @ApiOperation(value = "修改课程学员的信息")
    @PostMapping("update")
    //@RequiresPermissions("courseStudent:update")
    @Log(title = "更新【课程学员】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseStudentUpdateRequest params) {
        return R.ok(this.courseStudentService.update(params));
    }

    /**
     * 删除【课程学员】
     *
     * @param request 主键集合
     */
    @ApiOperation(value = "删除一个或多个课程学员")
    @PostMapping("delete")
    //@RequiresPermissions("courseStudent:delete")
    @Log(title = "删除【课程学员】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest request) {
        return R.ok(this.courseStudentService.manualRemoveByIds(request.getIdList()));
    }


    /**
     * 导出学员列表到Excel
     */
    @ApiOperation(value = "导出学员列表到Excel")
    @PostMapping("exportExcel")
    public void exportExcel(HttpServletResponse response, @RequestBody CourseStudentPageRequest request) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户导出-学员列表-"+ LocalDate.now(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<CourseStudentExcelDTO> excelData = this.courseStudentService.generateExcelData(request);
        if (!excelData.isEmpty()) {
            EasyExcel.write(response.getOutputStream(), CourseStudentExcelDTO.class).sheet("sheet1").doWrite(excelData);
        }
    }

    @AutoSelectOrg
    @PostMapping("/drop")
    R<Boolean> dropOut(@RequestBody CourseStudentDropRequest params) {
        if (params.getId() != null) {
            List<Long> ids = new ArrayList<>(1);
            ids.add(params.getId());
            courseStudentService.deleteByIds(ids);
        } else {
            courseStudentService.dropOut(params.getCourseId(), params.getStudentId());
            // 刷新课程学员
            this.courseStatisticOverviewService.refreshStatistic(params.getCourseId());
        }
        return R.ok(Boolean.TRUE);
    }

    /**
     * 查询【课程学员】详情
     *
     * @param request 主键
     */
    @AutoSelectOrg
    @PostMapping("/learn")
    public R<List<StudySnapshotDTO>> learn(@RequestBody CourseStudentLearnDTO request) {
        if (request.getCourseIdList() == null || request.getUserIdList() == null) {
            return R.ok(Collections.emptyList());
        }
        int size = request.getCourseIdList().size() * request.getUserIdList().size();
        if (size <= 0) {
            return R.ok(Collections.emptyList());
        }
        List<StudySnapshotDTO> list = new ArrayList<>(size);
        for (Long userId : request.getUserIdList()) {
            List<StudySnapshotDTO> studyLesson = studySnapshotService.getStudyLesson(userId, request.getCourseIdList());
            if (!studyLesson.isEmpty()) {
                list.addAll(studyLesson);
            }
        }
        return R.ok(list);
    }

    @ApiOperation(value = "批量手动添加课程学员")
    @PostMapping("/batch/add")
    @AccessAuth({UserConstants.USER_EMPLOYEE})
    public R<Boolean> batchAdd(@RequestBody @Validated CourseStudentBatchAddRequest params) {
        if (params.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        }
        Long courseId = params.getCourseId();
        params.getSignUpInfos().stream().map(s -> {
            CourseStudentAddRequest add = BeanUtil.copyProperties(s, CourseStudentAddRequest.class, "orgId");
            add.setCourseId(courseId);
            return add;
        }).forEach(e -> {
            try {
                this.courseStudentService.add(e);
            } catch (Exception exception) {
                log.error(e.toString());
            }
        });
        // 刷新课程学员
        this.courseStatisticOverviewService.refreshStatistic(courseId);
        return R.ok();
    }

    @Resource
    private RemoteStudentService remoteStudentService;

    @GetMapping("/select/page/{courseId}")
    public R<PageResultComDTO<StudentDTO>> pageStudent(@PathVariable Long courseId, StudentSearchDTO searchDTO) {
        R<PageResultComDTO<StudentDTO>> studentList = remoteStudentService.pageWithStaff(searchDTO);
        List<CourseStudent> list = this.courseStudentService.lambdaQuery().select(CourseStudent::getStudentId).eq(CourseStudent::getCourseId, courseId).list();
        if (CollectionUtils.isEmpty(studentList.getData().getRecords())){
            return studentList;
        }
        List<Long> studentIdList = list.stream().map(CourseStudent::getStudentId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(studentList.getData().getRecords())){
            studentList.getData().getRecords().forEach(i -> {
                if (studentIdList.contains(i.getId())){
                    i.setAdded(1);
                }
            });
        }
        return studentList;
    }
}

