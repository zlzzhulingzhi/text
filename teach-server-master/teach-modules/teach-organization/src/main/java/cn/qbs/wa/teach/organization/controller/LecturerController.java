package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.organization.pojo.lecturer.*;
import cn.qbs.wa.teach.organization.pojo.student.MemberClazzResponse;
import cn.qbs.wa.teach.organization.service.LecturerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 讲师表(Lecturer)表控制层
 *
 * @author makejava
 * @since 2021-11-17 11:25:32
 */
@RestController
@RequestMapping("lecturer")
@Api(tags = "讲师管理")
public class LecturerController {

    /**
     * 服务对象
     */
    @Resource
    private LecturerService lecturerService;


    /**
     * 新增讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("lecturer:add")
    //@Log(title = "新增讲师表", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated LecturerAddRequest params) {
        return R.ok(this.lecturerService.add(params));
    }

    /**
     * 分页查询讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("lecturer:page")
    //@Log(title = "分页查询讲师表", businessType = BusinessType.OTHER)
    @ApiOperation("分页")
    public R<IPage<LecturerPageResponse>> page(@RequestBody LecturerPageRequest params) {
        if(params.getOrgId()!=null){
            SecurityContextHolder.setOrgId(params.getOrgId().toString());
        }
        return R.ok(this.lecturerService.page(params));
    }

    /**
     * 分页查询讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("admin/page")
    //@RequiresPermissions("lecturer:page")
    //@Log(title = "分页查询讲师表", businessType = BusinessType.OTHER)
    @ApiOperation("平台管理讲师选择器")
    public R<IPage<LecturerPageResponse>> pageAdmin(@RequestBody LecturerPageRequest params) {
        return R.ok(this.lecturerService.pageAdmin(params));
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("list")
    //@RequiresPermissions("application:list")
    @Log(title = "列表显示", businessType = BusinessType.OTHER)
    @ApiOperation("列表显示")
    public R<List<LecturerListResponse>> list(@RequestBody LecturerListRequest request) {
        if (request.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
        }
        return R.ok(this.lecturerService.listLecture(request));
    }

    /**
     * 查询讲师表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("lecturer:details")
    //@Log(title = "讲师表详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<LecturerDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.lecturerService.detail(request.getId()));
    }

    /**
     * 修改讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("lecturer:update")
    //@Log(title = "更新讲师表", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated LecturerUpdateRequest params) {
        return R.ok(this.lecturerService.update(params));
    }

    /**
     * 删除讲师表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("lecturer:delete")
    //@Log(title = "删除讲师表", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.lecturerService.deleteByIds(request.getIdList()));
    }

    @ApiOperation("平台管理-机构讲师-综合查询")
    @PostMapping("/plat/page")
    @RequiresPermissions("System:Management:Plat:Lecturer")
    public R<IPage<LecturerResponse>> pageLecturer(@RequestBody LecturerPageQuery params) {
        return R.ok(this.lecturerService.pageLecturerByPlat(params));
    }

    @ApiOperation("平台管理-机构讲师-班级列表")
    @PostMapping("/plat/page-clazz")
    @RequiresPermissions("System:Management:Plat:Lecturer")
    public R<List<LecturerClazzResponse>> pageLecturerClazz(@RequestBody IdRequest id) {
        return R.ok(this.lecturerService.pageLecturerClazz(id.getId()));
    }

}

