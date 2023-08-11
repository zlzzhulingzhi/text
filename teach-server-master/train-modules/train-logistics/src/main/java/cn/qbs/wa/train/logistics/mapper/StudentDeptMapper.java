package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Dept;
import cn.qbs.wa.train.logistics.entity.StudentDept;
import cn.qbs.wa.train.logistics.pojo.studentdept.StudentDeptDetailResponse;
import cn.qbs.wa.train.logistics.pojo.studentdept.StudentDeptPageRequest;
import cn.qbs.wa.train.logistics.pojo.studentdept.StudentDeptPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 学员部门表(StudentDept)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-09 15:15:30
 */
public interface StudentDeptMapper extends BaseMapper<StudentDept> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<StudentDept> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<StudentDept> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<StudentDept> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<StudentDept> entities);

    IPage<StudentDeptPageResponse> page(@Param("page") IPage<?> page, @Param("params") StudentDeptPageRequest params);

    StudentDeptDetailResponse selectDetailById(Serializable id);

    List<Long> selectStudentIdListByDeptIdList(List<Long> deptIdList);

    List<Dept> listDeptByStu(Serializable id);
}

