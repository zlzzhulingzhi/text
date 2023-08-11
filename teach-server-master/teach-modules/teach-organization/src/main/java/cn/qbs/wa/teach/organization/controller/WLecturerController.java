package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.pojo.wlecturer.*;
import cn.qbs.wa.teach.organization.service.WLecturerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * 插件-讲师表(WLecturer)表控制层
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@RestController
@RequestMapping("WLecturer")
public class WLecturerController {

    /**
     * 服务对象
     */
    @Resource
    private WLecturerService wLecturerService;


    /**
     * 新增插件-讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("wLecturer:add")
    //@Log(title = "新增插件-讲师表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated WLecturerAddRequest params) {
        return R.ok(this.wLecturerService.add(params));
    }

    /**
     * 分页查询插件-讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("wLecturer:page")
    //@Log(title = "分页查询插件-讲师表", businessType = BusinessType.OTHER)
    public R<IPage<WLecturerPageResponse>> page(@RequestBody WLecturerPageRequest params) {
        return R.ok(this.wLecturerService.page(params));
    }

    /**
     * 查询插件-讲师表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("wLecturer:details")
    //@Log(title = "插件-讲师表详情", businessType = BusinessType.OTHER)
    public R<WLecturerDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.wLecturerService.detail(request.getId()));
    }

    /**
     * 修改插件-讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("wLecturer:update")
    //@Log(title = "更新插件-讲师表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated WLecturerUpdateRequest params) {
        return R.ok(this.wLecturerService.update(params));
    }

    /**
     * 删除插件-讲师表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("wLecturer:delete")
    //@Log(title = "删除插件-讲师表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.wLecturerService.deleteByIds(request.getIdList()));
    }

}

