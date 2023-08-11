package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.InvitationGroup;
import cn.qbs.wa.train.logistics.pojo.invitationgroup.InvitationGroupDetailResponse;
import cn.qbs.wa.train.logistics.pojo.invitationgroup.InvitationGroupPageRequest;
import cn.qbs.wa.train.logistics.pojo.invitationgroup.InvitationGroupPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请分组表(InvitationGroup)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-20 19:16:18
 */
public interface InvitationGroupMapper extends BaseMapper<InvitationGroup> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<InvitationGroup> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<InvitationGroup> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<InvitationGroup> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<InvitationGroup> entities);

	IPage<InvitationGroupPageResponse> page(@Param("page") IPage<?> page, @Param("params") InvitationGroupPageRequest params);

	InvitationGroupDetailResponse selectDetailById(Serializable id);

}

