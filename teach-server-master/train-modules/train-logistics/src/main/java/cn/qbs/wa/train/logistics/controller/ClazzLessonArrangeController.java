package cn.qbs.wa.train.logistics.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.ClazzLesson;
import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import cn.qbs.wa.train.logistics.mapper.ClassroomMapper;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.ClazzLessonPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazzlessonarrange.*;
import cn.qbs.wa.train.logistics.service.ClazzLessonArrangeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;


/**
 * 班级课程安排(ClazzLessonArrange)表控制层
 *
 * @author makejava
 * @since 2023-03-14 09:21:38
 */
@RestController
@RequestMapping("clazzLessonArrange")
@Api(tags = "班级课程安排")
public class ClazzLessonArrangeController {

    /**
     * 服务对象
     */
    @Resource
    private ClazzLessonArrangeService clazzLessonArrangeService;

    @Resource
    ClassroomMapper classroomMapper;


    /**
     * 新增班级课程安排
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增班级课程安排")
    //@RequiresPermissions("clazzLessonArrange:add")
    //@Log(title = "新增班级课程安排", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated ClazzLessonArrangeAddRequest params) {
        return R.ok(this.clazzLessonArrangeService.add(params));
    }

    /**
     * 分页查询班级课程安排
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("clazzLessonArrange:page")
    //@Log(title = "分页查询班级课程安排", businessType = BusinessType.OTHER)
    public R<IPage<ClazzLessonArrangePageResponse>> page(@RequestBody ClazzLessonArrangePageRequest params) {
        return R.ok(this.clazzLessonArrangeService.page(params));
    }

    /**
     * 查询班级课程安排详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("班级课程安排详情")
    //@RequiresPermissions("clazzLessonArrange:details")
    //@Log(title = "班级课程安排详情", businessType = BusinessType.OTHER)
    public R<ClazzLessonArrangeDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.clazzLessonArrangeService.detail(request.getId()));
    }

    /**
     * 修改班级课程安排
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新班级课程安排")
    //@RequiresPermissions("clazzLessonArrange:update")
    //@Log(title = "更新班级课程安排", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ClazzLessonArrangeUpdateRequest params) {
        return R.ok(this.clazzLessonArrangeService.update(params));
    }

    @PostMapping("sort")
    @ApiOperation("更新班级课程安排排序")
    //@RequiresPermissions("clazzLessonArrange:update")
    //@Log(title = "更新班级课程安排", businessType = BusinessType.UPDATE)
    public R<Boolean> sort(@RequestBody @Validated ClazzLessonArrangeUpdateRequest params) {
        return R.ok(this.clazzLessonArrangeService.sort(params));
    }

    /**
     * 删除班级课程安排
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除班级课程安排")
    //@RequiresPermissions("clazzLessonArrange:delete")
    //@Log(title = "删除班级课程安排", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.clazzLessonArrangeService.deleteByIds(request.getIdList()));
    }

    @PostMapping("list")
    @ApiOperation("查询班级课程安排列表")
    public R<List<ClazzLessonArrangeListResponse>> list(@RequestBody ClazzLessonArrangePageRequest request) {
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponses=new ArrayList<>();
        List<ClazzLessonArrange> clazzLessonArrangeList;
        if(request.getOrgId()!=null){
            clazzLessonArrangeList=this.clazzLessonArrangeService.lambdaQuery().eq(ClazzLessonArrange::getClazzId,request.getClazzId()).
                    eq(request.getLessonId()!=null,ClazzLessonArrange::getLessonId,request.getLessonId()).eq(ClazzLessonArrange::getOrgId,request.getOrgId()).
                    orderByAsc(ClazzLessonArrange::getSort).list();
        }else {
            clazzLessonArrangeList=this.clazzLessonArrangeService.lambdaQuery().eq(ClazzLessonArrange::getClazzId,request.getClazzId()).
                    eq(request.getLessonId()!=null,ClazzLessonArrange::getLessonId,request.getLessonId()).eq(ClazzLessonArrange::getOrgId, SecurityContextHolder.getOrgId()).
                    orderByAsc(ClazzLessonArrange::getSort).list();
        }
        if(CollectionUtils.isNotEmpty(clazzLessonArrangeList)){
            clazzLessonArrangeListResponses=BeanUtil.copyToList(clazzLessonArrangeList, ClazzLessonArrangeListResponse.class);
            for (ClazzLessonArrangeListResponse clazzLessonArrangeListResponse:clazzLessonArrangeListResponses) {
                if( clazzLessonArrangeListResponse.getClassroomId()!=null){
                    Classroom classroom=classroomMapper.selectById( clazzLessonArrangeListResponse.getClassroomId());
                    if(classroom!=null){
                        clazzLessonArrangeListResponse.setRoomNo(classroom.getRoomNo());
                        clazzLessonArrangeListResponse.setRoomType(classroom.getRoomType());
                        clazzLessonArrangeListResponse.setBuilding(classroom.getBuilding());
                    }
                }
            }
        }
        return R.ok(clazzLessonArrangeListResponses);
    }

    @PostMapping("listV2")
    @ApiOperation("查询课表")
    public R<List<ClazzLessonArrangeListResponse>> listV2(@RequestBody ClazzLessonArrangePageRequest request) {
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponses=clazzLessonArrangeService.listV2(request);
        return R.ok(clazzLessonArrangeListResponses);
    }

    @PostMapping("listV3")
    @ApiOperation("查询周课表")
    public R<List<ClazzLessonArrangeListResponse>> listV3(@RequestBody ClazzLessonArrangePageRequest request) {
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponses=clazzLessonArrangeService.listV3(request);
        return R.ok(clazzLessonArrangeListResponses);
    }

    @PostMapping("getWeek")
    @ApiOperation("课表周数")
    public R<ClazzLessonArrangeWeekResponse> getWeek(@RequestBody ClazzLessonArrangePageRequest request) {
        ClazzLessonArrangeWeekResponse clazzLessonArrangeWeekResponse=clazzLessonArrangeService.getWeek(request);
        return R.ok(clazzLessonArrangeWeekResponse);
    }



    @PostMapping("listV4")
    @ApiOperation("查询月课表")
    public R<List<ClazzLessonArrangeListResponse>> listV4(@RequestBody ClazzLessonArrangePageRequest request) {
        List<ClazzLessonArrangeListResponse> clazzLessonArrangeListResponses=clazzLessonArrangeService.listV4(request);
        return R.ok(clazzLessonArrangeListResponses);
    }


    @PostMapping("addBatch")
    @ApiOperation("批量新增班级课程安排")
    //@RequiresPermissions("clazzLessonArrange:add")
    //@Log(title = "新增班级课程安排", businessType = BusinessType.INSERT)
    public R<Boolean> addBatch(@RequestBody @Validated List<ClazzLessonArrangeAddRequest> params) {
        return R.ok(this.clazzLessonArrangeService.addBatch(params));
    }

}

