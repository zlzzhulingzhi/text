package cn.qbs.wa.teach.cert.api;

import cn.qbs.wa.teach.cert.api.factory.RemoteCertCenterServiceFallbackFactory;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.MyCertSearchRequestDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.MyCertSearchResponseDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(contextId = "remoteCertCenterService", name = "teach-cert", path = "cert/center", fallbackFactory = RemoteCertCenterServiceFallbackFactory.class)
public interface RemoteCertCenterService {
    @PostMapping("myCertSearch")
    @ApiOperation("证书查询")
    R<MyCertSearchResponseDTO> myCertSearch(@RequestBody @Valid MyCertSearchRequestDTO params);
}
