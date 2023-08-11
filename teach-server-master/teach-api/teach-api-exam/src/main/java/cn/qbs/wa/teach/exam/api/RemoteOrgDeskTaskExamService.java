package cn.qbs.wa.teach.exam.api;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.factory.RemoteOrgDeskTaskExamFallbackFactory;
import cn.qbs.wa.teach.exam.api.pojo.DTO.orgdesk.ExamInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author WX
 * @Date 2022/03/16 16:36
 * @Version 1.0
 */
@FeignClient(contextId = "remoteOrgDeskTaskExamService", name = "teach-exam-admin", path = "exam/admin/orgDeskTask", fallbackFactory = RemoteOrgDeskTaskExamFallbackFactory.class)
public interface RemoteOrgDeskTaskExamService {

    /**
     * 查询机构前台h5任务中包含的考试
     * @param request
     * @return
     */
    @PostMapping("/getExamList")
    R<List<ExamInfoDTO>> getExamList(@RequestBody IdListAndUserIdRequest request);

}
