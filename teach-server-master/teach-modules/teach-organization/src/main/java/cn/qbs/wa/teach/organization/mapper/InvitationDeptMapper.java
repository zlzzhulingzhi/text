package cn.qbs.wa.teach.organization.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.organization.entity.InvitationDept;
import cn.qbs.wa.teach.organization.pojo.invitationdept.InvitationDeptDetailResponse;
import cn.qbs.wa.teach.organization.pojo.invitationdept.InvitationDeptPageRequest;
import cn.qbs.wa.teach.organization.pojo.invitationdept.InvitationDeptPageResponse;

/**
 * 邀请部门表(InvitationDept)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-20 19:11:30
 */
public interface InvitationDeptMapper extends BaseMapper<InvitationDept> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<InvitationDept> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<InvitationDept> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<InvitationDept> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<InvitationDept> entities);

	IPage<InvitationDeptPageResponse> page(@Param("page") IPage<?> page, @Param("params") InvitationDeptPageRequest params);

	InvitationDeptDetailResponse selectDetailById(Serializable id);

}

