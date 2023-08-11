package cn.qbs.wa.union.admin.controller;


import cn.qbs.wa.union.admin.pojo.systemsubuser.*;
import cn.qbs.wa.union.admin.service.SystemSubUserService;
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
 * 子用户表(SystemSubUser)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:06
 */
//@RestController
//@RequestMapping("systemSubUser")
public class SystemSubUserController {

    /**
     * 服务对象
     */
    @Resource
    private SystemSubUserService systemSubUserService;


    /**
     * 新增子用户表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("systemSubUser:add")
    //@Log(title = "新增子用户表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated SystemSubUserAddRequest params) {
        return R.ok(this.systemSubUserService.add(params));
    }

    /**
     * 分页查询子用户表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("systemSubUser:page")
    //@Log(title = "分页查询子用户表", businessType = BusinessType.OTHER)
    public R<IPage<SystemSubUserPageResponse>> page(@RequestBody SystemSubUserPageRequest params) {
        return R.ok(this.systemSubUserService.page(params));
    }

    /**
     * 查询子用户表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("systemSubUser:details")
    //@Log(title = "子用户表详情", businessType = BusinessType.OTHER)
    public R<SystemSubUserDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.systemSubUserService.detail(request.getId()));
    }

    /**
     * 修改子用户表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("systemSubUser:update")
    //@Log(title = "更新子用户表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated SystemSubUserUpdateRequest params) {
        return R.ok(this.systemSubUserService.update(params));
    }

    /**
     * 删除子用户表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("systemSubUser:delete")
    //@Log(title = "删除子用户表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.systemSubUserService.deleteByIds(request.getIdList()));
    }

}

