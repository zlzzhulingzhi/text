package cn.qbs.wa.teach.cert.api.factory;


import cn.qbs.wa.teach.cert.api.RemoteStandardTemplateService;
import cn.qbs.wa.teach.cert.api.pojo.DTO.template.StandardTemplateDetailResponseDTO;
import cn.qbs.wa.teach.cert.api.pojo.DTO.template.StandardTemplatePageRequestDto;
import cn.qbs.wa.teach.cert.api.pojo.DTO.template.StandardTemplatePageResponseDTO;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteStandardTemplateFallbackFactory implements FallbackFactory<RemoteStandardTemplateService> {

    @Override
    public RemoteStandardTemplateService create(Throwable cause) {
        return new RemoteStandardTemplateService() {
            @Override
            public R<PageResultComDTO<StandardTemplatePageResponseDTO>> page(StandardTemplatePageRequestDto params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<StandardTemplateDetailResponseDTO> preview(IdRequest request) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
