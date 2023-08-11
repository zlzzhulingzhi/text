package cn.qbs.wa.teach.exam.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.pojo.examgroup.*;
import cn.qbs.wa.teach.exam.admin.service.ExamGroupService;
import cn.qbs.wa.teach.exam.common.entity.ExamDept;
import cn.qbs.wa.teach.exam.common.entity.ExamGroup;
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
 * 考试标签表(ExamGroup)表控制层
 *
 * @author makejava
 * @since 2022-08-12 17:22:06
 */
@RestController
@RequestMapping("examGroup")
@Api(tags = "考试标签管理")
public class ExamGroupController {

	/**
	 * 服务对象
	 */
	@Resource
	private ExamGroupService examGroupService;

	/**
	 * 新增考试标签表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("batchAdd")
	@ApiOperation("批量添加")
	public R<Boolean> add(@RequestBody @Validated ExamGroupBatchAddRequest params) {
		return R.ok(this.examGroupService.add(params));
	}

	@PostMapping("addExamUserByGroup")
	@ApiOperation("批量添加")
	public R<Boolean> addExamUserByGroup(@RequestBody ExamGroupBatchAddRequest params) {
		return R.ok(this.examGroupService.addExamUserByGroup(BeanUtil.copyToList(params.getExamGroupList(), ExamGroup.class)));
	}

	@PostMapping("/listAssignGroup")
	@ApiOperation("考试指派的标签")
	public R<List<ExamGroupDetailResponse>> listAssignGroup(@RequestBody IdRequest request){
		return R.ok(this.examGroupService.listAssignGroup(request.getId()));
	}

	@PostMapping("/examByGroupIdList")
	@ApiOperation("标签下的考试")
	public R<List<ExamGroup>> examByGroupId(@RequestBody IdListRequest request){
		return R.ok(this.examGroupService.list(Wrappers.<ExamGroup>lambdaQuery().in(ExamGroup::getGroupId, request.getIdList())));
	}

	/**
	 * 分页查询考试标签表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("page")
	@ApiIgnore
	public R<IPage<ExamGroupPageResponse>> page(@RequestBody ExamGroupPageRequest params) {
		return R.ok(this.examGroupService.page(params));
	}

	/**
	 * 查询考试标签表详情
	 *
	 * @param request 主键
	 * @return
	 */
	@PostMapping("detail")
	@ApiIgnore
	public R<ExamGroupDetailResponse> detail(@RequestBody IdRequest request) {
		return R.ok(this.examGroupService.detail(request.getId()));
	}

	/**
	 * 修改考试标签表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("update")
	@ApiIgnore
	public R<Boolean> update(@RequestBody @Validated ExamGroupUpdateRequest params) {
		return R.ok(this.examGroupService.update(params));
	}

	/**
	 * 删除考试标签表
	 *
	 * @param request 主键集合
	 * @return
	 */
	@PostMapping("delete")
	@ApiOperation("删除")
	public R<Boolean> delete(@RequestBody IdListRequest request) {
		return R.ok(this.examGroupService.deleteByIds(request.getIdList()));
	}

}

