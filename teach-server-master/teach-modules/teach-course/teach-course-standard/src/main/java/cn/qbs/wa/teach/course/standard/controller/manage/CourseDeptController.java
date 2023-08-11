package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListParam;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.common.entity.CourseDept;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.*;
import cn.qbs.wa.teach.course.standard.service.CourseDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 课程部门表(CourseDept)表控制层
 *
 * @author makejava
 * @since 2022-08-10 19:27:56
 */
@RestController
@RequestMapping("courseDept")
@Api(tags = "课程部门管理")
public class CourseDeptController {

	/**
	 * 服务对象
	 */
	@Resource
	private CourseDeptService courseDeptService;


	/**
	 * 新增课程部门表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("batchAdd")
	@ApiOperation("批量新增")
	public R<Boolean> add(@RequestBody @Validated CourseDeptBatchAddRequest params) {
		return R.ok(this.courseDeptService.add(params));
	}

	/**
	 * 根据部门id添加课程学员
	 *
	 * @param requests
	 * @return
	 */
	@ApiIgnore
	@PostMapping("/addCourseStudentByDept")
	@ApiOperation("根据部门id添加课程学员")
	public R<Boolean> addCourseStudentByDept(@RequestBody List<CourseDeptAddRequest> requests) {
		return R.ok(this.courseDeptService.addCourseStudentByDept(BeanUtil.copyToList(requests, CourseDept.class), SecurityContextHolder.getOrgId()));
	}

	@ApiIgnore
	@PostMapping("/listByDeptId")
	@ApiOperation("根据部门id获取课程部门列表")
	public R<List<CourseDeptDetailResponse>> listByDeptId(@RequestBody IdRequest request){
		return R.ok(BeanUtil.copyToList(this.courseDeptService.list(Wrappers.<CourseDept>lambdaQuery().eq(CourseDept::getDeptId, request.getId())), CourseDeptDetailResponse.class));
	}

	@PostMapping("/listAssignDept")
	@ApiOperation("课程指派的部门")
	public R<List<CourseDeptDetailResponse>> listAssignDept(@RequestBody IdRequest request){
		return R.ok(this.courseDeptService.listAssignDept(request.getId()));
	}

	@PostMapping("/dept/count")
	@ApiOperation(value = "获取部门分类下的课程数量")
	public R<Long> deptCount(@RequestBody @Validated IdListParam param) {
		return R.ok(this.courseDeptService.deptCount(param.getIdList()));
	}

	/**
	 * 分页查询课程部门表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("page")
	@ApiOperation("部门指定的课程分页")
	public R<IPage<CoursePageResponse>> page(@RequestBody CoursePageRequest params) {
		return R.ok(this.courseDeptService.page(params));
	}

	/**
	 * 查询课程部门表详情
	 *
	 * @param id 主键
	 * @return
	 */
	@PostMapping("detail")
	@ApiIgnore
	public R<CourseDeptDetailResponse> detail(@RequestBody IdRequest request) {
		return R.ok(this.courseDeptService.detail(request.getId()));
	}

	/**
	 * 修改课程部门表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("update")
	@ApiIgnore
	public R<Boolean> update(@RequestBody @Validated CourseDeptUpdateRequest params) {
		return R.ok(this.courseDeptService.update(params));
	}

	/**
	 * 删除课程部门表
	 *
	 * @param request 主键集合
	 * @return
	 */
	@PostMapping("delete")
	@ApiOperation("删除")
	public R<Boolean> delete(@RequestBody CourseDeptDeleteRequest request) {
		return R.ok(this.courseDeptService.deleteByIds(request.getIdList(), request.getDeptId()));
	}

}

