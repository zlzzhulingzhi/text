package cn.qbs.wa.train.logistics.controller.manage;


import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.pojo.dormitory.*;
import cn.qbs.wa.train.logistics.service.manage.DormitoryManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;



/**
 * 宿舍表(Dormitory)表控制层
 *
 * @author makejava
 * @since 2022-10-08 17:40:00
 */
@RestController
@RequestMapping("manageDormitory")
@Api(tags = "宿舍管理")
public class DormitoryManageController {

    /**
     * 服务对象
     */
    @Resource
    private DormitoryManageService dormitoryManageService;

    /**
     * 分页查询宿舍表
     *
     * @param params
     * @return
     */
    @PostMapping("managePage")
    @ApiOperation("分页查询宿舍")
    //@RequiresPermissions("dormitory:page")
    //@Log(title = "分页查询宿舍表", businessType = BusinessType.OTHER)
    public R<IPage<DormitoryPageResponse>> page(@RequestBody DormitoryPageRequest params) {
        return R.ok(this.dormitoryManageService.page(params));
    }

    /**
     * 查询宿舍表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("manageDetail")
    @ApiOperation("宿舍详情")
    //@RequiresPermissions("dormitory:details")
    //@Log(title = "宿舍表详情", businessType = BusinessType.OTHER)
    public R<DormitoryDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.dormitoryManageService.detail(request.getId()));
    }

}

