package cn.qbs.wa.teach.exam.admin.controller;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.teach.exam.admin.service.ExamUserVisibleService;
import cn.qbs.wa.teach.exam.admin.service.ExamineeLiveRoomService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "学生远程服务请求接口")
@RestController
@RequiredArgsConstructor
public class StudentController {

    /**
     * 学生服务
     */
    @Resource
    private RemoteStudentService remoteStudentService;

    @Resource
    private ExamUserVisibleService examUserVisibleService;

    /**
     * 获取机构学员列表
     * @param params 查询参数
     * @return 学员列表
     */
    @AutoSelectOrg
    @ApiOperation(value = "获取机构学员列表")
    @PostMapping("/student/page")
    public R<PageResultComDTO<StudentDTO>> pageStudent(@RequestBody StudentSearchDTO params) {
        return remoteStudentService.pageWithStaff(params);
    }

    /**
     * 删除【课程可见学员】
     */
    @ApiOperation(value = "删除可见学员")
    @PostMapping("/remove/student")
    public R<Boolean> removeStudent(@RequestBody @Validated IdListRequest request) {
        return R.ok(this.examUserVisibleService.deleteByIds(request.getIdList()));
    }

}
