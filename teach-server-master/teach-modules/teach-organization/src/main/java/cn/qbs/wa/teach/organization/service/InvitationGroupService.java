package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.InvitationGroup;
import cn.qbs.wa.teach.organization.pojo.invitationgroup.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请分组表(InvitationGroup)表服务接口
 *
 * @author makejava
 * @since 2022-06-20 19:16:19
 */
public interface InvitationGroupService extends IService<InvitationGroup> {

	/**
	 * 新增邀请分组表
	 *
	 * @param params
	 * @return
	 */
	boolean add(InvitationGroupAddRequest params);

	/**
	 * 分页查询邀请分组表
	 *
	 * @param params
	 * @return
	 */
	IPage<InvitationGroupPageResponse> page(InvitationGroupPageRequest params);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	InvitationGroupDetailResponse detail(Serializable id);

	/**
	 * 更新邀请分组表
	 *
	 * @param params
	 * @return
	 */
	boolean update(InvitationGroupUpdateRequest params);

	/**
	 * 删除邀请分组表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList);

}

