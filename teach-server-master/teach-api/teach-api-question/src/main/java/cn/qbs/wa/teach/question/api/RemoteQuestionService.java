package cn.qbs.wa.teach.question.api;

import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.api.factory.RemoteQuestionFallbackFactory;
import cn.qbs.wa.teach.question.api.pojo.DTO.CategoryDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.QuestionCategoryAddRequestDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.question.QuestionDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author zcm
 * @Date 2021/11/26 16:21
 * @Version 1.0
 */
@FeignClient(contextId = "remoteQuestionService", name = "teach-question", path = "question/inner", fallbackFactory = RemoteQuestionFallbackFactory.class)
public interface RemoteQuestionService {

    /**
     * 获取试题详情
     * @param request ID List
     * @return 试题详情
     */
    @PostMapping("/question/detail")
    R<QuestionDetailsDTO> questionDetail(@RequestBody IdOrgRequest request);

    @PostMapping("/init")
    R<CategoryDTO> init(@RequestBody QuestionCategoryAddRequestDTO params);

}
