package cn.qbs.wa.teach.exam.admin.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.exam.common.entity.ExamGroup;
import cn.qbs.wa.teach.exam.admin.pojo.examgroup.ExamGroupDetailResponse;
import cn.qbs.wa.teach.exam.admin.pojo.examgroup.ExamGroupPageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.examgroup.ExamGroupPageResponse;

/**
 * 考试标签表(ExamGroup)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-12 17:22:05
 */
public interface ExamGroupMapper extends BaseMapper<ExamGroup> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<ExamGroup> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<ExamGroup> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<ExamGroup> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<ExamGroup> entities);

	IPage<ExamGroupPageResponse> page(@Param("page") IPage<?> page, @Param("params") ExamGroupPageRequest params);

	ExamGroupDetailResponse selectDetailById(Serializable id);

}

