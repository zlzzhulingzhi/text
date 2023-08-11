package cn.qbs.wa.teach.organization.mapper;

import cn.qbs.wa.teach.organization.entity.Groups;
import cn.qbs.wa.teach.organization.entity.StudentGroup;
import cn.qbs.wa.teach.organization.pojo.studentgroup.StudentGroupDetailResponse;
import cn.qbs.wa.teach.organization.pojo.studentgroup.StudentGroupPageRequest;
import cn.qbs.wa.teach.organization.pojo.studentgroup.StudentGroupPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 学员-分组(StudentGroup)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-28 16:07:13
 */
public interface StudentGroupMapper extends BaseMapper<StudentGroup> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<StudentGroup> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<StudentGroup> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<StudentGroup> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<StudentGroup> entities);

    IPage<StudentGroupPageResponse> page(@Param("page") IPage<?> page, @Param("params") StudentGroupPageRequest params);

    List<StudentGroupDetailResponse> selectDetailById(Serializable id);

    int addStudentGroup(StudentGroup studentGroup);

    StudentGroupPageResponse selectGroup(@Param("params") StudentGroup params);

    List<StudentGroupDetailResponse> selectAll();


    List<Groups> listGroupByStu(Serializable id);
}

