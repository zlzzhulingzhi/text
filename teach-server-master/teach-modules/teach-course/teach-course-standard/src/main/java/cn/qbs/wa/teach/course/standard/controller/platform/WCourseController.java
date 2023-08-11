package cn.qbs.wa.teach.course.standard.controller.platform;
//
//
//import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
//import cn.qbs.wa.teach.common.core.domain.IdListRequest;
//import cn.qbs.wa.teach.common.core.domain.IdRequest;
//import cn.qbs.wa.teach.common.core.domain.R;
//import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
//import cn.qbs.wa.teach.course.standard.pojo.wcourse.*;
//import cn.qbs.wa.teach.course.standard.service.WCourseService;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCourseUpdateRequest;
import cn.qbs.wa.teach.course.standard.service.WCourseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
//
//import javax.annotation.Resource;
//
//
//
///**
// * 插件-课程表(WCourse)表控制层
// *
// * @author makejava
// * @since 2022-03-01 14:25:16
// */
@Api(tags = "机构课程")
@RestController
@RequestMapping("WCourse")
public class WCourseController {
//
//    /**
//     * 服务对象
//     */
    @Resource
    private WCourseService wCourseService;
//
//
//    /**
//     * 新增插件-课程表
//     *
//     * @param params
//     * @return
//     */
//    @PostMapping("add")
//    //@RequiresPermissions("wCourse:add")
//    //@Log(title = "新增插件-课程表", businessType = BusinessType.INSERT)
//    @AutoSelectOrg
//    public R<Boolean> add(@RequestBody @Validated WCourseAddRequest params) {
//        return R.ok(this.wCourseService.add(params));
//    }
//
//    /**
//     * 分页查询插件
//     *
//     * @param params
//     * @return
//     */
    @PostMapping("page")
    @ApiOperation(value = "查询")
    @RequiresPermissions("System:Management:Plat:Course")
//    //@Log(title = "分页查询插件-课程表", businessType = BusinessType.OTHER)
    public R<IPage<WCoursePageResponse>> page(@RequestBody WCoursePageRequest params) {
        return R.ok(this.wCourseService.page(params));
    }

    @PostMapping("pageV2")
    @ApiOperation(value = "查询")
    @RequiresPermissions("System:Management:Plat:CoursePlus")
//    //@Log(title = "分页查询插件-课程表", businessType = BusinessType.OTHER)
    public R<IPage<WCoursePageResponse>> pageV2(@RequestBody WCoursePageRequest params) {
        return R.ok(this.wCourseService.pageV2(params));
    }
//
//    /**
//     * 分页查询插件
//     *
//     * @param params
//     * @return
//     */
//    @PostMapping("/search")
//    public R<IPage<WCoursePageResponse>> search(@RequestBody WCoursePageRequest params) {
//        return R.ok(this.wCourseService.search(params));
//    }
//
//    /**
//     * 分页查询插件
//     *
//     * @param params
//     * @return
//     */
//    @PostMapping("pageByChild")
//    //@RequiresPermissions("wCourse:page")
//    //@Log(title = "分页查询插件-课程表", businessType = BusinessType.OTHER)
//    public R<IPage<WCoursePageByChildResponse>> pageByChild(@RequestBody WCoursePageRequest params) {
//        return R.ok(this.wCourseService.pageByChild(params));
//    }
//
//    /**
//     * 查询插件-课程表详情
//     *
//     * @param id 主键
//     * @return
//     */
//    @PostMapping("detail")
//    //@RequiresPermissions("wCourse:details")
//    //@Log(title = "插件-课程表详情", businessType = BusinessType.OTHER)
//    public R<WCourseDetailResponse> detail(@RequestBody IdRequest request) {
//        return R.ok(this.wCourseService.detail(request.getId()));
//    }
//
//    /**
//     * 修改插件-课程表
//     *
//     * @param params
//     * @return
//     */
    @PostMapping("update")
    @ApiOperation(value = "修改排序")
    @RequiresPermissions("System:Management:Plat:CoursePlus")
//    //@RequiresPermissions("wCourse:update")
//    //@Log(title = "更新插件-课程表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated WCourseUpdateRequest params) {
//        if (params.getOrgId() != null) {
//            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
//        }
        return R.ok(this.wCourseService.update(params));
    }
//
//    /**
//     * 删除插件-课程表
//     *
//     * @param idList 主键集合
//     * @return
//     */
    @PostMapping("delete")
    @ApiOperation(value = "下架")
    @RequiresPermissions("System:Management:Plat:CoursePlus")
//    //@RequiresPermissions("wCourse:delete")
//    //@Log(title = "删除插件-课程表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.wCourseService.deleteByIds(request.getIdList()));
    }
//
}
//
