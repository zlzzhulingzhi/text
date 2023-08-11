package cn.qbs.wa.train.logistics.controller;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.pojo.memberclockin.*;
import cn.qbs.wa.train.logistics.service.MemberClockInService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * 学员打卡记录(MemberClockIn)表控制层
 *
 * @author makejava
 * @since 2022-12-26 15:42:22
 */
@RestController
@RequestMapping("memberClockIn")
public class MemberClockInController {

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
    public R<IPage<MemberClockInPageResponse>> page(@RequestBody MemberClockInPageRequest params) {
        return R.ok(this.memberClockInService.page(params));
    }

    /**
     * 查询学员打卡记录详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("memberClockIn:details")
    //@Log(title = "学员打卡记录详情", businessType = BusinessType.OTHER)
    public R<MemberClockInDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.memberClockInService.detail(request.getId()));
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
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.memberClockInService.deleteByIds(request.getIdList()));
    }

    @PostMapping("pages")
    //@RequiresPermissions("memberClockIn:page")
    //@Log(title = "分页查询学员打卡记录", businessType = BusinessType.OTHER)
    public R<IPage<MemberClockInPageResponse>> pages(@RequestBody MemberClockInPagesRequest params) {
        return R.ok(this.memberClockInService.pages(params));
    }

}

