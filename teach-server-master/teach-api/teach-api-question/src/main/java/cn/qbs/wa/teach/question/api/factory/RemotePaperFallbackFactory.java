package cn.qbs.wa.teach.question.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.api.RemotePaperService;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.*;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/26 16:23
 * @Version 1.0
 */
public class RemotePaperFallbackFactory implements FallbackFactory<RemotePaperService> {

    @Override
    public RemotePaperService create(Throwable cause) {
        return new RemotePaperService() {
            @Override
            public R<List<CategoryTreeNodeDTO>> category() {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<SearchPageDTO<PaperDTO>> search(PaperSearchDTO request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PaperDetailDTO> detail(IdOrgRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Boolean> editable(UpdatePaperEditableRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<BasicPaper>> basicList(IdListRequest request) {
                return R.fail("服务暂无法访问");
            }
        };
    }

}
