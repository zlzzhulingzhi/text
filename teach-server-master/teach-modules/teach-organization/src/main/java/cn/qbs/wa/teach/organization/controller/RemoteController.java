package cn.qbs.wa.teach.organization.controller;

import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniOrgInfoResponseDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniOrgSearchDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionOrgService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "远程服务统一请求接口")
@RestController
@RequestMapping("/remote")
public class RemoteController {

    @Resource
    private RemoteUnionOrgService remoteUnionOrgService;

    @PostMapping("/org/page")
    R<PageResultComDTO<UniOrgInfoResponseDTO>> page(@RequestBody UniOrgSearchDTO params) {
        return remoteUnionOrgService.page(params);
    }

}
