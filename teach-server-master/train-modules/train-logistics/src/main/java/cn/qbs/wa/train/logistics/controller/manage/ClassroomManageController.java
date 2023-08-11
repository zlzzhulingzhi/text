package cn.qbs.wa.train.logistics.controller.manage;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.pojo.classroom.*;
import cn.qbs.wa.train.logistics.service.manage.ClassroomManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 教室表(Classroom)表控制层
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
@RestController
@RequestMapping("manageClassroom")
@Api(tags = "教室管理")
public class ClassroomManageController {

  /** 服务对象 */
  @Resource private ClassroomManageService classroomManageService;

  @PostMapping("getManageClassroomState")
  @ApiOperation("机构教室预订分布")
  @RequiresPermissions("Org:Classroom:Schedule")
  public R<IPage<ClassroomPageResponse>> getManageClassroomState(@RequestBody ClassroomPageRequest params) {
    //params.setOrgId(SecurityContextHolder.getOrgId());
    return R.ok(this.classroomManageService.getClassroomState(params));
  }

  @PostMapping("managePage")
  @ApiOperation("分页查询教室")
  public R<IPage<ClassroomPageResponse>> page(@RequestBody ClassroomPageRequest params) {
    return R.ok(this.classroomManageService.page(params));
  }

  @PostMapping("manageDetail")
  @ApiOperation("教室详情")
  public R<ClassroomDetailResponse> detail(@RequestBody IdRequest request) {
    return R.ok(this.classroomManageService.detail(request.getId()));
  }


}
