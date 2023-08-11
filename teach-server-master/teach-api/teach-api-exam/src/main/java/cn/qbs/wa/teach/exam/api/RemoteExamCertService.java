package cn.qbs.wa.teach.exam.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.api.factory.RemoteExamCertFallbackFactory;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamRecordDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.PersonCertDetailResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: NQY
 * @Date: 2022/5/18 9:46
 * @Description:
 */
@FeignClient(contextId = "remoteExamCertService", name = "teach-exam-admin", path = "/exam/admin/tCert", fallbackFactory = RemoteExamCertFallbackFactory.class)
public interface RemoteExamCertService {
    @PostMapping("getCertInfo")
    //@RequiresPermissions("cert:details")
    //@Log(title = "个人查询考试证书信息", businessType = BusinessType.OTHER)
    @ApiOperation("个人查询考试证书信息")
    R<List<PersonCertDetailResponseDTO>> getCertInfo(@RequestBody ExamRecordDTO examRecord);
}
