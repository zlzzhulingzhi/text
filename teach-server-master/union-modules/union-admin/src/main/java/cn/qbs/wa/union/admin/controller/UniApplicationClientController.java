package cn.qbs.wa.union.admin.controller;


import cn.qbs.wa.union.admin.pojo.uniapplicationclient.*;
import cn.qbs.wa.union.admin.service.UniApplicationClientService;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
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
 * 统一应用客户端(UniApplicationClient)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@RestController
@RequestMapping("uniApplicationClient")
@Api(tags = "应用客户端")
public class UniApplicationClientController {

    /**
     * 服务对象
     */
    @Resource
    private UniApplicationClientService uniApplicationClientService;


    /**
     * 新增统一应用客户端
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("uniApplicationClient:add")
    //@Log(title = "新增统一应用客户端", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated UniApplicationClientAddRequest params) {
        return R.ok(this.uniApplicationClientService.add(params));
    }

    /**
     * 分页查询统一应用客户端
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("uniApplicationClient:page")
    //@Log(title = "分页查询统一应用客户端", businessType = BusinessType.OTHER)
    public R<IPage<UniApplicationClientPageResponse>> page(@RequestBody UniApplicationClientPageRequest params) {
        return R.ok(this.uniApplicationClientService.page(params));
    }

    /**
     * 查询统一应用客户端详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("uniApplicationClient:details")
    //@Log(title = "统一应用客户端详情", businessType = BusinessType.OTHER)
    public R<UniApplicationClientDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.uniApplicationClientService.detail(request.getId()));
    }

    /**
     * 修改统一应用客户端
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("uniApplicationClient:update")
    //@Log(title = "更新统一应用客户端", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated UniApplicationClientUpdateRequest params) {
        return R.ok(this.uniApplicationClientService.update(params));
    }

    /**
     * 删除统一应用客户端
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("uniApplicationClient:delete")
    //@Log(title = "删除统一应用客户端", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.uniApplicationClientService.deleteByIds(request.getIdList()));
    }

    @PostMapping("list")
    //@RequiresPermissions("systemMenu:details")
    //@Log(title = "【系统菜单】详情", businessType = BusinessType.OTHER)
    @ApiOperation("列表")
    public R<List<UniApplicationClientDetailResponse>> clientList() {
        return R.ok(this.uniApplicationClientService.clientList());
    }

}

