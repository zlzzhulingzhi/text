package cn.qbs.wa.train.logistics.controller.platform;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.Dormitory;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomPageResponse;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomUpdateBatchRequest;
import cn.qbs.wa.train.logistics.pojo.dormitory.*;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.UseDateStateCount;
import cn.qbs.wa.train.logistics.service.platform.DormitoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;

import java.util.List;


/**
 * 宿舍表(Dormitory)表控制层
 *
 * @author makejava
 * @since 2022-10-08 17:40:00
 */
@RestController
@RequestMapping("dormitory")
@Api(tags = "宿舍管理")
public class DormitoryController {

    /**
     * 服务对象
     */
    @Resource
    private DormitoryService dormitoryService;


    /**
     * 新增宿舍表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("添加宿舍")
    @RequiresPermissions("Management:Dormitory")
    //@Log(title = "新增宿舍表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated DormitoryAddRequest params) {
        return R.ok(this.dormitoryService.add(params));
    }

    /**
     * 分页查询宿舍表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询宿舍")
    @RequiresPermissions("Management:Dormitory")
    //@Log(title = "分页查询宿舍表", businessType = BusinessType.OTHER)
    public R<IPage<DormitoryPageResponse>> page(@RequestBody DormitoryPageRequest params) {
        return R.ok(this.dormitoryService.page(params));
    }

    /**
     * 查询宿舍表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("宿舍详情")
    @RequiresPermissions("Management:Dormitory")
    //@Log(title = "宿舍表详情", businessType = BusinessType.OTHER)
    public R<DormitoryDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.dormitoryService.detail(request.getId()));
    }

    /**
     * 修改宿舍表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("修改宿舍")
    @RequiresPermissions("Management:Dormitory")
    //@Log(title = "更新宿舍表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated DormitoryUpdateRequest params) {
        return R.ok(this.dormitoryService.update(params));
    }

    /**
     * 批量修改宿舍表
     *
     * @param params
     * @return
     */
    @PostMapping("updateBatch")
    @ApiOperation("批量修改宿舍")
    //@RequiresPermissions("classroom:update")
    //@Log(title = "更新宿舍表", businessType = BusinessType.UPDATE)
    public R<Boolean> updateBatch(@RequestBody @Validated DormitoryUpdateBatchRequest params) {
        return R.ok(this.dormitoryService.updateBatch(params));
    }

    /**
     * 删除宿舍表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除宿舍")
    @RequiresPermissions("Management:Dormitory")
    //@Log(title = "删除宿舍表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.dormitoryService.deleteByIds(request.getIdList()));
    }

    @PostMapping("list")
    @ApiOperation("查询宿舍列表")
    public R<List<Dormitory>> listDormitory(@RequestBody DormitoryPageRequest request) {
        return R.ok(this.dormitoryService.listDormitory(request));
    }

    @PostMapping("getDormitoryState")
    @ApiOperation("宿舍预订分布")
    @RequiresPermissions("Management:Dormitory")
    public R<IPage<DormitoryPageResponse>> getDormitoryState(@RequestBody DormitoryPageRequest params) {
        return R.ok(this.dormitoryService.getDormitoryState(params));
    }

    @PostMapping("getDormitoryCount")
    @ApiOperation("宿舍数量")
    @RequiresPermissions("Management:Dormitory")
    public R<List<UseDateStateCount>> getDormitoryCount(@RequestBody DormitoryPageRequest params) {
        return R.ok(this.dormitoryService.getDormitoryCount(params));
    }
}

