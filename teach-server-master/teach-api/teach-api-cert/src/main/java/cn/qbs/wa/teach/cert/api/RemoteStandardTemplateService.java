package cn.qbs.wa.teach.cert.api;

import cn.qbs.wa.teach.cert.api.factory.RemoteStandardTemplateFallbackFactory;
import cn.qbs.wa.teach.cert.api.pojo.DTO.template.StandardTemplateDetailResponseDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.template.StandardTemplatePageRequestDto;
import cn.qbs.wa.teach.cert.api.pojo.DTO.template.StandardTemplatePageResponseDTO;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remoteStandardTemplateService", name = "teach-cert", path = "cert/standardTemplate", fallbackFactory = RemoteStandardTemplateFallbackFactory.class)
public interface RemoteStandardTemplateService {
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
    R<PageResultComDTO<StandardTemplatePageResponseDTO>> page(@RequestBody StandardTemplatePageRequestDto params);

    @PostMapping("preview")
    @ApiOperation("预览")
    R<StandardTemplateDetailResponseDTO> preview(@RequestBody IdRequest request);
}
