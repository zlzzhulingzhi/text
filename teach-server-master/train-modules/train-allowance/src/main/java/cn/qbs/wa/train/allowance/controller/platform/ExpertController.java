package cn.qbs.wa.train.allowance.controller.platform;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.allowance.entity.Expert;
import cn.qbs.wa.train.allowance.pojo.expert.*;
import cn.qbs.wa.train.allowance.service.ExpertService;
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
 * 专家信息(Expert)表控制层
 *
 * @author makejava
 * @since 2022-10-31 18:47:29
 */
@RestController
@RequestMapping("expert")
@Api(tags = "专家信息维护")
public class ExpertController {

    /**
     * 服务对象
     */
    @Resource
    private ExpertService expertService;


    /**
     * 新增专家信息
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增专家信息")
    @RequiresPermissions("Management:Expert")
    //@Log(title = "新增专家信息", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated ExpertAddRequest params) {
        return R.ok(this.expertService.add(params));
    }

    /**
     * 分页查询专家信息
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询专家信息")
    @RequiresPermissions("Management:Expert")
    //@Log(title = "分页查询专家信息", businessType = BusinessType.OTHER)
    public R<IPage<ExpertPageResponse>> page(@RequestBody ExpertPageRequest params) {
        return R.ok(this.expertService.page(params));
    }

    /**
     * 查询专家信息详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("专家信息详情")
    @RequiresPermissions("Management:Expert")
    //@Log(title = "专家信息详情", businessType = BusinessType.OTHER)
    public R<ExpertDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.expertService.detail(request.getId()));
    }

    /**
     * 修改专家信息
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新专家信息")
    @RequiresPermissions("Management:Expert")
    //@Log(title = "更新专家信息", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ExpertUpdateRequest params) {
        return R.ok(this.expertService.update(params));
    }

    /**
     * 删除专家信息
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除专家信息")
    @RequiresPermissions("Management:Expert")
    //@Log(title = "删除专家信息", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.expertService.deleteByIds(request.getIdList()));
    }

    @PostMapping("list")
    @ApiOperation("专家列表")
    public R<List<Expert>> list() {
        return R.ok(this.expertService.lambdaQuery().list());
    }

}

