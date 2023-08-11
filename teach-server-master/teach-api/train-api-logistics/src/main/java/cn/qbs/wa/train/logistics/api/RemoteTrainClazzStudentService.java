package cn.qbs.wa.train.logistics.api;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.factory.RemoteTrainClazzStudentFallbackFactory;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzStudentPageRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzStudentPageResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteTrainClazzStudentService", name = "train-logistics", path = "/logistics/clazzStudent", fallbackFactory = RemoteTrainClazzStudentFallbackFactory.class)
public interface RemoteTrainClazzStudentService {

    @PostMapping("getByOrgId")
    R<List<ClazzStudentPageResponseDTO>>getByOrgId(@RequestBody ClazzStudentPageRequestDTO params);

    @PostMapping("/inner/queryClazzLast")
    R<Map<Long, String>> queryClazzLast(@RequestBody IdListRequest request);
}
