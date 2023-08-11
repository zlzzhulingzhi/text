package cn.qbs.wa.teach.cert.api;

import cn.qbs.wa.teach.cert.api.factory.RemoteCertFallbackFactory;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.*;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord.*;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "remoteCertService", name = "teach-cert", path = "cert/cert", fallbackFactory = RemoteCertFallbackFactory.class)
public interface RemoteCertService {
    /**
     * 新增证书
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("cert:add")
    //@Log(title = "新增证书", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    R<CertDetailResponseDTO> add(@RequestBody @Validated CertAddRequestDTO params);

    /**
     * 分页查询证书
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("cert:page")
    //@Log(title = "分页查询证书", businessType = BusinessType.OTHER)
    @ApiOperation("分页")
    R<PageResultComDTO<CertPageResponseDTO>> page(@RequestBody CertPageRequestDTO params);

    /**
     * 查询证书详情
     *
     * @param
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("cert:details")
    //@Log(title = "证书详情", businessType = BusinessType.OTHER)
    @ApiOperation("证书详情")
    R<CertDetailResponseDTO> detail(@RequestBody IdRequest request);

    /**
     * 修改证书
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("cert:update")
    //@Log(title = "更新证书", businessType = BusinessType.UPDATE)
    R<Boolean> update(@RequestBody @Validated CertUpdateRequestDTO params);

    @PostMapping("award/batch-enabled")
    @ApiOperation("证书记录 禁用/启用")
    R batchEnable(@RequestBody @Validated BatchFlagRequest batchFlagRequest);

    @PostMapping("repetition")
    //@RequiresPermissions("cert:details")
    //@Log(title = "判断证书是否添加重复", businessType = BusinessType.OTHER)
    @ApiOperation("判断证书是否添加重复")
    R<List<CertDetailResponseDTO>> repetition(@RequestBody CertListRequestDTO certDetailRequest);

    @PostMapping("award/page")
    //@RequiresPermissions("cert:details")
    //@Log(title = "证书详情", businessType = BusinessType.OTHER)
    @ApiOperation("发放证书用户分页")
    R<PageResultComDTO<AwardRecordPageResponseDTO>> awardRecordPage(@RequestBody AwardRecordPageRequestDTO recordPageRequest);

    @PostMapping("acquire")
    //@RequiresPermissions("cert:details")
    //@Log(title = "查询个人已获取的证书", businessType = BusinessType.OTHER)
    @ApiOperation("查询个人已获取的证书")
    R<List<PersonCertDetailResponseDTO>> acquire(@RequestBody PersonCertDetailRequestDTO personCertDetailRequest);

    @PostMapping("award")
    @ApiOperation("颁发证书")
    R award(@RequestBody CertAwardRequestDTO certAwardRequest);

    @PostMapping("grant")
    @ApiOperation("查询证书是否已发布")
    R<List<AwardRecordDetailResponseDTO>> grant(@RequestBody IdRequest idRequest);

    @PostMapping("batchSelect")
    //@RequiresPermissions("cert:details")
    //@Log(title = "批量查询证书", businessType = BusinessType.OTHER)
    @ApiOperation("批量查询证书,主要供外部使用")
    R<List<PersonCertDetailResponseDTO>> batchSelect(@RequestBody BatchGetCertRequestDTO batchGetCertRequestDTO);
}
