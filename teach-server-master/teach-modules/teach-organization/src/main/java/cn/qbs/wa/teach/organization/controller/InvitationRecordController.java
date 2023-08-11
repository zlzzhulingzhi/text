package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.organization.pojo.invitationrecord.*;
import cn.qbs.wa.teach.organization.service.InvitationRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 受邀记录表(InvitationRecord)表控制层
 *
 * @author makejava
 * @since 2022-06-20 19:16:49
 */
@ApiIgnore
@RestController
@RequestMapping("invitationRecord")
public class InvitationRecordController {

	/**
	 * 服务对象
	 */
	@Resource
	private InvitationRecordService invitationRecordService;


	/**
	 * 新增受邀记录表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("add")
	//@RequiresPermissions("invitationRecord:add")
	//@Log(title = "新增受邀记录表", businessType = BusinessType.INSERT)
	public R<Boolean> add(@RequestBody @Validated InvitationRecordAddRequest params) {
		return R.ok(this.invitationRecordService.add(params));
	}

	/**
	 * 分页查询受邀记录表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("page")
	//@RequiresPermissions("invitationRecord:page")
	//@Log(title = "分页查询受邀记录表", businessType = BusinessType.OTHER)
	public R<IPage<InvitationRecordPageResponse>> page(@RequestBody InvitationRecordPageRequest params) {
		return R.ok(this.invitationRecordService.page(params));
	}

	/**
	 * 查询受邀记录表详情
	 *
	 * @param id 主键
	 * @return
	 */
	@PostMapping("detail")
	//@RequiresPermissions("invitationRecord:details")
	//@Log(title = "受邀记录表详情", businessType = BusinessType.OTHER)
	public R<InvitationRecordDetailResponse> detail(@RequestBody IdRequest request) {
		return R.ok(this.invitationRecordService.detail(request.getId()));
	}

	/**
	 * 修改受邀记录表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("update")
	//@RequiresPermissions("invitationRecord:update")
	//@Log(title = "更新受邀记录表", businessType = BusinessType.UPDATE)
	public R<Boolean> update(@RequestBody @Validated InvitationRecordUpdateRequest params) {
		return R.ok(this.invitationRecordService.update(params));
	}

	/**
	 * 删除受邀记录表
	 *
	 * @param idList 主键集合
	 * @return
	 */
	@PostMapping("delete")
	//@RequiresPermissions("invitationRecord:delete")
	//@Log(title = "删除受邀记录表", businessType = BusinessType.DELETE)
	public R<Boolean> delete(@RequestBody IdListRequest request) {
		return R.ok(this.invitationRecordService.deleteByIds(request.getIdList()));
	}

}

