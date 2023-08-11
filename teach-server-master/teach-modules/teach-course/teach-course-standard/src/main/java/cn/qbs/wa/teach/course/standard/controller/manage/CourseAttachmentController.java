package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.teach.course.common.entity.CourseComponentAttachment;
import cn.qbs.wa.teach.course.standard.pojo.courseattachment.*;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.ComponentChangeNameRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment.CourseComponentUpdateResourceFileNameRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.ResourceFileDurationRecordDTO;
import cn.qbs.wa.teach.course.standard.service.CourseAttachmentService;
import cn.qbs.wa.teach.course.standard.service.CourseComponentAttachmentService;
import cn.qbs.wa.teach.course.standard.service.CoursePersonalService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 【课程讲义】(CourseAttachment)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 17:01:10
 */
@Slf4j
@Api(tags = "课程讲义")
@RestController
@RequestMapping("/attach")
public class CourseAttachmentController {

    /**
     * 服务对象
     */
    @Resource
    private CourseAttachmentService courseAttachmentService;


    @Resource
    private CourseComponentAttachmentService courseComponentAttachmentService;

    //@Resource
    //private CoursePersonalService coursePersonalService;

    /**
     * 分页查询【课程讲义】
     */
    @PostMapping("/page")
    @ApiOperation("课程讲义分页列表")
    //@RequiresPermissions("courseAttachment:page")
    //@Log(title = "分页查询【课程讲义】", businessType = BusinessType.OTHER)
    public R<IPage<CourseAttachmentPageResponse>> page(@RequestBody @Validated CourseAttachmentPageRequest params) {
        return R.ok(this.courseAttachmentService.page(params));
    }

    /**
     * 【课程讲义】列表
     */
    @PostMapping("/list")
    @ApiOperation("课程讲义列表")
    //@RequiresPermissions("courseAttachment:page")
    //@Log(title = "分页查询【课程讲义】", businessType = BusinessType.OTHER)
    public R<List<CourseAttachmentPageResponse>> list(@RequestBody @Validated CourseAttachmentListRequest params) {
        return R.ok(this.courseAttachmentService.list(params));
    }

    /**
     * 新增【课程讲义】
     */
    @ApiOperation(value = "新增【课程讲义】")
    @PostMapping("/add")
    //@RequiresPermissions("courseAttachment:add")
    @Log(title = "新增【课程讲义】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseAttachmentAddRequest params) {
        return R.ok(this.courseAttachmentService.add(params));
    }

    /**
     * 新增【课程讲义】
     */
    @ApiOperation(value = "批量新增【课程讲义】")
    @PostMapping("/add/batch")
    //@RequiresPermissions("courseAttachment:add")
    @Log(title = "批量新增【课程讲义】", businessType = BusinessType.INSERT)
    public R<Boolean> addBatch(@RequestBody @Validated CourseAttachmentAddBatchRequest params) {
        return R.ok(this.courseAttachmentService.addBatch(params));
    }

    /**
     * 删除【课程讲义】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("课程讲义删除")
    //@RequiresPermissions("courseAttachment:delete")
    @Log(title = "删除【课程讲义】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest idList) {
        return R.ok(this.courseAttachmentService.deleteByIds(idList.getIdList()));
    }

    /**
     * 直播结束生成回放保存直播总时长
     */
    @ApiOperation(value = "直播结束生成回放保存直播总时长")
    @PostMapping("/resourceFileDurationRecording")
    @AutoSelectOrg
    public R<Boolean> resourceFileDurationRecording(@RequestBody ResourceFileDurationRecordDTO params) {
        try {
         this.courseComponentAttachmentService.resourceFileDurationRecording(params);

        } catch (Exception e) {
            log.error("直播结束生成回放保存直播总时长出错", e);
        }
        return R.ok(true);
    }

    /**
     * 修改【课程讲义名称】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程讲义-修改")
    @PostMapping("/changeName")
    //@RequiresPermissions("courseComponent:changeName")
    @Log(title = "修改【课程讲义名称】", businessType = BusinessType.UPDATE)
    public R<Boolean> changeName(@RequestBody @Validated ComponentChangeNameRequest params) {
        return R.ok(this.courseAttachmentService.changeName(params));
    }

    /**
     * 课程讲次内容附近名称-修改
     *
     * @param params
     * @return
     */
    @ApiIgnore
    @ApiOperation(value = "课程讲次内容附近名称-修改")
    @PostMapping("/update/resourceFileName")
    @Log(title = "更新【课程讲次内容附近名称】", businessType = BusinessType.UPDATE)
    public R<Boolean> updateResourceFileName(@RequestBody @Validated CourseComponentUpdateResourceFileNameRequest params) {
        return R.ok(this.courseComponentAttachmentService.lambdaUpdate()
                .set(CourseComponentAttachment::getResourceFileName, params.getResourceFileName())
                .set(CourseComponentAttachment::getUpdateBy, SecurityContextHolder.getUserId())
                .set(CourseComponentAttachment::getUpdateTime, LocalDateTime.now())
                .eq(CourseComponentAttachment::getResourceFileId, params.getResourceFileId())
                .update());
    }

}

