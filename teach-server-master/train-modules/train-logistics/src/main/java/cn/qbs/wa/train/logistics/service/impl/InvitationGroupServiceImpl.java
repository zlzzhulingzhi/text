package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.entity.InvitationGroup;
import cn.qbs.wa.train.logistics.mapper.InvitationGroupMapper;
import cn.qbs.wa.train.logistics.pojo.invitationgroup.*;
import cn.qbs.wa.train.logistics.service.InvitationGroupService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请分组表(InvitationGroup)表服务实现类
 *
 * @author makejava
 * @since 2022-06-20 19:16:19
 */
@Slf4j
@Service("invitationGroupService")
public class InvitationGroupServiceImpl extends ServiceImpl<InvitationGroupMapper, InvitationGroup> implements InvitationGroupService {

	@Override
	public boolean add(InvitationGroupAddRequest params) {
		InvitationGroup invitationGroup = new InvitationGroup();
		BeanUtils.copyProperties(params, invitationGroup);
		return this.save(invitationGroup);
	}

	@Override
	public IPage<InvitationGroupPageResponse> page(InvitationGroupPageRequest params) {
		return baseMapper.page(params.createMpPage(), params);
	}

	@Override
	public InvitationGroupDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(InvitationGroupUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		InvitationGroup invitationGroup = new InvitationGroup();
		BeanUtils.copyProperties(params, invitationGroup);
		return this.updateById(invitationGroup);
	}

	@Override
	public boolean deleteByIds(List<Long> idList) {
		return this.removeByIds(idList);
	}

}

