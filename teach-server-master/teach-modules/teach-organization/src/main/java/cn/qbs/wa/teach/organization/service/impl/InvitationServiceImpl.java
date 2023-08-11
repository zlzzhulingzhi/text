package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.LineIter;
import cn.hutool.core.util.IdUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.service.TokenService;
import cn.qbs.wa.teach.organization.entity.Groups;
import cn.qbs.wa.teach.organization.entity.InvitationGroup;
import cn.qbs.wa.teach.organization.entity.Student;
import cn.qbs.wa.teach.organization.enums.BusinessTypeEnum;
import cn.qbs.wa.teach.organization.enums.EnableEnum;
import cn.qbs.wa.teach.organization.mapper.InvitationMapper;
import cn.qbs.wa.teach.organization.entity.Invitation;
import cn.qbs.wa.teach.organization.mapper.StudentMapper;
import cn.qbs.wa.teach.organization.pojo.invitationdept.InvitationDeptAddRequest;
import cn.qbs.wa.teach.organization.pojo.invitationgroup.InvitationGroupListResponse;
import cn.qbs.wa.teach.organization.pojo.invitationrecord.InvitationRecordAddRequest;
import cn.qbs.wa.teach.organization.pojo.student.LoginInfoRequest;
import cn.qbs.wa.teach.organization.pojo.student.StudentAddRequest;
import cn.qbs.wa.teach.organization.service.*;
import cn.qbs.wa.teach.organization.pojo.invitation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 邀请表(Invitation)表服务实现类
 *
 * @author makejava
 * @since 2022-06-20 19:10:52
 */
