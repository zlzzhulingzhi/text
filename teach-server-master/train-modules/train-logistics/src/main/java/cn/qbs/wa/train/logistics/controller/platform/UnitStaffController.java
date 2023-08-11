package cn.qbs.wa.train.logistics.controller.platform;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.train.logistics.pojo.unitstaff.*;
import cn.qbs.wa.train.logistics.service.platform.UnitStaffService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 单位职员表(UnitStaff)表控制层
 *
 * @author makejava
 * @since 2022-09-29 09:04:02
 */
@RestController
@RequestMapping("unitStaff")
@Api(tags = "单位职员管理")
public class UnitStaffController {

    /**
     * 服务对象
     */
    @Resource
    private UnitStaffService unitStaffService;


    /**
     * 新增单位职员表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增单位职员")
    //@RequiresPermissions("unitStaff:add")
    //@Log(title = "新增单位职员表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated UnitStaffAddRequest params) {
        return R.ok(this.unitStaffService.add(params));
    }

    /**
     * 分页查询单位职员表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询单位职员")
    //@RequiresPermissions("unitStaff:page")
    //@Log(title = "分页查询单位职员表", businessType = BusinessType.OTHER)
    public R<IPage<UnitStaffPageResponse>> page(@RequestBody UnitStaffPageRequest params) {
        return R.ok(this.unitStaffService.page(params));
    }

    /**
     * 查询单位职员表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("单位职员详情")
    //@RequiresPermissions("unitStaff:details")
    //@Log(title = "单位职员表详情", businessType = BusinessType.OTHER)
    public R<UnitStaffDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.unitStaffService.detail(request.getId()));
    }

    /**
     * 修改单位职员表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新单位职员")
    //@RequiresPermissions("unitStaff:update")
    //@Log(title = "更新单位职员表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated UnitStaffUpdateRequest params) {
        return R.ok(this.unitStaffService.update(params));
    }

    /**
     * 批量修改单位职员表
     *
     * @param params
     * @return
     */
    @PostMapping("updateBatch")
    @ApiOperation("批量修改单位职员")
    public R<Boolean> updateBatch(@RequestBody @Validated List<UnitStaffUpdateBatchRequest> params) {
        return R.ok(this.unitStaffService.updateBatch(params));
    }

    /**
     * 删除单位职员表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除单位职员")
    //@RequiresPermissions("unitStaff:delete")
    //@Log(title = "删除单位职员表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.unitStaffService.deleteByIds(request.getIdList()));
    }

    @ApiOperation(value = "预导入单位职工信息")
    @PostMapping("preview")
    public R importPre(MultipartFile file, @RequestParam("unitId") Long unitId){
        return R.ok(unitStaffService.importPre(file,unitId));
    }


    /**
     * 批量新增单位职工
     *
     * @param params
     * @return
     */
    @PostMapping("batch/add")
    //@RequiresPermissions("employee:add")
    //@Log(title = "管批量新增单位职工", businessType = BusinessType.INSERT)
    @ApiOperation("批量新增单位职工")
    public R batchAdminAdd(@RequestBody @Validated List<UnitStaffExcelAddRequest> params) {
        return R.ok( unitStaffService.batchAdd(params));
    }

}

