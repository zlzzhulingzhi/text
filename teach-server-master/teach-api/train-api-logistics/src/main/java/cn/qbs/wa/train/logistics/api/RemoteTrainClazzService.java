package cn.qbs.wa.train.logistics.api;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.factory.RemoteTrainClazzFallbackFactory;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzPageRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzPageResponseDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.IntegrateClazzResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteTrainClazzService", name = "train-logistics", path = "/logistics/clazz", fallbackFactory = RemoteTrainClazzFallbackFactory.class)
public interface RemoteTrainClazzService {

    @PostMapping("getByOrgId")
    R<List<ClazzPageResponseDTO>> getByOrgId(@RequestBody ClazzPageRequestDTO params);

    @PostMapping("getCountByCourseId")
    R<Long> getCountByCourseId(@RequestBody ClazzPageRequestDTO params);

    @PostMapping("/inner/listClazzByMemberId")
    R<List<IntegrateClazzResponse>> listClazzByMemberId(@RequestBody IdRequest request);

    @PostMapping("/inner/listClazzByLecturerId")
    R<List<IntegrateClazzResponse>> listClazzByLecturerId(@RequestBody IdRequest request);

    @PostMapping("/inner/queryClazzLastByLecturerIds")
    R<Map<Long, String>> queryClazzLastByLecturerIds(@RequestBody IdListRequest request);
}
