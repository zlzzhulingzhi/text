package cn.qbs.wa.teach.exam.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.RemoteExamCertService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamRecordDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.PersonCertDetailResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @Author: NQY
 * @Date: 2022/5/18 9:47
 * @Description:
 */
public class RemoteExamCertFallbackFactory implements FallbackFactory<RemoteExamCertService> {

    @Override
    public RemoteExamCertService create(Throwable cause) {
        return new RemoteExamCertService() {
            @Override
            public R<List<PersonCertDetailResponseDTO>> getCertInfo(ExamRecordDTO examRecord) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
