package cn.qbs.wa.teach.exam.admin.controller;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.exam.admin.pojo.tcertrule.*;
import cn.qbs.wa.teach.exam.admin.service.TCertRuleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



/**
 * 证书规则表(TCertRule)表控制层
 *
 * @author makejava
 * @since 2022-05-16 13:49:21
 */
@RestController
@RequestMapping("tCertRule")
public class TCertRuleController {

    /**
     * 服务对象
     */
    @Resource
    private TCertRuleService tCertRuleService;


    /**
     * 新增证书规则表
     *
     * @param params
     * @return 
     */
    @PostMapping("add")
    //@RequiresPermissions("tCertRule:add")
    //@Log(title = "新增证书规则表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated TCertRuleAddRequest params) {
        return R.ok(this.tCertRuleService.add(params));
    }
    
    /**
     * 分页查询证书规则表
     *
     * @param params
     * @return 
     */
    @PostMapping("page")
    //@RequiresPermissions("tCertRule:page")
    //@Log(title = "分页查询证书规则表", businessType = BusinessType.OTHER)
    public R<IPage<TCertRulePageResponse>> page(@RequestBody TCertRulePageRequest params) {
        return R.ok(this.tCertRuleService.page(params));
    }

    /**
     * 查询证书规则表详情
     *
     * @param id 主键
     * @return 
     */
    @PostMapping("detail")
    //@RequiresPermissions("tCertRule:details")
    //@Log(title = "证书规则表详情", businessType = BusinessType.OTHER)
    public R<TCertRuleDetailResponse> detail(Long id) {
        return R.ok(this.tCertRuleService.detail(id));
    }

    /**
     * 修改证书规则表
     *
     * @param params 
     * @return 
     */
    @PostMapping("update")
    //@RequiresPermissions("tCertRule:update")
    //@Log(title = "更新证书规则表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated TCertRuleUpdateRequest params) {
        return R.ok(this.tCertRuleService.update(params));
    }

    /**
     * 删除证书规则表
     *
     * @param idList 主键集合
     * @return 
     */
    @PostMapping("delete")
    //@RequiresPermissions("tCertRule:delete")
    //@Log(title = "删除证书规则表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestParam("ids") List<Long> idList) {
        return R.ok(this.tCertRuleService.deleteByIds(idList));
    }
    
}

