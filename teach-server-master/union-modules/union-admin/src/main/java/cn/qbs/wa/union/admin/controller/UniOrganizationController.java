package cn.qbs.wa.union.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.basic.api.RemoteDictService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictResultDTO;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.union.admin.entity.UniOrganization;
import cn.qbs.wa.union.admin.pojo.dict.DictRequest;
import cn.qbs.wa.union.admin.pojo.dict.DictResponse;
import cn.qbs.wa.union.admin.pojo.uniorganization.*;
import cn.qbs.wa.union.admin.service.UniOrganizationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一机构(UniOrganization)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
@RestController
@RequestMapping("/uniOrg")
public class UniOrganizationController {

    /**
     * 服务对象
     */
    @Resource
    private UniOrganizationService uniOrganizationService;

    @Resource
    private RemoteDictService remoteDictService;

    /**
     * 新增统一机构
     *
     * @param params
     * @return
     */
    @PostMapping("/add")
    @RequiresPermissions("System:Admin:Org")
    public R<Boolean> add(@RequestBody @Validated UniOrganizationAddRequest params) {
        return R.ok(this.uniOrganizationService.add(params));
    }

    /**
     * 分页查询统一机构
     *
     * @param params
     * @return
     */
    @PostMapping("/page")
    @RequiresPermissions("System:Admin:Org")
    public R<IPage<UniOrganizationPageResponse>> page(@RequestBody UniOrganizationPageRequest params) {
        return R.ok(this.uniOrganizationService.page(params));
    }

    /**
     * 查询统一机构详情
     *
     * @param request 主键
     * @return
     */
    @PostMapping("/detail")
    @RequiresPermissions("System:Admin:Org")
    public R<UniOrganizationDetailResponse> detail(@RequestBody @Validated IdRequest request) {
        return R.ok(this.uniOrganizationService.detail(request.getId()));
    }

    @PostMapping("/info")
    public R<UniOrganizationDetailResponse> info() {
        return R.ok(this.uniOrganizationService.detail(SecurityContextHolder.getOrgId()));
    }

    /**
     * 修改统一机构
     *
     * @param params
     * @return
     */
    @PostMapping("/update")
    @RequiresPermissions("System:Admin:Org")
    public R<Boolean> update(@RequestBody @Validated UniOrganizationUpdateRequest params) {
        return R.ok(this.uniOrganizationService.update(params));
    }

    @PostMapping("/enabled")
    @RequiresPermissions("System:Admin:Org")
    @ApiOperation("启用/禁用")
    public R<Boolean> batchEnabled(@RequestBody BatchFlagRequest request) {
        if (CollUtil.isEmpty(request.getIdList())) {
            return R.fail("请选择机构");
        }
        this.uniOrganizationService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok(Boolean.TRUE);
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/page")
    public R<IPage<UniOrganizationInfoResponse>> pageFromInner(@RequestBody UniOrganizationPageRequest request) {
        if (request.getEnabled() == null) {
            request.setEnabled(Constants.DB_TRUE);
        }
        IPage<UniOrganizationPageResponse> page = this.uniOrganizationService.page(request);
        if (page.getRecords().isEmpty()) {
            return R.ok(request.createMpPage());
        }
        return R.ok(page.convert(r -> BeanUtil.copyProperties(r, UniOrganizationInfoResponse.class)));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/list")
    public R<List<UniOrganizationInfoResponse>> ListFromInner(@RequestBody UniOrganizationListRequest request) {
        if (request.getEnabled() == null) {
            request.setEnabled(Constants.DB_TRUE);
        }
        List<UniOrganization> list = this.uniOrganizationService.lambdaQuery()
                .eq(UniOrganization::getEnabled, request.getEnabled())
                .in(CollUtil.isNotEmpty(request.getIds()), UniOrganization::getId, request.getIds())
                .eq(request.getPlan() != null, UniOrganization::getPlan, request.getPlan())
                .like(StrUtil.isNotBlank(request.getName()), UniOrganization::getName, request.getName())
                .list();
        return R.ok(BeanUtil.copyToList(list, UniOrganizationInfoResponse.class));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/info")
    public R<UniOrganizationInfoResponse> infoFromInner(@RequestBody IdRequest request) {
        UniOrganizationDetailResponse detail = this.uniOrganizationService.detail(request.getId());
        return R.ok(BeanUtil.copyProperties(detail, UniOrganizationInfoResponse.class));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/changeToPlan")
    public R<Boolean> changeToPlan(@RequestBody IdRequest request) {
        UniOrganization updater = new UniOrganization();
        updater.setId(request.getId());
        updater.setPlan(Constants.DB_TRUE);
        return R.ok(this.uniOrganizationService.updateById(updater));
    }

    @PostMapping("/dict/list")
    public R<List<DictResponse>> listDict(@RequestBody @Validated DictRequest request) {
        R<List<DictResultDTO>> r = remoteDictService.dictList(new DictDTO(request.getCode()));
        List<DictResultDTO> list = r.getData();
        if (r.isOk() && CollUtil.isNotEmpty(list)) {
            List<DictResponse> collect = BeanUtil.copyToList(list, DictResponse.class);
            if (request.getEnabled() != null) {
                collect = collect.stream().filter(e -> e.getEnabled().equals(request.getEnabled())).collect(Collectors.toList());
            }
            return R.ok(collect);
        }
        return R.ok();
    }
}

