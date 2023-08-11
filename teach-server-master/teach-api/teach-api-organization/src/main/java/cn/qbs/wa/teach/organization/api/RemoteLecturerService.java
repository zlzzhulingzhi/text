package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteLecturerFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteLecturerService", name="teach-org", path = "org", fallbackFactory = RemoteLecturerFallbackFactory.class)
public interface RemoteLecturerService {


    @PostMapping("/lecturer/list")
    @ApiOperation("讲师列表")
    R<List<LecturerDTO>> listLecturers(LecturerSearchDTO search);


    /**
     * 分页查询讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("/lecturer/page")
    @ApiOperation("分页")
    R<PageResultComDTO<LecturerPageResultDTO>> page(@RequestBody LecturerPageSearchDTO params) ;


    /**
     * 分页查询讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("/lecturer/admin/page")
    @ApiOperation("分页(平台管理讲师选择器)")
    R<PageResultComDTO<LecturerPageResultDTO>> pageAdmin(@RequestBody LecturerPageSearchDTO params) ;

    @PostMapping("inner/lecturer/detail")
    @ApiOperation("详情")
    R<LecturerDetailResponseDTO> detail(@RequestBody IdRequest request);


}
