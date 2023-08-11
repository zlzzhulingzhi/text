package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.InvitationDept;
import cn.qbs.wa.train.logistics.pojo.invitationdept.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请部门表(InvitationDept)表服务接口
 *
 * @author makejava
 * @since 2022-06-20 19:11:30
 */
public interface InvitationDeptService extends IService<InvitationDept> {

	/**
	 * 新增邀请部门表
	 *
	 * @param params
	 * @return
	 */
	boolean add(InvitationDeptAddRequest params);

	/**
	 * 分页查询邀请部门表
	 *
	 * @param params
	 * @return
	 */
	IPage<InvitationDeptPageResponse> page(InvitationDeptPageRequest params);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	InvitationDeptDetailResponse detail(Serializable id);

	/**
	 * 更新邀请部门表
	 *
	 * @param params
	 * @return
	 */
	boolean update(InvitationDeptUpdateRequest params);

	/**
	 * 删除邀请部门表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList);

}

