package cn.qbs.wa.train.logistics.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.course.api.RemoteCourseComponentService;
import cn.qbs.wa.teach.course.api.RemoteCourseService;
import cn.qbs.wa.teach.course.api.pojo.DTO.component.CourseComponentExtraDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.lesson.CourseLessonDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.DeptDetailResponseDTO;
import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.pojo.clazz.*;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.ClazzLessonAndArrangeResponse;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageResponse;
import cn.qbs.wa.train.logistics.service.manage.ClazzService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 班级表(Clazz)表控制层
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@RestController
@RequestMapping("clazz")
@Api(tags = "班级管理")
public class ClazzController {

    /**
     * 服务对象
     */
    @Resource
    private ClazzService clazzService;

    @Resource
    private RemoteCourseComponentService remoteCourseComponentService;

    @Resource
    private RemoteCourseService remoteCourseService;
    /**
     * 新增班级表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增班级")
    @RequiresPermissions("Class:Manage")
    //@Log(title = "新增班级表", businessType = BusinessType.INSERT)
    public R<Clazz> add(@RequestBody @Validated ClazzAddRequest params) {
        return R.ok(this.clazzService.add(params));
    }

    /**
     * 分页查询班级表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询班级")
    @RequiresPermissions("Class:Manage")
    //@Log(title = "分页查询班级表", businessType = BusinessType.OTHER)
    public R<IPage<ClazzPageResponse>> page(@RequestBody ClazzPageRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.clazzService.page(params));
    }

    /**
     * 查询班级表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("班级详情")
    @RequiresPermissions("Class:Manage")
    //@Log(title = "班级表详情", businessType = BusinessType.OTHER)
    public R<ClazzDetailResponse> detail(@RequestBody ClazzDetailRequest request) {
        return R.ok(this.clazzService.detail(request));
    }

    /**
     * 修改班级表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新班级")
    @RequiresPermissions("Class:Manage")
    //@Log(title = "更新班级表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ClazzUpdateRequest params) {
        return R.ok(this.clazzService.update(params));
    }

    /**
     * 删除班级表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除班级")
    @RequiresPermissions("Class:Manage")
    //@Log(title = "删除班级表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.clazzService.deleteByIds(request.getIdList()));
    }

    /**
     * 查询班级列表
     */
    @PostMapping("/list")
    @ApiOperation("班级列表")
    public R<List<ClazzListResponse>> list(@RequestBody ClazzListRequest params) {
        // 获取当前登录人机构ID
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(this.clazzService.list(params));
    }

    @PostMapping("/info")
    @ApiOperation("班级信息总览")
    public R<ClazzInfoResponse> list(@RequestBody @Validated IdRequest request) {
        return R.ok(this.clazzService.info(request.getId()));
    }

    @PostMapping("getByOrgId")
    public R<List<ClazzPageResponse>> getByOrgId(@RequestBody ClazzPageRequest params) {
        List<ClazzPageResponse> clazzPageResponseList=new ArrayList<>();
        List<Clazz> clazzList=clazzService.lambdaQuery().eq(Clazz::getOrgId,params.getOrgId()).eq(Clazz::getId,params.getClazzId()).list();
        for (Clazz clazz:clazzList){
            ClazzPageResponse clazzPageResponse=new ClazzPageResponse();
            clazzPageResponse.setCourseId(clazz.getCourseId());
            clazzPageResponseList.add(clazzPageResponse);
        }
        return R.ok(clazzPageResponseList);
    }

    @PostMapping("getCountByCourseId")
    public R<Long> getCountByCourseId(@RequestBody ClazzPageRequest params) {
        Long count=clazzService.lambdaQuery().eq(Clazz::getOrgId,params.getOrgId()).eq(Clazz::getCourseId,params.getCourseId()).count();
        return R.ok(count);
    }

    @PostMapping("getCourseComponent")
    public R<List<CourseComponentExtraDTO>> getCourseComponent(@RequestBody IdRequest params) {
        List<CourseComponentExtraDTO> courseComponentExtraDTOS=remoteCourseComponentService.listByLessonV2(params).getRemoteData();
        return R.ok(courseComponentExtraDTOS);
    }

    @PostMapping("getCourseLessonAndComponent")
    public R<List<ClazzLessonAndArrangeResponse>> getCourseLessonAndComponent(@RequestBody IdRequest params) {
        List<ClazzLessonAndArrangeResponse> clazzLessonAndArrangeResponses=new ArrayList<>();
        List<CourseLessonDTO> courseLessonDTOs=remoteCourseService.getCourseLesson(params).getRemoteData();
        if(CollectionUtils.isNotEmpty(courseLessonDTOs)){
            List<CourseComponentExtraDTO> courseComponentExtraDTOS=remoteCourseComponentService.listByLessonV2(params).getRemoteData();
            //数据转换
            for (CourseLessonDTO courseLessonDTO:courseLessonDTOs) {
                ClazzLessonAndArrangeResponse clazzLessonAndArrangeResponse=new ClazzLessonAndArrangeResponse();
                clazzLessonAndArrangeResponse.setLessonName(courseLessonDTO.getLessonName());
                clazzLessonAndArrangeResponse.setSort(courseLessonDTO.getSort());
                List<ClazzLessonArrange> clazzLessonArrangeList=new ArrayList<>();
                if(CollectionUtils.isNotEmpty(courseComponentExtraDTOS)){
                    for (CourseComponentExtraDTO courseComponentExtraDTO:courseComponentExtraDTOS) {
                        if(courseComponentExtraDTO.getLessonId().equals(courseLessonDTO.getId())){
                            ClazzLessonArrange clazzLessonArrange=new ClazzLessonArrange();
                            BeanUtils.copyProperties(courseComponentExtraDTO,clazzLessonArrange);
                            clazzLessonArrange.setLessonId(null);
                            if(courseComponentExtraDTO.getStartDate()!=null){
                                clazzLessonArrange.setStartDate(courseComponentExtraDTO.getStartDate().atStartOfDay());
                            }
                            if(courseComponentExtraDTO.getEndDate()!=null){
                                clazzLessonArrange.setEndDate(courseComponentExtraDTO.getEndDate().atStartOfDay());
                            }
                            clazzLessonArrange.setContentDescription(courseComponentExtraDTO.getExtraDescription());
                            clazzLessonArrange.setContentName(courseComponentExtraDTO.getComponentName());
                            clazzLessonArrangeList.add(clazzLessonArrange);
                        }
                    }
                    clazzLessonAndArrangeResponse.setClazzLessonArrangeList(clazzLessonArrangeList);
                }
                clazzLessonAndArrangeResponses.add(clazzLessonAndArrangeResponse);
            }

        }
        return R.ok(clazzLessonAndArrangeResponses);
    }

}

