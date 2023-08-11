package cn.qbs.wa.teach.cert.api.factory;

import cn.qbs.wa.teach.cert.api.RemoteCertCenterService;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.MyCertSearchRequestDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.cert.MyCertSearchResponseDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteCertCenterServiceFallbackFactory implements FallbackFactory<RemoteCertCenterService> {
    @Override
    public RemoteCertCenterService create(Throwable cause) {
        return new RemoteCertCenterService() {
            @Override
            public R<MyCertSearchResponseDTO> myCertSearch(MyCertSearchRequestDTO params) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
