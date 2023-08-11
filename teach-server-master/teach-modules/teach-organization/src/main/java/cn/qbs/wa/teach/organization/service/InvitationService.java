package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.organization.entity.Invitation;
import cn.qbs.wa.teach.organization.pojo.invitation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 邀请表(Invitation)表服务接口
 *
 * @author makejava
 * @since 2022-06-20 19:10:51
 */
public interface InvitationService extends IService<Invitation> {

	/**
	 * 新增邀请表
	 *
	 * @param params
	 * @return
	 */
	boolean add(InvitationAddRequest params);

	/**
	 * 分页查询邀请表
	 *
	 * @param params
	 * @return
	 */
	IPage<InvitationPageResponse> page(InvitationPageRequest params);

	/**
	 * 启/禁用邀请设置
	 *
	 * @param idList
	 * @return
	 */
	boolean batchEnabled(List<Long> idList,Integer enabled);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	InvitationDetailResponse detail(IdOrgRequest request);

	/**
	 * 立即加入
	 *
	 * @param request
	 * @return
	 */
	Map<String, Object> joinNow(JoinNowRequest request);

	/**
	 * 更新邀请表
	 *
	 * @param params
	 * @return
	 */
	boolean update(InvitationUpdateRequest params);

	/**
	 * 删除邀请表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList);

}

