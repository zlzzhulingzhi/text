package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.entity.InvitationRecord;
import cn.qbs.wa.train.logistics.mapper.InvitationRecordMapper;
import cn.qbs.wa.train.logistics.pojo.invitationrecord.*;
import cn.qbs.wa.train.logistics.service.InvitationRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 受邀记录表(InvitationRecord)表服务实现类
 *
 * @author makejava
 * @since 2022-06-20 19:16:49
 */
@Slf4j
@Service("invitationRecordService")
public class InvitationRecordServiceImpl extends ServiceImpl<InvitationRecordMapper, InvitationRecord> implements InvitationRecordService {

	@Override
	public boolean add(InvitationRecordAddRequest params) {
		InvitationRecord invitationRecord = new InvitationRecord();
		BeanUtils.copyProperties(params, invitationRecord);
		return this.save(invitationRecord);
	}

	@Override
	public IPage<InvitationRecordPageResponse> page(InvitationRecordPageRequest params) {
		return baseMapper.page(params.createMpPage(), params);
	}

	@Override
	public InvitationRecordDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(InvitationRecordUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		InvitationRecord invitationRecord = new InvitationRecord();
		BeanUtils.copyProperties(params, invitationRecord);
		return this.updateById(invitationRecord);
	}

	@Override
	public boolean deleteByIds(List<Long> idList) {
		return this.removeByIds(idList);
	}

}

