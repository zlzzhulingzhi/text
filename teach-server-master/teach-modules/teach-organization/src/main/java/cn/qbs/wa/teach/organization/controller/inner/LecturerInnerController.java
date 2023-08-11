package cn.qbs.wa.teach.organization.controller.inner;

import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.organization.pojo.lecturer.*;
import cn.qbs.wa.teach.organization.service.inner.LecturerInnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * 讲师表(Lecturer)表控制层
 *
 * @author makejava
 * @since 2021-11-17 11:25:32
 */
@RestController
@RequestMapping("inner/lecturer")
@Api(tags = "讲师管理")
public class LecturerInnerController {

    /**
     * 服务对象
     */
    @Resource
    private LecturerInnerService lecturerInnerService;


    /**
     * 查询讲师表详情
     *
     * @param id 主键
     * @return
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("detail")
    @ApiOperation("详情")
    public R<LecturerDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.lecturerInnerService.detail(request.getId()));
    }

}

