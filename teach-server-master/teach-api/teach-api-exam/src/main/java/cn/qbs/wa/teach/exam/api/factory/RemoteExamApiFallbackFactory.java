package cn.qbs.wa.teach.exam.api.factory;

import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.RemoteExamApiService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamPageRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamineeQueryDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamineeeDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/26 16:23
 * @Version 1.0
 */
public class RemoteExamApiFallbackFactory implements FallbackFactory<RemoteExamApiService> {

    @Override
    public RemoteExamApiService create(Throwable cause) {
        return new RemoteExamApiService() {
            @Override
            public R<PageResultComDTO<ExamDTO>> page(ExamPageRequestDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<ExamineeeDTO>> examineeList(ExamineeQueryDTO examIdDTO) {
                return R.fail("服务暂无法访问");
            }
        };
    }

}
