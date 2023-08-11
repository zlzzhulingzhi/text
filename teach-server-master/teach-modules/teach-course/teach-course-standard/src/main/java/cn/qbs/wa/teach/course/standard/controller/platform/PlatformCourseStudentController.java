package cn.qbs.wa.teach.course.standard.controller.platform;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.*;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "平台课程学员")
@RestController
@RequestMapping("/platform/student")
public class PlatformCourseStudentController {

    /**
     * 服务对象
     */
    @Resource
    private CourseStudentService courseStudentService;




    /**
     * 分页查询【课程学员】
     */
    @ApiOperation(value = "分页查询课程学员的基本信息")
    @PostMapping("/page")
    @Log(title = "分页查询【课程学员】", businessType = BusinessType.OTHER)
    public R<IPage<CourseStudentPageResponse>> page(@RequestBody CourseStudentPageRequest params) {
        return R.ok(this.courseStudentService.pageV2(params));
    }
}

