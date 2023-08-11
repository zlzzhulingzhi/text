package cn.qbs.wa.teach.exam.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.factory.RemoteExamFallbackFactory;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDeptBatchAddRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDeptDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamGroupBatchAddRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamGroupDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/26 16:21
 * @Version 1.0
 */
@FeignClient(contextId = "remoteExamService", name = "teach-exam-admin", path = "/exam/admin", fallbackFactory = RemoteExamFallbackFactory.class)
public interface RemoteExamService {


    @PostMapping("examDept/examByDeptId")
    R<List<ExamDeptDTO>> examByDeptId(@RequestBody IdRequest request);

    @PostMapping("examDept/addExamUserByDept")
    R<Boolean> addExamUserByDept(@RequestBody ExamDeptBatchAddRequestDTO request);

    @PostMapping("examGroup/addExamUserByGroup")
    R<Boolean> addExamUserByGroup(@RequestBody ExamGroupBatchAddRequestDTO request);

    @PostMapping("examGroup/examByGroupIdList")
    R<List<ExamGroupDTO>> examByGroupIdList(@RequestBody IdListRequest request);

}
