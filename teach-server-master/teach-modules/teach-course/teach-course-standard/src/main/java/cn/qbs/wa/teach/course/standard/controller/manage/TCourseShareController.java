package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.SysUser;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.oss.utils.TencentCosUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.common.weixin.miniapp.service.MiniAppCodeService;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.enums.ShelfStatusEnum;
import cn.qbs.wa.teach.course.standard.entity.TCourse;
import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.courseshare.CourseShareRequest;
import cn.qbs.wa.teach.course.standard.pojo.courseshare.CourseShareResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseStudentExcelDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.TCourseStudentExcelDTO;
import cn.qbs.wa.teach.course.standard.pojo.tcourse.TCoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcourse.TCoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentAck;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentUpdateRequest;
import cn.qbs.wa.teach.course.standard.service.CourseService;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import cn.qbs.wa.teach.course.standard.service.TCourseService;
import cn.qbs.wa.teach.course.standard.service.TCourseStudentService;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yjx
 */
@Slf4j
@Api(tags = "课程小程序")
@RestController
@RequestMapping("/share")
public class TCourseShareController {

    @Resource
    private CourseService courseService;

    @Resource
    private TCourseService tCourseService;

    @Resource
    private TCourseStudentService tCourseStudentService;

    @Resource
    private MiniAppCodeService miniAppCodeService;

    @Resource
    private TencentCosUtil tencentCosUtil;

    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;

    @ApiOperation("二维码海报信息")
    @SneakyThrows
    @PostMapping
    public R<CourseShareResponse> share(@RequestBody @Validated CourseShareRequest request) {
        Course course = courseService.getById(request.getCourseId());
        if (ShelfStatusEnum.DOWN.getCode().equals(course.getShelfStatus())) {
            return R.fail("课程已下架, 无法分享");
        }
        TCourse tCourse = tCourseService.lambdaQuery().eq(TCourse::getCourseId, request.getCourseId()).one();
        if (tCourse == null) {
            tCourse = new TCourse();
            tCourse.setCourseId(request.getCourseId());
        }
        if (StrUtil.isBlank(tCourse.getQrcodeUrl())) {
            File miniAppCodeFile = this.miniAppCodeService.createMiniAppCode(request.getPage(), request.getScene());
            if (miniAppCodeFile != null) {
                try {
                    String url = tencentCosUtil.putObject(miniAppCodeFile, "/train/wxQrcode/miniAppCode/");
                    if (url != null) {
                        tCourse.setQrcodeUrl(url);
                    }
                } finally {
                    miniAppCodeFile.delete();
                }
            }
            tCourseService.saveOrUpdate(tCourse);
        }
        CourseShareResponse response = new CourseShareResponse();
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        response.setQrcodeUrl(tCourse.getQrcodeUrl());
        response.setPhone(sysUser.getAccount());
        response.setRealName(sysUser.getUserName());
        return R.ok(response);
    }

    @ApiOperation("课程预报名学员分页")
    @PostMapping("/page-reserve-student")
    public R<IPage<TCourseStudentPageResponse>> pageCourseStudent(@RequestBody @Validated TCourseStudentPageRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.tCourseStudentService.pageCourseStudent(params));
    }

    @ApiOperation("预报名学员的课程列表")
    @PostMapping("/list-course")
    public R<List<TCoursePageResponse>> listCourse(@RequestBody TCoursePageRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.tCourseService.listCourse(params));
    }
    @ApiOperation("预报名学员的数量")
    @PostMapping("/count")
    public R<Long> count(@RequestBody TCoursePageRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        TCourse tCourse=tCourseService.getById(params.getIdList().get(Constants.DB_FAIL));
        Long count=tCourseStudentService.lambdaQuery().eq(TCourseStudent::getCourseId,tCourse.getCourseId()).eq(TCourseStudent::getOrgId,params.getOrgId()).count();
        return R.ok(count);
    }

    @ApiOperation("课程预报名学员-确认报名")
    @PostMapping("/ack-student")
    public R<List<TCourseStudentAck>> ack(@RequestBody @Validated List<IdRequest> request) {
        List<TCourseStudentAck> tCourseStudentAckList=new ArrayList<>();
        for (IdRequest idRequest:request) {
            List<TCourseStudentAck> list1=tCourseStudentService.ack(idRequest);
            tCourseStudentAckList.addAll(list1);
        }
        if (CollUtil.isNotEmpty(tCourseStudentAckList)) {
            // 刷新课程学员
            Long courseId = tCourseStudentAckList.get(0).getCourseStudent().getCourseId();
            this.courseStatisticOverviewService.refreshStatistic(courseId);
        }
        return R.ok(tCourseStudentAckList);
    }

    @ApiOperation(value = "导出预报学员列表到Excel")
    @PostMapping("exportExcel")
    public void exportExcel(HttpServletResponse response, @RequestBody TCourseStudentPageRequest request) throws IOException {
        if(request.getOrgId()==null){
            throw new ServiceException("请选择要导出的机构");
        }
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户导出-预报学员列表-"+ LocalDate.now(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<TCourseStudentExcelDTO> excelData = this.tCourseStudentService.generateExcelData(request);
        if (!excelData.isEmpty()) {
            EasyExcel.write(response.getOutputStream(), TCourseStudentExcelDTO.class).sheet("sheet1").doWrite(excelData);
        }
    }

}
