package cn.qbs.wa.teach.organization.mapper;

import java.util.List;

import cn.qbs.wa.teach.organization.pojo.invitationgroup.InvitationGroupListResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.organization.entity.Invitation;
import cn.qbs.wa.teach.organization.pojo.invitation.InvitationDetailResponse;
import cn.qbs.wa.teach.organization.pojo.invitation.InvitationPageRequest;
import cn.qbs.wa.teach.organization.pojo.invitation.InvitationPageResponse;

/**
 * 邀请表(Invitation)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-20 19:10:51
 */
public interface InvitationMapper extends BaseMapper<Invitation> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<Invitation> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<Invitation> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<Invitation> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<Invitation> entities);

	IPage<InvitationPageResponse> page(@Param("page") IPage<?> page, @Param("params") InvitationPageRequest params);

	@InterceptorIgnore(tenantLine = "true")
	InvitationDetailResponse selectDetailById(@Param("id") Long id, @Param("orgId") Long orgId, @Param("invitationKey") String invitationKey);

	List<InvitationGroupListResponse> invitationGroups(@Param("invitaionIdList") List<Long> invitaionIdList);
}

