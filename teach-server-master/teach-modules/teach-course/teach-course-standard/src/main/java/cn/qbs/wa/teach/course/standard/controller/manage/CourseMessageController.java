package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.course.standard.pojo.coursemessage.*;
import cn.qbs.wa.teach.course.standard.service.CourseMessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;



/**
 * 【课程留言】(CourseMessage)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Api(tags = "课程留言")
@RestController
@RequestMapping("/message")
public class CourseMessageController {

    /**
     * 服务对象
     */
    @Resource
    private CourseMessageService courseMessageService;


    /**
     * 新增【课程留言】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程留言-新增")
    @PostMapping("/add")
    //@RequiresPermissions("courseMessage:add")
    //@Log(title = "新增【课程留言】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseMessageAddRequest params) {
        return R.ok(this.courseMessageService.add(params));
    }

    /**
     * 分页查询【课程留言】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程留言-分页")
    @PostMapping("/page")
    //@RequiresPermissions("courseMessage:page")
    //@Log(title = "分页查询【课程留言】", businessType = BusinessType.OTHER)
    public R<IPage<CourseMessagePageResponse>> page(@RequestBody CourseMessagePageRequest params) {
        return R.ok(this.courseMessageService.page(params));
    }

    /**
     * 查询【课程留言】详情
     *
     * @param params 主键
     * @return
     */
    @ApiOperation(value = "课程留言-详情")
    @PostMapping("/detail")
    //@RequiresPermissions("courseMessage:details")
    //@Log(title = "【课程留言】详情", businessType = BusinessType.OTHER)
    public R<CourseMessageDetailResponse> detail(@RequestBody @Validated IdRequest params) {
        return R.ok(this.courseMessageService.detail(params.getId()));
    }

    /**
     * 修改【课程留言】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程留言-修改")
    @PostMapping("/update")
    //@RequiresPermissions("courseMessage:update")
    //@Log(title = "更新【课程留言】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseMessageUpdateRequest params) {
        return R.ok(this.courseMessageService.update(params));
    }

    /**
     * 删除【课程留言】
     *
     * @param params 主键集合
     * @return
     */
    @ApiOperation(value = "课程留言-删除")
    @PostMapping("/delete")
    //@RequiresPermissions("courseMessage:delete")
    //@Log(title = "删除【课程留言】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest params) {
        return R.ok(this.courseMessageService.deleteByIds(params.getIdList()));
    }

}

