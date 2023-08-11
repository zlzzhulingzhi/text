package cn.qbs.wa.teach.exam.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.pojo.examdept.*;
import cn.qbs.wa.teach.exam.admin.pojo.examgroup.ExamGroupBatchAddRequest;
import cn.qbs.wa.teach.exam.admin.service.ExamDeptService;
import cn.qbs.wa.teach.exam.common.entity.ExamDept;
import cn.qbs.wa.teach.exam.common.entity.ExamGroup;
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
 * 考试部门表(ExamDept)表控制层
 *
 * @author makejava
 * @since 2022-08-12 13:57:06
 */
@RestController
@RequestMapping("examDept")
@Api(tags = "考试部门管理")
public class ExamDeptController {

	/**
	 * 服务对象
	 */
	@Resource
	private ExamDeptService examDeptService;


	/**
	 * 新增考试部门表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("batchAdd")
	@ApiOperation("批量新增")
	public R<Boolean> add(@RequestBody @Validated ExamDeptBatchAddRequest params) {
		return R.ok(this.examDeptService.add(params));
	}

	@PostMapping("addExamUserByDept")
	@ApiOperation("批量添加")
	public R<Boolean> addExamUserByDept(@RequestBody ExamDeptBatchAddRequest params) {
		return R.ok(this.examDeptService.addExamUserByDept(BeanUtil.copyToList(params.getExamDeptList(), ExamDept.class)));
	}

	/**
	 * 分页查询考试部门表
	 *
	 * @param params
	 * @return
	 */
	@ApiIgnore
	@PostMapping("page")
	@ApiOperation("部门下指派的考试分页")
	public R<IPage<ExamDeptPageResponse>> page(@RequestBody ExamDeptPageRequest params) {
		return R.ok(this.examDeptService.page(params));
	}

	@PostMapping("/listAssignDept")
	@ApiOperation("考试指派的部门")
	public R<List<ExamDeptDetailResponse>> listAssignDept(@RequestBody IdRequest request){
		return R.ok(this.examDeptService.listAssignDept(request.getId()));
	}

	@PostMapping("/examByDeptId")
	@ApiOperation("部门下的考试")
	public R<List<ExamDept>> examByDeptId(@RequestBody IdRequest request){
		return R.ok(this.examDeptService.list(Wrappers.<ExamDept>lambdaQuery().eq(ExamDept::getDeptId, request.getId())));
	}

	/**
	 * 查询考试部门表详情
	 *
	 * @param request 主键
	 * @return
	 */
	@PostMapping("detail")
	@ApiIgnore
	public R<ExamDeptDetailResponse> detail(@RequestBody IdRequest request) {
		return R.ok(this.examDeptService.detail(request.getId()));
	}

	/**
	 * 修改考试部门表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("update")
	@ApiIgnore
	public R<Boolean> update(@RequestBody @Validated ExamDeptUpdateRequest params) {
		return R.ok(this.examDeptService.update(params));
	}

	/**
	 * 删除考试部门表
	 *
	 * @param request 主键集合
	 * @return
	 */
	@PostMapping("delete")
	@ApiOperation("删除")
	public R<Boolean> delete(@RequestBody IdListRequest request) {
		return R.ok(this.examDeptService.deleteByIds(request.getIdList()));
	}

}

