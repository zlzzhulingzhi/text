package cn.qbs.wa.teach.exam.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.RemoteExamService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDeptBatchAddRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDeptDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamGroupBatchAddRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamGroupDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/26 16:23
 * @Version 1.0
 */
public class RemoteExamFallbackFactory implements FallbackFactory<RemoteExamService> {

    @Override
    public RemoteExamService create(Throwable cause) {
        return new RemoteExamService() {

            @Override
            public R<List<ExamDeptDTO>> examByDeptId(IdRequest request) {
                return null;
            }

            @Override
            public R<List<ExamGroupDTO>> examByGroupIdList(IdListRequest request) {
                return null;
            }

            @Override
            public R<Boolean> addExamUserByDept(ExamDeptBatchAddRequestDTO request) {
                return null;
            }

            @Override
            public R<Boolean> addExamUserByGroup(ExamGroupBatchAddRequestDTO request) {
                return null;
            }
        };
    }

}
