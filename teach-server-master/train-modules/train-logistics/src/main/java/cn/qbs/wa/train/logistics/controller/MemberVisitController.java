package cn.qbs.wa.train.logistics.controller;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.pojo.membervisit.*;
import cn.qbs.wa.train.logistics.service.MemberVisitService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;


/**
 * 学员访问管理(MemberVisit)表控制层
 *
 * @author makejava
 * @since 2022-12-28 16:24:21
 */
@RestController
@RequestMapping("memberVisit")
@Api(tags = "学员访问管理")
public class MemberVisitController {

    /**
     * 服务对象
     */
    @Resource
    private MemberVisitService memberVisitService;


    /**
     * 新增学员访问管理
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增学员访问")
    //@RequiresPermissions("memberVisit:add")
    //@Log(title = "新增学员访问管理", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated MemberVisitAddRequest params) {

        return R.ok(this.memberVisitService.add(params));
    }

    /**
     * 分页查询学员访问管理
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询员访问")
    @RequiresPermissions("Management:Visitor:Record")
    //@RequiresPermissions("memberVisit:page")
    //@Log(title = "分页查询学员访问管理", businessType = BusinessType.OTHER)
    public R<IPage<MemberVisitPageResponse>> page(@RequestBody MemberVisitPageRequest params) {

        return R.ok(this.memberVisitService.page(params));
    }

    @PostMapping("lite/page")
    @ApiOperation("分页查询员访问")
    //@RequiresPermissions("memberVisit:page")
    //@Log(title = "分页查询学员访问管理", businessType = BusinessType.OTHER)
    public R<IPage<MemberVisitPageResponse>> pageLite(@RequestBody MemberVisitPageRequest params) {

        return R.ok(this.memberVisitService.pageLite(params));
    }

    /**
     * 查询学员访问管理详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("学员访问详情")
    //@RequiresPermissions("memberVisit:details")
    //@Log(title = "学员访问管理详情", businessType = BusinessType.OTHER)
    public R<MemberVisitDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.memberVisitService.detail(request.getId()));
    }

    /**
     * 修改学员访问管理
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("修改学员访问")
    //@RequiresPermissions("memberVisit:update")
    //@Log(title = "更新学员访问管理", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated MemberVisitUpdateRequest params) {
        return R.ok(this.memberVisitService.update(params));
    }

    /**
     * 删除学员访问管理
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除学员访问")
    //@RequiresPermissions("memberVisit:delete")
    //@Log(title = "删除学员访问管理", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.memberVisitService.deleteByIds(request.getIdList()));
    }

}

