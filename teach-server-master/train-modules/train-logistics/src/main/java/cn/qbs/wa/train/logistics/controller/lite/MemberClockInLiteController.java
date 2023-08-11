package cn.qbs.wa.train.logistics.controller.lite;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.pojo.clazz.LiteClazzResponse;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentDetailResponse;
import cn.qbs.wa.train.logistics.pojo.memberclockin.*;
import cn.qbs.wa.train.logistics.service.MemberClockInService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 学员打卡记录(MemberClockIn)表控制层
 *
 * @author makejava
 * @since 2022-12-26 15:42:22
 */
@RestController
@RequestMapping("lite/memberClockIn")
@Api(tags = "小程序学员打卡功能接口")
public class MemberClockInLiteController {

    /**
     * 服务对象
     */
    @Resource
    private MemberClockInService memberClockInService;


    /**
     * 新增学员打卡记录
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("memberClockIn:add")
    //@Log(title = "新增学员打卡记录", businessType = BusinessType.INSERT)
    @ApiOperation("新增学员打卡记录")
    public R<Boolean> add(@RequestBody @Validated MemberClockInAddRequest params) {
        return R.ok(this.memberClockInService.add(params));
    }

    /**
     * 分页查询学员打卡记录
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("memberClockIn:page")
    //@Log(title = "分页查询学员打卡记录", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询学员打卡记录")
    public R<IPage<MemberClockInPageResponse>> page(@RequestBody @Validated MemberClockInSelectPageRequest params) {
        return R.ok(this.memberClockInService.pageMember(params));
    }


    @PostMapping("calendar/list")
    //@RequiresPermissions("memberClockIn:page")
    //@Log(title = "分页查询学员打卡记录", businessType = BusinessType.OTHER)
    @ApiOperation("日历学员打卡记录")
    public R<List<MemberClockInCalendarResponse>> calendarList(@RequestBody @Validated MemberClockInCalendarRequest params) {
        return R.ok(this.memberClockInService.calendarList(params));
    }

    @PostMapping("calendar/detail")
    //@RequiresPermissions("memberClockIn:page")
    //@Log(title = "分页查询学员打卡记录", businessType = BusinessType.OTHER)
    @ApiOperation("日历学员打卡详情记录")
    public R<MemberClockInCalendarDetailOverViewResponse> calendarDetail(@RequestBody @Validated MemberClockInCalendarDetailRequest params) {
        return R.ok(this.memberClockInService.calendarDetail(params));
    }

    @PostMapping("select/clazz")
    //@RequiresPermissions("memberClockIn:page")
    //@Log(title = "分页查询学员打卡记录", businessType = BusinessType.OTHER)
    @ApiOperation("打卡可选班级列表")
    public R<List<LiteClazzResponse>> selectClazzList() {
        return R.ok(this.memberClockInService.selectClazzList());
    }




    /**
     * 修改学员打卡记录
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("memberClockIn:update")
    //@Log(title = "更新学员打卡记录", businessType = BusinessType.UPDATE)
    @ApiOperation("更新学员打卡记录")
    public R<Boolean> update(@RequestBody @Validated MemberClockInUpdateRequest params) {
        return R.ok(this.memberClockInService.update(params));
    }

    /**
     * 删除学员打卡记录
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("memberClockIn:delete")
    //@Log(title = "删除学员打卡记录", businessType = BusinessType.DELETE)
    @ApiOperation("批量删除打卡")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.memberClockInService.deleteByIds(request.getIdList()));
    }

}

