package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.course.standard.pojo.courseusergroupvisible.*;
import cn.qbs.wa.teach.course.standard.service.CourseUserGroupVisibleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



/**
 * 【课程可见学员分组】(CourseUserGroupVisible)表控制层
 *
 * @author makejava
 * @since 2022-05-09 16:07:59
 */
@RestController
@RequestMapping("courseUserGroupVisible")
public class CourseUserGroupVisibleController {

    /**
     * 服务对象
     */
    @Resource
    private CourseUserGroupVisibleService courseUserGroupVisibleService;


    /**
     * 新增【课程可见学员分组】
     *
     * @param params
     * @return 
     */
    @PostMapping("add")
    //@RequiresPermissions("courseUserGroupVisible:add")
    //@Log(title = "新增【课程可见学员分组】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseUserGroupVisibleAddRequest params) {
        return R.ok(this.courseUserGroupVisibleService.add(params));
    }
    
    /**
     * 分页查询【课程可见学员分组】
     *
     * @param params
     * @return 
     */
    @PostMapping("page")
    //@RequiresPermissions("courseUserGroupVisible:page")
    //@Log(title = "分页查询【课程可见学员分组】", businessType = BusinessType.OTHER)
    public R<IPage<CourseUserGroupVisiblePageResponse>> page(@RequestBody CourseUserGroupVisiblePageRequest params) {
        return R.ok(this.courseUserGroupVisibleService.page(params));
    }

    /**
     * 查询【课程可见学员分组】详情
     *
     * @param id 主键
     * @return 
     */
    @PostMapping("detail")
    //@RequiresPermissions("courseUserGroupVisible:details")
    //@Log(title = "【课程可见学员分组】详情", businessType = BusinessType.OTHER)
    public R<CourseUserGroupVisibleDetailResponse> detail(Long id) {
        return R.ok(this.courseUserGroupVisibleService.detail(id));
    }

    /**
     * 修改【课程可见学员分组】
     *
     * @param params 
     * @return 
     */
    @PostMapping("update")
    //@RequiresPermissions("courseUserGroupVisible:update")
    //@Log(title = "更新【课程可见学员分组】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseUserGroupVisibleUpdateRequest params) {
        return R.ok(this.courseUserGroupVisibleService.update(params));
    }

    /**
     * 删除【课程可见学员分组】
     *
     * @param idList 主键集合
     * @return 
     */
    @PostMapping("delete")
    //@RequiresPermissions("courseUserGroupVisible:delete")
    //@Log(title = "删除【课程可见学员分组】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestParam("ids") List<Long> idList) {
        return R.ok(this.courseUserGroupVisibleService.deleteByIds(idList));
    }
    
}

