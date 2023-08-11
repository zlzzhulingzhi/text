package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.course.common.entity.CourseMessage;
import cn.qbs.wa.teach.course.standard.pojo.coursemessagereply.*;
import cn.qbs.wa.teach.course.standard.service.CourseMessageReplyService;
import cn.qbs.wa.teach.course.standard.service.CourseMessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

import static cn.qbs.wa.teach.course.standard.constants.CourseConstants.REPLY_STATUS_NOT_REPLY;
import static cn.qbs.wa.teach.course.standard.constants.CourseConstants.REPLY_STATUS_REPLIED;


/**
 * 【课程留言回复记录】(CourseMessageReply)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-25 15:22:17
 */
@Slf4j
@Api(tags = "留言回复")
@RestController
@RequestMapping("/message/reply")
public class CourseMessageReplyController {

    /**
     * 服务对象
     */
    @Resource
    private CourseMessageReplyService courseMessageReplyService;
    @Resource
    private CourseMessageService courseMessageService;


    /**
     * 新增【课程留言回复记录】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "留言回复-用户回复")
    @PostMapping("/add")
    //@RequiresPermissions("courseMessageReply:add")
    //@Log(title = "新增【课程留言回复记录】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseMessageReplyAddRequest params) {
        return R.ok(this.courseMessageReplyService.add(params));
    }

    /**
     * 新增【课程留言回复记录】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "留言回复-官方回复")
    @PostMapping("/add-official")
    //@RequiresPermissions("courseMessageReply:add")
    //@Log(title = "新增【课程留言回复记录】", businessType = BusinessType.INSERT)
    public R<Boolean> addOfficial(@RequestBody @Validated CourseMessageReplyAddRequest params) {
        Optional.ofNullable(SecurityUtils.getLoginUser()).ifPresent(u -> params.setReplyNickName(u.getUsername()));
        CourseMessage courseMessage = this.courseMessageService.getById(params.getMessageId());
        if (courseMessage != null) {
            if (REPLY_STATUS_NOT_REPLY == Optional.ofNullable(courseMessage.getReplyStatus()).orElse(REPLY_STATUS_NOT_REPLY)) {
                // 更新对应留言的回复状态
                CourseMessage request = new CourseMessage();
                request.setId(params.getMessageId());
                request.setReplyStatus(REPLY_STATUS_REPLIED);
                this.courseMessageService.updateById(request);
            }
            return R.ok(this.courseMessageReplyService.add(params));
        } else {
            log.info("留言不存在。 message_id:{}", params.getMessageId());
            return R.fail("留言不存在。");
        }
    }

    /**
     * 分页查询【课程留言回复记录】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "留言回复-分页")
    @PostMapping("/page")
    //@RequiresPermissions("courseMessageReply:page")
    //@Log(title = "分页查询【课程留言回复记录】", businessType = BusinessType.OTHER)
    public R<IPage<CourseMessageReplyPageResponse>> page(@RequestBody CourseMessageReplyPageRequest params) {
        return R.ok(this.courseMessageReplyService.page(params));
    }

    /**
     * 删除【课程留言回复记录】
     *
     * @param params 主键集合
     * @return
     */
    @ApiOperation(value = "留言回复-删除")
    @PostMapping("/delete")
    //@RequiresPermissions("courseMessageReply:delete")
    //@Log(title = "删除【课程留言回复记录】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest params) {
        return R.ok(this.courseMessageReplyService.deleteByIds(params.getIdList()));
    }

    /**
     * 查询【留言回复】详情
     * @param params 回复id
     * @return
     */
    @ApiOperation(value = "留言回复-详情")
    @PostMapping("/detail")
    //@RequiresPermissions("courseMessageReply:detail")
    //@Log(title = "查询【课程留言回复详情】", businessType = BusinessType.OTHER)
    public R<CourseMessageReplyDetailResponse> detail(@RequestBody @Validated IdRequest params) {
        return R.ok(this.courseMessageReplyService.detail(params.getId()));
    }

    /**
     * 修改【留言回复】
     *
     * @param params 更新信息
     * @return
     */
    @ApiOperation(value = "留言回复-修改")
    @PostMapping("/update")
    //@RequiresPermissions("courseMessageReply:update")
    //@Log(title = "更新【留言回复】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseMessageReplyUpdateRequest params) {
        return R.ok(this.courseMessageReplyService.update(params));
    }
}

