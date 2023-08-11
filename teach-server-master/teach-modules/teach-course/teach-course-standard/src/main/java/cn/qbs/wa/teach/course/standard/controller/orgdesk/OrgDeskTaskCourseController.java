//package cn.qbs.wa.teach.course.standard.controller.orgdesk;
//
//import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
//import cn.qbs.wa.teach.common.core.domain.R;
//import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
//import cn.qbs.wa.teach.course.standard.pojo.dto.orgdesk.OrgDeskTaskCourseInfoDTO;
//import cn.qbs.wa.teach.course.standard.service.*;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * 【课程】课程中心(机构前台h5)
// *
// * @author WX
// * @version 1.0
// */
//@Api(tags = "机构前台h5远程接口")
//@Slf4j
//@RestController
//@RequestMapping("orgDeskTask")
//public class OrgDeskTaskCourseController {
//
//    /**
//     * 课程服务对象
//     */
//
//    @Resource
//    private OrgDeskTaskCourseService orgDeskTaskCourseService;
//
//    /**
//     * 查询机构前台h5任务中包含的课程
//     */
//    @AutoSelectOrg
//    @ApiOperation(value = "任务中包含的课程信息")
//    @PostMapping("getCourseList")
//    public R<List<OrgDeskTaskCourseInfoDTO>> detail(@RequestBody @Validated IdListAndUserIdRequest request) {
//        return R.ok(orgDeskTaskCourseService.getCourseList(request));
//    }
//
//}
//
