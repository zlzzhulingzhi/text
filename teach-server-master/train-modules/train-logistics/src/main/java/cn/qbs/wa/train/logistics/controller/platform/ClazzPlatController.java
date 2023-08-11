package cn.qbs.wa.train.logistics.controller.platform;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomPageResponse;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomSummaryRequest;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomSummaryResponse;
import cn.qbs.wa.train.logistics.pojo.clazz.*;
import cn.qbs.wa.train.logistics.service.platform.ClazzPlatService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/**
 * 班级表(Clazz)表控制层
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@RestController
@RequestMapping("plat/clazz")
@Api(tags = "平台班级管理")
public class ClazzPlatController {

    /**
     * 服务对象
     */
    @Resource
    private ClazzPlatService clazzPlatService;


    /**
     * 分页查询班级表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("平台分页查询班级")
    //@Log(title = "分页查询班级表", businessType = BusinessType.OTHER)
    public R<IPage<ClazzPageResponse>> page(@RequestBody ClazzPageRequest params) {
        return R.ok(this.clazzPlatService.page(params));
    }

    @PostMapping("pageV2")
    @ApiOperation("平台分页查询班级")
    //@Log(title = "分页查询班级表", businessType = BusinessType.OTHER)
    public R<IPage<ClazzPageResponse>> pageV2(@RequestBody ClazzPageRequest params) {
        return R.ok(this.clazzPlatService.pageV2(params));
    }

    /**
     * 查询班级表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("平台班级详情")
    //@Log(title = "班级表详情", businessType = BusinessType.OTHER)
    public R<ClazzDetailResponse> detail(@RequestBody ClazzDetailRequest request) {
        return R.ok(this.clazzPlatService.detail(request));
    }

    @PostMapping("getClazzSummary")
    @ApiOperation("机构培训统计")
    public R<IPage<ClazzSummaryResponse>> getClazzSummary(@RequestBody ClazzSummaryRequest params) {
        return R.ok(this.clazzPlatService.getClazzSummary(params));
    }

    @PostMapping("getClazzTotalSummary")
    @ApiOperation("机构培训总数统计")
    public R<ClazzSummaryResponse> getClazzTotalSummary(@RequestBody ClazzSummaryRequest params) {
        return R.ok(this.clazzPlatService.getClazzTotalSummary(params));
    }

}

