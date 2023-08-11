package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.domain.IdListParam;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.CourseGroup;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursegroup.*;
import cn.qbs.wa.teach.course.standard.service.CourseGroupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 课程标签表(CourseGroup)表控制层
 *
 * @author makejava
 * @since 2022-08-11 15:23:58
 */
@RestController
@RequestMapping("courseGroup")
@Api(tags = "课程标签管理")
public class CourseGroupController {

	/**
	 * 服务对象
	 */
	@Resource
	private CourseGroupService courseGroupService;


	/**
	 * 新增课程标签表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("batchAdd")
	@ApiOperation("批量新增")
	public R<Boolean> add(@RequestBody @Validated CourseGroupBatchAddRequest params) {
		return R.ok(this.courseGroupService.add(params));
	}

	/**
	 * 根据标签id添加课程学员
	 *
	 * @param requests
	 * @return
	 */
	@ApiIgnore
	@PostMapping("/addCourseStudentByGroup")
	@ApiOperation("根据标签id添加课程学员")
	public R<Boolean> addCourseStudentByGroup(@RequestBody List<CourseGroupAddRequest> requests) {
		return R.ok(this.courseGroupService.addCourseStudentByGroup(requests));
	}

	@ApiIgnore
	@PostMapping("/listByGroupId")
	@ApiOperation("根据部门id获取课程部门列表")
	public R<List<CourseGroupDetailResponse>> listByGroupId(@RequestBody IdListRequest request){
		return R.ok(BeanUtil.copyToList(this.courseGroupService.list(Wrappers.<CourseGroup>lambdaQuery().in(CourseGroup::getGroupId, request.getIdList())), CourseGroupDetailResponse.class));
	}

	@PostMapping("/listAssignGroup")
	@ApiOperation("课程指派的部门")
	public R<List<CourseGroupDetailResponse>> listAssignGroup(@RequestBody IdRequest request){
		return R.ok(this.courseGroupService.listAssignGroup(request.getId()));
	}

	@PostMapping("/group/count")
	@ApiOperation(value = "获取标签分类下的课程数量")
	public R<Long> groupCount(@RequestBody @Validated IdListParam param) {
		return R.ok(this.courseGroupService.groupCount(param.getIdList()));
	}

	/**
	 * 分页查询课程标签表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("page")
	@ApiOperation(value = "标签指派的课程分页")
	public R<IPage<CoursePageResponse>> page(@RequestBody CoursePageRequest params) {
		return R.ok(this.courseGroupService.page(params));
	}

	/**
	 * 查询课程标签表详情
	 *
	 * @param request 主键
	 * @return
	 */
	@PostMapping("detail")
	@ApiIgnore
	public R<CourseGroupDetailResponse> detail(@RequestBody IdRequest request) {
		return R.ok(this.courseGroupService.detail(request.getId()));
	}

	/**
	 * 修改课程标签表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("update")
	@ApiIgnore
	public R<Boolean> update(@RequestBody @Validated CourseGroupUpdateRequest params) {
		return R.ok(this.courseGroupService.update(params));
	}

	/**
	 * 删除课程标签表
	 *
	 * @param request 主键集合
	 * @return
	 */
	@PostMapping("delete")
	@ApiOperation("删除")
	public R<Boolean> delete(@RequestBody CourseGroupDeleteRequest request) {
		return R.ok(this.courseGroupService.deleteByIds(request.getIdList(), request.getGroupId()));
	}

}

