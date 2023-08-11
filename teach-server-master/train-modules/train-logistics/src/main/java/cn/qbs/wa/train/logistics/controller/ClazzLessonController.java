package cn.qbs.wa.train.logistics.controller;


import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.train.logistics.entity.ClazzLesson;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.*;
import cn.qbs.wa.train.logistics.service.ClazzLessonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;


/**
 * 班级课程(ClazzLesson)表控制层
 *
 * @author makejava
 * @since 2023-03-13 20:12:57
 */
@RestController
@RequestMapping("clazzLesson")
@Api(tags = "班级课程")
public class ClazzLessonController {

    /**
     * 服务对象
     */
    @Resource
    private ClazzLessonService clazzLessonService;


    /**
     * 新增班级课程
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增班级课程")
    //@RequiresPermissions("clazzLesson:add")
    //@Log(title = "新增班级课程", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated ClazzLessonAddRequest params) {
        return R.ok(this.clazzLessonService.add(params));
    }

    /**
     * 分页查询班级课程
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("clazzLesson:page")
    //@Log(title = "分页查询班级课程", businessType = BusinessType.OTHER)
    public R<IPage<ClazzLessonPageResponse>> page(@RequestBody ClazzLessonPageRequest params) {
        return R.ok(this.clazzLessonService.page(params));
    }

    /**
     * 查询班级课程详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("班级课程详情")
    //@RequiresPermissions("clazzLesson:details")
    //@Log(title = "班级课程详情", businessType = BusinessType.OTHER)
    public R<ClazzLessonDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.clazzLessonService.detail(request.getId()));
    }

    /**
     * 修改班级课程
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("修改班级课程")
    //@RequiresPermissions("clazzLesson:update")
    //@Log(title = "更新班级课程", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ClazzLessonUpdateRequest params) {
        return R.ok(this.clazzLessonService.update(params));
    }

    @PostMapping("sort")
    @ApiOperation("修改班级课程排序")
    //@RequiresPermissions("clazzLesson:update")
    //@Log(title = "更新班级课程", businessType = BusinessType.UPDATE)
    public R<Boolean> sort(@RequestBody @Validated ClazzLessonUpdateRequest params) {
        return R.ok(this.clazzLessonService.sort(params));
    }

    /**
     * 删除班级课程
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除班级课程")
    //@RequiresPermissions("clazzLesson:delete")
    //@Log(title = "删除班级课程", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.clazzLessonService.deleteByIds(request.getIdList()));
    }

    @PostMapping("list")
    @ApiOperation("班级课程列表")
    public R<List<ClazzLesson>> list(@RequestBody ClazzLessonPageRequest request) {
        List<ClazzLesson> clazzLessonList;
        if(request.getOrgId()!=null){
            clazzLessonList=this.clazzLessonService.lambdaQuery().eq(ClazzLesson::getClazzId,request.getClazzId()).
                    eq(ClazzLesson::getOrgId,request.getOrgId()).orderByAsc(ClazzLesson::getSort).list();
        }else {
            clazzLessonList=this.clazzLessonService.lambdaQuery().eq(ClazzLesson::getClazzId,request.getClazzId()).
                    eq(ClazzLesson::getOrgId,SecurityContextHolder.getOrgId()).orderByAsc(ClazzLesson::getSort).list();
        }
        return R.ok(clazzLessonList);
    }

    @PostMapping("addBatch")
    @ApiOperation("批量新增班级课程")
    //@RequiresPermissions("clazzLesson:add")
    //@Log(title = "新增班级课程", businessType = BusinessType.INSERT)
    public R<Boolean> addBatch(@RequestBody @Validated List<ClazzLessonAddRequest> params) {
        return R.ok(this.clazzLessonService.addBatch(params));
    }

}

