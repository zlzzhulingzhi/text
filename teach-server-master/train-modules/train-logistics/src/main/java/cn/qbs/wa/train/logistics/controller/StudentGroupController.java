package cn.qbs.wa.train.logistics.controller;/*
package cn.qbs.wa.train.organization.controller;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdStudentRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.organization.pojo.studentgroup.*;
import cn.qbs.wa.train.organization.service.StudentGroupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


*/
/**
 * 学员-分组(StudentGroup)表控制层
 *
 * @author makejava
 * @since 2022-03-28 16:07:14
 *//*

@Api(tags = "学员标签管理")
@RestController
@RequestMapping("studentGroup")
public class StudentGroupController {

    */
/**
     * 服务对象
     *//*

    @Resource
    private StudentGroupService studentGroupService;

    */
/**
     * 学员贴标签
     *
     * @param params
     * @return 
     *//*

    @ApiOperation("学员贴标签")
    @PostMapping("add")
    //@RequiresPermissions("studentGroup:add")
    //@Log(title = "学员贴标签", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated StudentGroupAddRequest params) {
        return R.ok(this.studentGroupService.add(params));
    }

    */
/**
     * 批量学员贴标签
     *
     * @param params
     * @return
     *//*

    @ApiOperation("批量学员贴标签")
    @PostMapping(value="/addList")
    public R addList(@RequestBody StudentGroupAddRequest params){
        return R.ok(this.studentGroupService.addList(params));
    }

    */
/**
     * 分页查询学员-分组
     *
     * @param params
     * @return 
     *//*

    @PostMapping("page")
    //@RequiresPermissions("studentGroup:page")
    //@Log(title = "分页查询学员-分组", businessType = BusinessType.OTHER)
    public R<IPage<StudentGroupPageResponse>> page(@RequestBody StudentGroupPageRequest params) {
        return R.ok(this.studentGroupService.page(params));
    }

    */
/**
     * 查询学员的标签(未被禁用)
     *
     * @param request 主键
     * @return 
     *//*

    @ApiOperation("查询学员未被禁用的标签")
    @PostMapping("detail")
    //@RequiresPermissions("studentGroup:details")
    //@Log(title = "查询学员标签", businessType = BusinessType.OTHER)
    public R<List<StudentGroupDetailResponse>> detail(@RequestBody IdStudentRequest request) {
        return R.ok(this.studentGroupService.detail(request));
    }

    */
/**
     * 修改学员的标签
     *
     * @param params 
     * @return 
     *//*

    @PostMapping("update")
    //@RequiresPermissions("studentGroup:update")
    //@Log(title = "修改学员标签", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated StudentGroupUpdateRequest params) {
        return R.ok(this.studentGroupService.update(params));
    }

    */
/**
     * 删除学员标签
     *
     * params request
     * @return 
     *//*

    @PostMapping("delete")
    //@RequiresPermissions("studentGroup:delete")
    //@Log(title = "删除学员-分组", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.studentGroupService.deleteByIds(request.getIdList()));
    }
    
}

*/
