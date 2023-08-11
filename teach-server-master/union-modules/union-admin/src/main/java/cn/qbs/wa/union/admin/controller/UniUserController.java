package cn.qbs.wa.union.admin.controller;


import cn.qbs.wa.union.admin.pojo.uniuser.*;
import cn.qbs.wa.union.admin.service.UniUserService;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * 统一用户表(UniUser)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
//@RestController
//@RequestMapping("uniUser")
public class UniUserController {

    /**
     * 服务对象
     */
    @Resource
    private UniUserService uniUserService;


    /**
     * 新增统一用户表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("uniUser:add")
    //@Log(title = "新增统一用户表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated UniUserAddRequest params) {
        return R.ok(this.uniUserService.add(params));
    }

    /**
     * 分页查询统一用户表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("uniUser:page")
    //@Log(title = "分页查询统一用户表", businessType = BusinessType.OTHER)
    public R<IPage<UniUserPageResponse>> page(@RequestBody UniUserPageRequest params) {
        return R.ok(this.uniUserService.page(params));
    }

    /**
     * 查询统一用户表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("uniUser:details")
    //@Log(title = "统一用户表详情", businessType = BusinessType.OTHER)
    public R<UniUserDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.uniUserService.detail(request.getId()));
    }

    /**
     * 修改统一用户表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("uniUser:update")
    //@Log(title = "更新统一用户表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated UniUserUpdateRequest params) {
        return R.ok(this.uniUserService.update(params));
    }

    /**
     * 删除统一用户表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("uniUser:delete")
    //@Log(title = "删除统一用户表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.uniUserService.deleteByIds(request.getIdList()));
    }

}

