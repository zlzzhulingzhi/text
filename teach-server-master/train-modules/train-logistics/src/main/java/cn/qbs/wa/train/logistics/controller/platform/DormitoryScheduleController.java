package cn.qbs.wa.train.logistics.controller.platform;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.entity.DormitorySchedule;
import cn.qbs.wa.train.logistics.excel.DormitoryScheduleDataParseResult;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.*;
import cn.qbs.wa.train.logistics.service.platform.DormitoryScheduleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import org.springframework.web.multipart.MultipartFile;


/**
 * 宿舍排期表(DormitorySchedule)表控制层
 *
 * @author makejava
 * @since 2022-10-18 10:03:58
 */
@RestController
@RequestMapping("dormitorySchedule")
@Api(tags = "宿舍排期管理")
public class DormitoryScheduleController {

    /**
     * 服务对象
     */
    @Resource
    private DormitoryScheduleService dormitoryScheduleService;


    /**
     * 新增宿舍排期表
     *
     * @param params
     * @return
     */
    @PostMapping("batchAdd")
    @ApiOperation("批量添加宿舍排期表")
    @RequiresPermissions("Management:Dormitory:Record")
    //@Log(title = "新增宿舍排期表", businessType = BusinessType.INSERT)
    public R<List<DormitorySchedule>> batchAdd(@RequestBody @Validated List<DormitoryScheduleDataParseResult> params) {
        try {
            return R.ok(this.dormitoryScheduleService.batchAdd(params));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    /**
     * 分页查询宿舍排期表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @RequiresPermissions("Management:Dormitory:Record")
    @ApiOperation("分页查询宿舍排期表")
    //@Log(title = "分页查询宿舍排期表", businessType = BusinessType.OTHER)
    public R<IPage<DormitorySchedulePageResponse>> page(@RequestBody DormitorySchedulePageRequest params) {
        return R.ok(this.dormitoryScheduleService.page(params));
    }

    /**
     * 查询宿舍排期表详情
     *
     * @param id 主键
     * @return
     *//*
    @PostMapping("detail")
    //@RequiresPermissions("dormitorySchedule:details")
    //@Log(title = "宿舍排期表详情", businessType = BusinessType.OTHER)
    public R<DormitoryScheduleDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.dormitoryScheduleService.detail(request.getId()));
    }

    *//**
     * 修改宿舍排期表
     *
     * @param params
     * @return
     *//*
    @PostMapping("update")
    //@RequiresPermissions("dormitorySchedule:update")
    //@Log(title = "更新宿舍排期表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated DormitoryScheduleUpdateRequest params) {
        return R.ok(this.dormitoryScheduleService.update(params));
    }*/

    /**
     * 删除宿舍排期表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除宿舍排期表")
    @RequiresPermissions("Management:Dormitory:Record")
    //@Log(title = "删除宿舍排期表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.dormitoryScheduleService.deleteByIds(request.getIdList()));
    }

    @ApiOperation(value = "预导入宿舍排期信息")
    @PostMapping("preview")
    @RequiresPermissions("Management:Dormitory:Record")
    public R importPre(MultipartFile file){
        return R.ok(dormitoryScheduleService.importPre(file));
    }


    /*@PostMapping("getDormitoryState")
    @ApiOperation("查询房态监控")
    @RequiresPermissions("Management:Dormitory:Schedule")
    public R<List<DormitoryStateResponse>> getDormitoryState(@RequestBody @Validated DormitoryStateRequest params) {
        return R.ok(this.dormitoryScheduleService.getDormitoryState(params));
    }*/


}

