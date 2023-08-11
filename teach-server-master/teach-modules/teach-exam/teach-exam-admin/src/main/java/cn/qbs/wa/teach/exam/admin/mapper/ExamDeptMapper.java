package cn.qbs.wa.teach.exam.admin.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.exam.common.entity.ExamDept;
import cn.qbs.wa.teach.exam.admin.pojo.examdept.ExamDeptDetailResponse;
import cn.qbs.wa.teach.exam.admin.pojo.examdept.ExamDeptPageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.examdept.ExamDeptPageResponse;

/**
 * 考试部门表(ExamDept)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-12 13:57:05
 */
public interface ExamDeptMapper extends BaseMapper<ExamDept> {

	/**
	 * 批量新增数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<ExamDept> 实例对象列表
	 * @return 影响行数
	 */
	int insertBatch(@Param("entities") List<ExamDept> entities);

	/**
	 * 批量新增或按主键更新数据（MyBatis原生foreach方法）
	 *
	 * @param entities List<ExamDept> 实例对象列表
	 * @return 影响行数
	 * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
	 */
	int insertOrUpdateBatch(@Param("entities") List<ExamDept> entities);

	IPage<ExamDeptPageResponse> page(@Param("page") IPage<?> page, @Param("params") ExamDeptPageRequest params);

	ExamDeptDetailResponse selectDetailById(Serializable id);

}

