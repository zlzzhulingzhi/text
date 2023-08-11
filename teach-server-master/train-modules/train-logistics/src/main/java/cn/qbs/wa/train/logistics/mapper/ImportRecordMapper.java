package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.ImportRecord;
import cn.qbs.wa.train.logistics.pojo.importrecord.ImportRecordDetailResponse;
import cn.qbs.wa.train.logistics.pojo.importrecord.ImportRecordPageRequest;
import cn.qbs.wa.train.logistics.pojo.importrecord.ImportRecordPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 导入记录表(ImportRecord)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-22 13:47:47
 */
public interface ImportRecordMapper extends BaseMapper<ImportRecord> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<ImportRecord> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<ImportRecord> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<ImportRecord> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<ImportRecord> entities);

	IPage<ImportRecordPageResponse> page(@Param("page") IPage<?> page, @Param("params") ImportRecordPageRequest params);

	ImportRecordDetailResponse selectDetailById(Serializable id);

}

