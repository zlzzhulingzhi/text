package cn.qbs.wa.teach.organization.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.organization.mapper.InvitationDeptMapper;
import cn.qbs.wa.teach.organization.entity.InvitationDept;
import cn.qbs.wa.teach.organization.service.InvitationDeptService;
import cn.qbs.wa.teach.organization.pojo.invitationdept.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请部门表(InvitationDept)表服务实现类
 *
 * @author makejava
 * @since 2022-06-20 19:11:30
 */
@Slf4j
@Service("invitationDeptService")
public class InvitationDeptServiceImpl extends ServiceImpl<InvitationDeptMapper, InvitationDept> implements InvitationDeptService {

	@Override
	public boolean add(InvitationDeptAddRequest params) {
		InvitationDept invitationDept = new InvitationDept();
		BeanUtils.copyProperties(params, invitationDept);
		return this.save(invitationDept);
	}

	@Override
	public IPage<InvitationDeptPageResponse> page(InvitationDeptPageRequest params) {
		return baseMapper.page(params.createMpPage(), params);
	}

	@Override
	public InvitationDeptDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(InvitationDeptUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		InvitationDept invitationDept = new InvitationDept();
		BeanUtils.copyProperties(params, invitationDept);
		return this.updateById(invitationDept);
	}

	@Override
	public boolean deleteByIds(List<Long> idList) {
		return this.removeByIds(idList);
	}

}

