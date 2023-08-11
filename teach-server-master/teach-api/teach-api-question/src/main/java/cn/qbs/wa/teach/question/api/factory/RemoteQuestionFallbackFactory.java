package cn.qbs.wa.teach.question.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.api.RemoteQuestionService;
import cn.qbs.wa.teach.question.api.pojo.DTO.CategoryDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.QuestionCategoryAddRequestDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.question.QuestionDetailsDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/26 16:23
 * @Version 1.0
 */
public class RemoteQuestionFallbackFactory implements FallbackFactory<RemoteQuestionService> {

    @Override
    public RemoteQuestionService create(Throwable cause) {

        return new RemoteQuestionService() {

            @Override
            public R<QuestionDetailsDTO> questionDetail(IdOrgRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<CategoryDTO> init(QuestionCategoryAddRequestDTO params) {
                return R.fail("服务暂无法访问");
            }
        };
    }


}
