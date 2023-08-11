package cn.qbs.wa.train.logistics.controller.manage;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.*;
import cn.qbs.wa.train.logistics.service.manage.ClazzStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;

import java.util.*;


/**
 * 班级学员表(ClazzStudent)表控制层
 *
 * @author makejava
 * @since 2022-10-08 17:28:14
 */
@RestController
@RequestMapping("clazzStudent")
@Api(tags = "班级学员管理")
public class ClazzStudentController {

    /**
     * 服务对象
     */
    @Resource
    private ClazzStudentService clazzStudentService;


    /**
     * 新增班级学员表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增班级学员")
    @RequiresPermissions("Class:Manage")
    //@Log(title = "新增班级学员表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated List<ClazzStudentAddRequest> params) {
        return R.ok(this.clazzStudentService.add(params));
    }

    /**
     * 分页查询班级学员表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询班级学员")
    @RequiresPermissions("Class:Manage")
    //@Log(title = "分页查询班级学员表", businessType = BusinessType.OTHER)
    public R<IPage<ClazzStudentPageResponse>> page(@RequestBody ClazzStudentPageRequest params) {
        return R.ok(this.clazzStudentService.page(params));
    }

    /**
     * 查询班级学员表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("班级详情学员")
    //@RequiresPermissions("clazzStudent:details")
    //@Log(title = "班级学员表详情", businessType = BusinessType.OTHER)
    public R<ClazzStudentDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.clazzStudentService.detail(request.getId()));
    }

    /**
     * 修改班级学员表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("更新班级学员")
    //@RequiresPermissions("clazzStudent:update")
    //@Log(title = "更新班级学员表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ClazzStudentUpdateRequest params) {
        return R.ok(this.clazzStudentService.update(params));
    }

    /**
     * 删除班级学员表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除班级学员")
    @RequiresPermissions("Class:Manage")
    //@Log(title = "删除班级学员表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.clazzStudentService.deleteByIds(request.getIdList()));
    }

    @PostMapping("getByOrgId")
    public R<List<ClazzStudentPageResponse>> getByOrgId(@RequestBody ClazzStudentPageRequest params) {
        List<ClazzStudentPageResponse> clazzStudentPageResponseList=new ArrayList<>();
        List<ClazzStudent> clazzStudentList=clazzStudentService.lambdaQuery().eq(ClazzStudent::getOrgId,params.getOrgId()).eq(ClazzStudent::getClazzId,params.getClazzId()).list();
        for (ClazzStudent clazzStudent:clazzStudentList){
            ClazzStudentPageResponse clazzStudentPageResponse=new ClazzStudentPageResponse();
            clazzStudentPageResponse.setStudentId(clazzStudent.getStudentId());
            clazzStudentPageResponseList.add(clazzStudentPageResponse);
        }
        return R.ok(clazzStudentPageResponseList);
    }

    @PostMapping("/inner/queryClazzLast")
    R<Map<Long, String>> queryClazzLast(@RequestBody IdListRequest idList) {
        Map<Long, String> map = new HashMap<>(idList.getIdList().size());
        // 依次获取学员最近开班的一个班级
        if (CollUtil.isNotEmpty(idList.getIdList())) {
            List<ClazzStudentMap> clazzStudentMaps = clazzStudentService.queryClazzLast(idList.getIdList());
            if (CollUtil.isNotEmpty(clazzStudentMaps)) {
                for (ClazzStudentMap clazzStudentMap : clazzStudentMaps) {
                    map.put(clazzStudentMap.getMemberId(), clazzStudentMap.getClazzName());
                }
            }
        }
        return R.ok(map);
    }
}

