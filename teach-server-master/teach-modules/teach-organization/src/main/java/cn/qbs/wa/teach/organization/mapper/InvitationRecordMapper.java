package cn.qbs.wa.teach.organization.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.organization.entity.InvitationRecord;
import cn.qbs.wa.teach.organization.pojo.invitationrecord.InvitationRecordDetailResponse;
import cn.qbs.wa.teach.organization.pojo.invitationrecord.InvitationRecordPageRequest;
import cn.qbs.wa.teach.organization.pojo.invitationrecord.InvitationRecordPageResponse;

/**
 * 受邀记录表(InvitationRecord)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-20 19:16:49
 */
public interface InvitationRecordMapper extends BaseMapper<InvitationRecord> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<InvitationRecord> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<InvitationRecord> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<InvitationRecord> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<InvitationRecord> entities);

	IPage<InvitationRecordPageResponse> page(@Param("page") IPage<?> page, @Param("params") InvitationRecordPageRequest params);

	InvitationRecordDetailResponse selectDetailById(Serializable id);

}

