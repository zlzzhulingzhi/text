package cn.qbs.wa.teach.exam.admin.controller;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom.*;
import cn.qbs.wa.teach.exam.admin.service.ExamineeLiveRoomService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



/**
 * 考生直播房间(ExamineeLiveRoom)表控制层
 *
 * @author makejava
 * @since 2022-01-04 11:41:03
 */
@RestController
@RequestMapping("examineeLiveRoom")
public class ExamineeLiveRoomController {

    /**
     * 服务对象
     */
    @Resource
    private ExamineeLiveRoomService examineeLiveRoomService;


    /**
     * 新增监控房间并直播
     *
     * @param params
     * @return 
     */
    @PostMapping("live")
    @ApiOperation("开启直播")
    //@RequiresPermissions("examineeLiveRoom:add")
    //@Log(title = "新增考生直播房间", businessType = BusinessType.INSERT)
    public R<PushOrPullShowResult> live(@RequestBody @Validated ExamineeLiveRoomAddRequest params) {
        return R.ok(this.examineeLiveRoomService.live(params));
    }
    
    /**
     * 分页查询考生直播房间
     *
     * @param params
     * @return 
     */
    @PostMapping("page")
    @ApiOperation("直播列表")
    //@RequiresPermissions("examineeLiveRoom:page")
    //@Log(title = "分页查询考生直播房间", businessType = BusinessType.OTHER)
    public R<List<ExamineeLiveRoomPageResponse>> page(@RequestBody ExamineeLiveRoomPageRequest params) {
        return R.ok(this.examineeLiveRoomService.page(params));
    }

    /**
     * 监考回放
     *
     * @param id 主键
     * @return 
     */
    @PostMapping("detail")
    @ApiOperation("监考回放")
    //@RequiresPermissions("examineeLiveRoom:details")
    //@Log(title = "监考回放", businessType = BusinessType.OTHER)
    public R<List<ExamineeLiveRoomPageResponse>> detail(@RequestBody ExamineeLiveRoomPageRequest params) {
        return R.ok(this.examineeLiveRoomService.detail(params));
    }

    /**
     * 违规后结束考试
     *
     * @param params 
     * @return 
     */
    @PostMapping("update")
    @ApiOperation("违规后结束考试")
    //@RequiresPermissions("examineeLiveRoom:update")
    //@Log(title = "违规后结束考试", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ExamineeLiveRoomUpdateRequest params) {
        return R.ok(this.examineeLiveRoomService.update(params));
    }

    /**
     * 删除考生直播房间
     *
     * @param idList 主键集合
     * @return 
     */
    @PostMapping("delete")
    //@RequiresPermissions("examineeLiveRoom:delete")
    //@Log(title = "删除考生直播房间", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestParam("ids") List<Long> idList) {
        return R.ok(this.examineeLiveRoomService.deleteByIds(idList));
    }
    
}

