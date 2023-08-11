package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.organization.pojo.invitationdept.*;
import cn.qbs.wa.teach.organization.service.InvitationDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 邀请部门表(InvitationDept)表控制层
 *
 * @author makejava
 * @since 2022-06-20 19:11:31
 */
@ApiIgnore
@RestController
@RequestMapping("invitationDept")
public class InvitationDeptController {

	/**
	 * 服务对象
	 */
	@Resource
	private InvitationDeptService invitationDeptService;


	/**
	 * 新增邀请部门表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("add")
	//@RequiresPermissions("invitationDept:add")
	//@Log(title = "新增邀请部门表", businessType = BusinessType.INSERT)
	public R<Boolean> add(@RequestBody @Validated InvitationDeptAddRequest params) {
		return R.ok(this.invitationDeptService.add(params));
	}

	/**
	 * 分页查询邀请部门表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("page")
	//@RequiresPermissions("invitationDept:page")
	//@Log(title = "分页查询邀请部门表", businessType = BusinessType.OTHER)
	public R<IPage<InvitationDeptPageResponse>> page(@RequestBody InvitationDeptPageRequest params) {
		return R.ok(this.invitationDeptService.page(params));
	}

	/**
	 * 查询邀请部门表详情
	 *
	 * @param id 主键
	 * @return
	 */
	@PostMapping("detail")
	//@RequiresPermissions("invitationDept:details")
	//@Log(title = "邀请部门表详情", businessType = BusinessType.OTHER)
	public R<InvitationDeptDetailResponse> detail(@RequestBody IdRequest request) {
		return R.ok(this.invitationDeptService.detail(request.getId()));
	}

	/**
	 * 修改邀请部门表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("update")
	//@RequiresPermissions("invitationDept:update")
	//@Log(title = "更新邀请部门表", businessType = BusinessType.UPDATE)
	public R<Boolean> update(@RequestBody @Validated InvitationDeptUpdateRequest params) {
		return R.ok(this.invitationDeptService.update(params));
	}

	/**
	 * 删除邀请部门表
	 *
	 * @param idList 主键集合
	 * @return
	 */
	@PostMapping("delete")
	//@RequiresPermissions("invitationDept:delete")
	//@Log(title = "删除邀请部门表", businessType = BusinessType.DELETE)
	public R<Boolean> delete(@RequestBody IdListRequest request) {
		return R.ok(this.invitationDeptService.deleteByIds(request.getIdList()));
	}

}

