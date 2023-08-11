package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.course.standard.pojo.courseuserdeptvisible.*;
import cn.qbs.wa.teach.course.standard.service.CourseUserDeptVisibleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



/**
 * 【课程可见学员部门】(CourseUserDeptVisible)表控制层
 *
 * @author makejava
 * @since 2022-05-09 16:06:54
 */
@RestController
@RequestMapping("courseUserDeptVisible")
public class CourseUserDeptVisibleController {

    /**
     * 服务对象
     */
    @Resource
    private CourseUserDeptVisibleService courseUserDeptVisibleService;


    /**
     * 新增【课程可见学员部门】
     *
     * @param params
     * @return 
     */
    @PostMapping("add")
    //@RequiresPermissions("courseUserDeptVisible:add")
    //@Log(title = "新增【课程可见学员部门】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseUserDeptVisibleAddRequest params) {
        return R.ok(this.courseUserDeptVisibleService.add(params));
    }
    
    /**
     * 分页查询【课程可见学员部门】
     *
     * @param params
     * @return 
     */
    @PostMapping("page")
    //@RequiresPermissions("courseUserDeptVisible:page")
    //@Log(title = "分页查询【课程可见学员部门】", businessType = BusinessType.OTHER)
    public R<IPage<CourseUserDeptVisiblePageResponse>> page(@RequestBody CourseUserDeptVisiblePageRequest params) {
        return R.ok(this.courseUserDeptVisibleService.page(params));
    }

    /**
     * 查询【课程可见学员部门】详情
     *
     * @param id 主键
     * @return 
     */
    @PostMapping("detail")
    //@RequiresPermissions("courseUserDeptVisible:details")
    //@Log(title = "【课程可见学员部门】详情", businessType = BusinessType.OTHER)
    public R<CourseUserDeptVisibleDetailResponse> detail(Long id) {
        return R.ok(this.courseUserDeptVisibleService.detail(id));
    }

    /**
     * 修改【课程可见学员部门】
     *
     * @param params 
     * @return 
     */
    @PostMapping("update")
    //@RequiresPermissions("courseUserDeptVisible:update")
    //@Log(title = "更新【课程可见学员部门】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseUserDeptVisibleUpdateRequest params) {
        return R.ok(this.courseUserDeptVisibleService.update(params));
    }

    /**
     * 删除【课程可见学员部门】
     *
     * @param idList 主键集合
     * @return 
     */
    @PostMapping("delete")
    //@RequiresPermissions("courseUserDeptVisible:delete")
    //@Log(title = "删除【课程可见学员部门】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestParam("ids") List<Long> idList) {
        return R.ok(this.courseUserDeptVisibleService.deleteByIds(idList));
    }
    
}

