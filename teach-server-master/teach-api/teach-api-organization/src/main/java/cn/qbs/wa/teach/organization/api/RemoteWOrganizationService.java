package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteWOrganizationFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WOrgUpdateDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.worganization.WOrgAddDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.worganization.WOrgPageResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.worganization.WOrganizationPageSearchDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteWOrganizationService", name = "teach-org", path = "org/WOrg", fallbackFactory = RemoteWOrganizationFallbackFactory.class)
public interface RemoteWOrganizationService {


    /**
     * 分页查询机构插件表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询机构插件表")
    R<PageResultComDTO<WOrgPageResultDTO>> page(@RequestBody WOrganizationPageSearchDTO params);

    /**
     * 新增机构插件表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增机构插件表")
    R add(@RequestBody @Validated WOrgAddDTO params);

    /**
     * 删除机构插件表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除机构插件表")
    R delete(@RequestBody IdListRequest request);


    @PostMapping("update")
    //@RequiresPermissions("wOrg:update")
    //@Log(title = "更新机构插件表", businessType = BusinessType.UPDATE)
    @ApiOperation("更新机构插件表")
     R<Boolean> update(@RequestBody @Validated WOrgUpdateDTO params) ;

}
