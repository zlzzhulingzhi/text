package cn.qbs.wa.teach.organization.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.organization.entity.Invitation;
import cn.qbs.wa.teach.organization.enums.EnableEnum;
import cn.qbs.wa.teach.organization.pojo.invitation.*;
import cn.qbs.wa.teach.organization.service.InvitationService;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 邀请表(Invitation)表控制层
 *
 * @author makejava
 * @since 2022-06-20 19:10:52
 */
@Api(tags = "邀请码管理")
@RestController
@RequestMapping("invitation")
public class InvitationController {

	/**
	 * 服务对象
	 */
	@Resource
	private InvitationService invitationService;


	/**
	 * 新增邀请表
	 *
	 * @param params
	 * @return
	 */
	@ApiOperation("新增")
	@PostMapping("add")
	public R<Boolean> add(@RequestBody @Validated InvitationAddRequest params) {
		return R.ok(this.invitationService.add(params));
	}

	/**
	 * 分页查询邀请表
	 *
	 * @param params
	 * @return
	 */
	@ApiOperation("分页")
	@PostMapping("page")
	public R<IPage<InvitationPageResponse>> page(@RequestBody InvitationPageRequest params) {
		return R.ok(this.invitationService.page(params));
	}

	/**
	 * 批量启/禁用邀请设置
	 *
	 * @param request
	 * @return
	 */
	@ApiOperation("批量启/禁用")
	@PostMapping("batchEnabled")
	public R<Boolean> batchEnabled(@RequestBody @Validated BatchEnabledRequest request) {
		return R.ok(this.invitationService.batchEnabled(request.getIdList(),request.getEnabled()));
	}

	/**
	 * 查询邀请表详情
	 *
	 * @param request 主键
	 * @return
	 */
	@ApiOperation("详情")
	@PostMapping("detail")
	public R<InvitationDetailResponse> detail(@RequestBody IdOrgRequest request) {
		return R.ok(this.invitationService.detail(request));
	}

	/**
	 * 查询邀请表基本信息，免登陆
	 *
	 * @param request 主键
	 * @return
	 */
	@ApiOperation("免登陆查看邀请基本信息")
	@PostMapping("essentialInfo")
	public R<InvitationEssentialResponse> essentialInfo(@RequestBody IdOrgRequest request) {
		LocalDateTime now = LocalDateTime.now();
		InvitationDetailResponse invitation = this.invitationService.detail(request);
		if (invitation == null || invitation.getEnabled().equals(EnableEnum.FORBIDDEN.getCode())){
			return R.ok();
		}
		if (invitation.getStartTime().isAfter(now) || invitation.getEndTime().isBefore(now)){
			return R.ok();
		}
		return R.ok(BeanUtil.copyProperties(invitation, InvitationEssentialResponse.class));
	}

	/**
	 * 立即加入
	 *
	 * @param request 主键
	 * @return
	 */
	@ApiOperation("立即加入")
	@PostMapping("joinNow")
	public R<?> joinNow(@RequestBody JoinNowRequest request) {
		return R.ok(this.invitationService.joinNow(request));
	}

	/**
	 * 修改邀请表
	 *
	 * @param params
	 * @return
	 */
	@ApiIgnore
	@PostMapping("update")
	public R<Boolean> update(@RequestBody @Validated InvitationUpdateRequest params) {
		return R.ok(this.invitationService.update(params));
	}

	/**
	 * 删除邀请表
	 *
	 * @param idList 主键集合
	 * @return
	 */
	@ApiIgnore
	@PostMapping("delete")
	public R<Boolean> delete(@RequestBody IdListRequest request) {
		return R.ok(this.invitationService.deleteByIds(request.getIdList()));
	}

}

