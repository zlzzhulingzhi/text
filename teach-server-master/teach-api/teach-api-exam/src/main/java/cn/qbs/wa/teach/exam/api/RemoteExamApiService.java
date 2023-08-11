package cn.qbs.wa.teach.exam.api;

import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.factory.RemoteExamApiFallbackFactory;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamPageRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamineeQueryDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamineeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/26 16:21
 * @Version 1.0
 */
@FeignClient(contextId = "remoteExamApiService", name = "teach-exam-admin", path = "/exam/admin/api", fallbackFactory = RemoteExamApiFallbackFactory.class)
public interface RemoteExamApiService {

    /**
     * 分页查询考试
     *
     * @param params
     * @return
     */
    @PostMapping("exam/page")
    R<PageResultComDTO<ExamDTO>> page(@RequestBody ExamPageRequestDTO params);

    /**
     * 查询考试考生列表
     *
     * @param examIdDTO
     * @return
     */
    @PostMapping("exam/examineeList")
    R<List<ExamineeeDTO>> examineeList(@RequestBody ExamineeQueryDTO examIdDTO);

}
