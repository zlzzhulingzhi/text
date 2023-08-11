package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.InvitationRecord;
import cn.qbs.wa.teach.organization.pojo.invitationrecord.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 受邀记录表(InvitationRecord)表服务接口
 *
 * @author makejava
 * @since 2022-06-20 19:16:49
 */
public interface InvitationRecordService extends IService<InvitationRecord> {

	/**
	 * 新增受邀记录表
	 *
	 * @param params
	 * @return
	 */
	boolean add(InvitationRecordAddRequest params);

	/**
	 * 分页查询受邀记录表
	 *
	 * @param params
	 * @return
	 */
	IPage<InvitationRecordPageResponse> page(InvitationRecordPageRequest params);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	InvitationRecordDetailResponse detail(Serializable id);

	/**
	 * 更新受邀记录表
	 *
	 * @param params
	 * @return
	 */
	boolean update(InvitationRecordUpdateRequest params);

	/**
	 * 删除受邀记录表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList);

}

