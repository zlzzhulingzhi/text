package cn.qbs.wa.teach.exam.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.RemoteOrgDeskTaskExamService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.orgdesk.ExamInfoDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @Author WX
 * @Date 2022/03/16 16:44
 * @Version 1.0
 */
public class RemoteOrgDeskTaskExamFallbackFactory implements FallbackFactory<RemoteOrgDeskTaskExamService> {

    @Override
    public RemoteOrgDeskTaskExamService create(Throwable cause) {
        return new RemoteOrgDeskTaskExamService() {
            @Override
            public R<List<ExamInfoDTO>> getExamList(IdListAndUserIdRequest request) {
                return R.fail("服务暂无法访问");
            }
        };
    }

}
