package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.pojo.worg.*;
import cn.qbs.wa.teach.organization.service.WOrgService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * 机构插件表(WOrg)表控制层
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
@RestController
@RequestMapping("WOrg")
public class WOrgController {

    /**
     * 服务对象
     */
    @Resource
    private WOrgService wOrgService;


    /**
     * 新增机构插件表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("wOrg:add")
    //@Log(title = "新增机构插件表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated WOrgAddRequest params) {
        return R.ok(this.wOrgService.add(params));
    }

    /**
     * 分页查询机构插件表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("wOrg:page")
    //@Log(title = "分页查询机构插件表", businessType = BusinessType.OTHER)
    public R<IPage<WOrgPageResponse>> page(@RequestBody WOrgPageRequest params) {
        return R.ok(this.wOrgService.page(params));
    }

    /**
     * 查询机构插件表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("wOrg:details")
    //@Log(title = "机构插件表详情", businessType = BusinessType.OTHER)
    public R<WOrgDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.wOrgService.detail(request.getId()));
    }

    /**
     * 修改机构插件表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("wOrg:update")
    //@Log(title = "更新机构插件表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated WOrgUpdateRequest params) {
        return R.ok(this.wOrgService.update(params));
    }

    /**
     * 删除机构插件表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("wOrg:delete")
    //@Log(title = "删除机构插件表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.wOrgService.deleteByIds(request.getIdList()));
    }

}

