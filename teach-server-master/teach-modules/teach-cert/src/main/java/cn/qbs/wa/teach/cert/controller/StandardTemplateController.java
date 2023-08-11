package cn.qbs.wa.teach.cert.controller;


import cn.qbs.wa.teach.cert.enums.CertSearchConfigEnum;
import cn.qbs.wa.teach.cert.enums.CertTemplateConfigEnum;
import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDetailDTO;
import cn.qbs.wa.teach.cert.pojo.standardtemplate.*;
import cn.qbs.wa.teach.cert.service.StandardTemplateService;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 证书模板(StandardTemplate)表控制层
 *
 * @author makejava
 * @since 2022-01-19 11:38:21
 */
@RestController
@RequestMapping("standardTemplate")
@Api(tags = "模板管理")
public class StandardTemplateController {

    /**
     * 服务对象
     */
    @Resource
    private StandardTemplateService standardTemplateService;


    /**
     * 新增证书模板
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("standardTemplate:add")
    //@Log(title = "新增证书模板", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated StandardTemplateAddRequest params) {
        return R.ok(this.standardTemplateService.add(params));
    }

    /**
     * 分页查询证书模板
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("standardTemplate:page")
    //@Log(title = "分页查询证书模板", businessType = BusinessType.OTHER)
    @ApiOperation("分页")
    public R<IPage<StandardTemplatePageResponse>> page(@RequestBody StandardTemplatePageRequest params) {
        return R.ok(this.standardTemplateService.page(params));
    }

    /**
     * 查询证书模板详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("standardTemplate:details")
    //@Log(title = "证书模板详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<StandardTemplateDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.standardTemplateService.detail(request.getId()));
    }

    @PostMapping("preview")
    @ApiOperation("预览")
    public R<StandardTemplateDetailResponse> preview(@RequestBody IdRequest request) {
        return R.ok(this.standardTemplateService.preview(request.getId()));
    }




    @PostMapping("detail/searchConfig")
    //@RequiresPermissions("standardTemplate:details")
    //@Log(title = "证书查询配置详情", businessType = BusinessType.OTHER)
    @ApiOperation("证书查询配置详情")
    public R<StandardTemplateDetailSearchConfigResponse > detailSearchConfig(@RequestBody IdRequest request) {
        return R.ok(this.standardTemplateService.detailSearchConfig(request.getId()));
    }

    @PostMapping("update/searchConfig")
    //@RequiresPermissions("standardTemplate:update")
    //@Log(title = "更新证书模板查询配置", businessType = BusinessType.UPDATE)
    @ApiOperation("更新证书模板查询配置")
    public R<Boolean> updateSearchConfig(@RequestBody @Validated StandardTemplateUpdateSearchConfigRequest params) {
        this.standardTemplateService.updateSearchConfig(params);
        return R.ok();
    }


    /**
     * 修改证书模板
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("standardTemplate:update")
    //@Log(title = "更新证书模板", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated StandardTemplateUpdateRequest params) {
        return R.ok(this.standardTemplateService.update(params));
    }

    /**
     * 删除证书模板
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("standardTemplate:delete")
    //@Log(title = "删除证书模板", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.standardTemplateService.deleteByIds(request.getIdList()));
    }

    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.standardTemplateService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }

    @PostMapping("templateConfig")
    @ApiOperation("获取证书信息可选配置")
    public R<List<CommonConfigDetailDTO>>  getTemplateConfig(){
        List<CommonConfigDetailDTO> commonConfigList = new ArrayList<>();
        CertTemplateConfigEnum[] values = CertTemplateConfigEnum.values();
        for (CertTemplateConfigEnum value : values) {
            CommonConfigDetailDTO commonConfigDetailDTO = new CommonConfigDetailDTO();
            BeanUtils.copyProperties(value, commonConfigDetailDTO);
            commonConfigList.add(commonConfigDetailDTO);
        }
        return R.ok(commonConfigList);
    }

    @PostMapping("searchConfig")
    @ApiOperation("获取查询可选配置")
    public R<List<CommonConfigDetailDTO>> getSearchConfig(){
        List<CommonConfigDetailDTO> commonConfigList = new ArrayList<>();
        CertSearchConfigEnum[] values = CertSearchConfigEnum.values();
        for (CertSearchConfigEnum value : values) {
            CommonConfigDetailDTO commonConfigDetailDTO = new CommonConfigDetailDTO();
            BeanUtils.copyProperties(value, commonConfigDetailDTO);
            commonConfigList.add(commonConfigDetailDTO);
        }
        return R.ok(commonConfigList);
    }


    @PostMapping("check/certNumRule")
    @ApiOperation("检查证书编号")
    public R<Boolean> getCheckCert(@RequestBody StandardTemplateCheckCertRequest request){
        return R.ok(this.standardTemplateService.getCheckCert(request));
    }

}

