package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.organization.pojo.invitationgroup.*;
import cn.qbs.wa.teach.organization.service.InvitationGroupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 邀请分组表(InvitationGroup)表控制层
 *
 * @author makejava
 * @since 2022-06-20 19:16:19
 */
@ApiIgnore
@RestController
@RequestMapping("invitationGroup")
public class InvitationGroupController {

	/**
	 * 服务对象
	 */
	@Resource
	private InvitationGroupService invitationGroupService;


	/**
	 * 新增邀请分组表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("add")
	//@RequiresPermissions("invitationGroup:add")
	//@Log(title = "新增邀请分组表", businessType = BusinessType.INSERT)
	public R<Boolean> add(@RequestBody @Validated InvitationGroupAddRequest params) {
		return R.ok(this.invitationGroupService.add(params));
	}

	/**
	 * 分页查询邀请分组表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("page")
	//@RequiresPermissions("invitationGroup:page")
	//@Log(title = "分页查询邀请分组表", businessType = BusinessType.OTHER)
	public R<IPage<InvitationGroupPageResponse>> page(@RequestBody InvitationGroupPageRequest params) {
		return R.ok(this.invitationGroupService.page(params));
	}

	/**
	 * 查询邀请分组表详情
	 *
	 * @param id 主键
	 * @return
	 */
	@PostMapping("detail")
	//@RequiresPermissions("invitationGroup:details")
	//@Log(title = "邀请分组表详情", businessType = BusinessType.OTHER)
	public R<InvitationGroupDetailResponse> detail(@RequestBody IdRequest request) {
		return R.ok(this.invitationGroupService.detail(request.getId()));
	}

	/**
	 * 修改邀请分组表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("update")
	//@RequiresPermissions("invitationGroup:update")
	//@Log(title = "更新邀请分组表", businessType = BusinessType.UPDATE)
	public R<Boolean> update(@RequestBody @Validated InvitationGroupUpdateRequest params) {
		return R.ok(this.invitationGroupService.update(params));
	}

	/**
	 * 删除邀请分组表
	 *
	 * @param idList 主键集合
	 * @return
	 */
	@PostMapping("delete")
	//@RequiresPermissions("invitationGroup:delete")
	//@Log(title = "删除邀请分组表", businessType = BusinessType.DELETE)
	public R<Boolean> delete(@RequestBody IdListRequest request) {
		return R.ok(this.invitationGroupService.deleteByIds(request.getIdList()));
	}

}