@Slf4j
@Service("invitationService")
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {

	@Resource
	private InvitationDeptService invDeptService;

	@Resource
	private InvitationGroupService invGroupService;

	@Resource
	private OrganizationService orgService;

	@Resource
	private StudentService studentService;

	@Resource
	private StudentMapper studentMapper;

	@Resource
	private InvitationRecordService recordService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private TokenService tokenService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean add(InvitationAddRequest params) {
		if (params.getDeptId() == null && CollectionUtil.isEmpty(params.getGroupId())){
			throw new ServiceException("组织和学员标签至少添加一项");
		}
		if (params.getOrgId() == null){
			params.setOrgId(SecurityContextHolder.getOrgId());
		}
		//新增邀请表
		Invitation invitation = new Invitation();
		params.setInvitationKey(IdUtil.getSnowflake().nextIdStr());
		BeanUtils.copyProperties(params, invitation);
		this.save(invitation);
		//如果组织不为空，新增邀请-组织关系表
		if (params.getDeptId() != null){
			InvitationDeptAddRequest deptAdd = new InvitationDeptAddRequest();
			BeanUtils.copyProperties(params, deptAdd);
			deptAdd.setInvitationId(invitation.getId());
			invDeptService.add(deptAdd);
		}
		//如果学员标签不为空，新增邀请-学员标签表
		if (CollectionUtil.isNotEmpty(params.getGroupId())){
			List<InvitationGroup> groupList = params.getGroupId().stream().map(i -> {
				InvitationGroup group = new InvitationGroup();
				group.setGroupId(i);
				return group;
			}).collect(Collectors.toList());
			groupList.forEach(i -> {
				i.setInvitationId(invitation.getId());
				i.setOrgId(params.getOrgId());
			});
			invGroupService.saveBatch(groupList);
		}
		return true;
	}

	@Override
	public IPage<InvitationPageResponse> page(InvitationPageRequest params) {
		IPage<InvitationPageResponse> invitationList = baseMapper.page(params.createMpPage(), params);
		if (CollectionUtils.isEmpty(invitationList.getRecords())){
			return invitationList;
		}
		List<Long> invitationIdList = invitationList.getRecords().stream().map(InvitationPageResponse::getId).collect(Collectors.toList());
		List<InvitationGroupListResponse> groupList = baseMapper.invitationGroups(invitationIdList);
		if (CollectionUtils.isEmpty(groupList)){
			return invitationList;
		}
		invitationList.getRecords().forEach(i -> {
			List<InvitationGroupResponse> invGroupList = new ArrayList<>();
			groupList.forEach(g -> {
				InvitationGroupResponse group  = new InvitationGroupResponse();
				if (i.getId().equals(g.getInvitaionId())){
					BeanUtils.copyProperties(g, group);
					invGroupList.add(group);
				}
			});
			i.setGroupList(invGroupList);
		});
		return invitationList;
	}

	@Override
	public InvitationDetailResponse detail(IdOrgRequest request) {
		return baseMapper.selectDetailById(request.getId(), request.getOrgId(),null);
	}

	@Override
	public boolean update(InvitationUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		Invitation invitation = new Invitation();
		BeanUtils.copyProperties(params, invitation);
		return this.updateById(invitation);
	}

	@Override
	public boolean deleteByIds(List<Long> idList) {
		return this.removeByIds(idList);
	}

	@Override
	public boolean batchEnabled(List<Long> idList,Integer enabled) {
		List<Invitation> invitationList = idList.stream().map(i -> {
			Invitation invitation = new Invitation();
			invitation.setId(i);
			invitation.setEnabled(enabled);
			return invitation;
		}).collect(Collectors.toList());
		return this.updateBatchById(invitationList);
	}

	private void checkCode(String account, String cachePrefix, String code) {
		Object cacheCode = redisService.getCacheObject(cachePrefix + ":" + null + ":" + account);
		if (cacheCode == null) {
			throw new ServiceException("验证码已过期请重新再试");
		}
		String c = cacheCode.toString();
		if (!c.equals(code)) {
			throw new ServiceException("验证码错误,请重新输入");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> joinNow(JoinNowRequest request) {
		//验证短信
		checkCode(request.getPhone(), CacheConstants.REGISTER_PHONE_CODE, request.getSms());
		//查询邀请码
		InvitationDetailResponse detailResponse = this.baseMapper.selectDetailById(null,null,request.getInvitationKey());
		if (detailResponse == null || detailResponse.getEnabled().equals(EnableEnum.FORBIDDEN.getCode())){
			throw new ServiceException("邀请码不存在或已禁用！");
		}
		LocalDateTime now = LocalDateTime.now();
		if (detailResponse.getStartTime().isAfter(now) || detailResponse.getEndTime().isBefore(now)){
			throw new ServiceException("未在有效使用时间内！");
		}
		LoginUserAndPhoneIsExist loginUserAndPhoneIsExist = new LoginUserAndPhoneIsExist();
		LoginUser loginUser;
		//查询手机号是否已注册
		Student student = studentMapper.verifyPhone(detailResponse.getOrgId(), AesUtil.encode(request.getPhone()));
		//如果已注册直接登录
		if (student != null){
			LoginInfoRequest loginInfoRequest = new LoginInfoRequest();
			loginInfoRequest.setOrgId(detailResponse.getOrgId());
			loginInfoRequest.setAccount(AesUtil.decode(student.getAccount()));
			loginUser = studentService.getLoginInfo(loginInfoRequest);
			BeanUtils.copyProperties(loginUser, loginUserAndPhoneIsExist);
			loginUserAndPhoneIsExist.setPhoneIsExist(true);
			Map<String, Object> loginStudent = tokenService.createTokenForStudent(loginUser);
			loginStudent.put("phoneIsExist", true);
			return loginStudent;
		}
		//否则先注册账号
		StudentAddRequest studentAddRequest = new StudentAddRequest();
		BeanUtils.copyProperties(detailResponse, studentAddRequest);
		studentAddRequest.setAccount(request.getPhone());
		studentAddRequest.setPhone(request.getPhone());
		studentAddRequest.setRealName("学员" + request.getPhone().substring(7));
		List<Long> groupIdList = detailResponse.getGroupList().stream().map(InvitationGroupResponse::getGroupId).collect(Collectors.toList());
		studentAddRequest.setGroupIdList(groupIdList);

		SecurityContextHolder.setSelectOrgId(detailResponse.getOrgId().toString());
		SecurityContextHolder.setOrgId(detailResponse.getOrgId().toString());

		loginUser = studentService.add(studentAddRequest);
		BeanUtils.copyProperties(loginUser, loginUserAndPhoneIsExist);
		//新增邀请记录
		InvitationRecordAddRequest recordAddRequest = new InvitationRecordAddRequest();
		recordAddRequest.setBusinessType(request.getBusinessType() != null ? request.getBusinessType() : BusinessTypeEnum.STUDENT.getCode());
		recordAddRequest.setOrgId(detailResponse.getOrgId());
		recordAddRequest.setInvitationId(detailResponse.getId());
		recordAddRequest.setInviteBy(detailResponse.getCreateBy());
		recordAddRequest.setBusinessId(loginUser.getStudentId());
		recordService.add(recordAddRequest);
		Map<String, Object> loginStudent = tokenService.createTokenForStudent(loginUser);
		loginStudent.put("phoneIsExist", false);
		return loginStudent;
	}
}

