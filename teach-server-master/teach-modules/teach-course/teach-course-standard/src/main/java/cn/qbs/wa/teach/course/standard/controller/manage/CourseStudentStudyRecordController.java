package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.course.standard.pojo.coursestudentstudyrecord.*;
import cn.qbs.wa.teach.course.standard.service.CourseStudentStudyRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;



/**
 * 【课程学习记录】(CourseStudentStudyRecord)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@ApiIgnore
@RestController
@RequestMapping("courseStudentStudyRecord")
public class CourseStudentStudyRecordController {

    /**
     * 服务对象
     */
    @Resource
    private CourseStudentStudyRecordService courseStudentStudyRecordService;


    /**
     * 新增【课程学习记录】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("courseStudentStudyRecord:add")
    //@Log(title = "新增【课程学习记录】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseStudentStudyRecordAddRequest params) {
        return R.ok(this.courseStudentStudyRecordService.add(params));
    }

    /**
     * 分页查询【课程学习记录】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("courseStudentStudyRecord:page")
    //@Log(title = "分页查询【课程学习记录】", businessType = BusinessType.OTHER)
    public R<IPage<CourseStudentStudyRecordPageResponse>> page(@RequestBody CourseStudentStudyRecordPageRequest params) {
        return R.ok(this.courseStudentStudyRecordService.page(params));
    }

    /**
     * 查询【课程学习记录】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("courseStudentStudyRecord:details")
    //@Log(title = "【课程学习记录】详情", businessType = BusinessType.OTHER)
    public R<CourseStudentStudyRecordDetailResponse> detail(Long id) {
        return R.ok(this.courseStudentStudyRecordService.detail(id));
    }

    /**
     * 修改【课程学习记录】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("courseStudentStudyRecord:update")
    //@Log(title = "更新【课程学习记录】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseStudentStudyRecordUpdateRequest params) {
        return R.ok(this.courseStudentStudyRecordService.update(params));
    }

    /**
     * 删除【课程学习记录】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("courseStudentStudyRecord:delete")
    //@Log(title = "删除【课程学习记录】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestParam("ids") List<Long> idList) {
        return R.ok(this.courseStudentStudyRecordService.deleteByIds(idList));
    }

}

