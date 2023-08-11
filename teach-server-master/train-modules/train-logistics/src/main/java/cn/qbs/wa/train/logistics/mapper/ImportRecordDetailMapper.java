package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.ImportRecordDetail;
import cn.qbs.wa.train.logistics.pojo.importrecorddetail.ImportRecordDetailDetailResponse;
import cn.qbs.wa.train.logistics.pojo.importrecorddetail.ImportRecordDetailPageRequest;
import cn.qbs.wa.train.logistics.pojo.importrecorddetail.ImportRecordDetailPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 导入记录详情表(ImportRecordDetail)表数据库访问层
 *
 * @author makejava
 * @since 2022-06-22 13:48:24
 */
public interface ImportRecordDetailMapper extends BaseMapper<ImportRecordDetail> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<ImportRecordDetail> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<ImportRecordDetail> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<ImportRecordDetail> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<ImportRecordDetail> entities);

	IPage<ImportRecordDetailPageResponse> page(@Param("page") IPage<?> page, @Param("params") ImportRecordDetailPageRequest params);

	ImportRecordDetailDetailResponse selectDetailById(Serializable id);

}

