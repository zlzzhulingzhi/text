package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.ClassroomAttach;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachDetailResponse;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroomattach.ClassroomAttachPageResponse;

/**
 * 教室附件(ClassroomAttach)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-12 15:36:11
 */
public interface ClassroomAttachMapper extends BaseMapper<ClassroomAttach> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClassroomAttach> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ClassroomAttach> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClassroomAttach> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ClassroomAttach> entities);

    IPage<ClassroomAttachPageResponse> page(@Param("page") IPage<?> page, @Param("params") ClassroomAttachPageRequest params);

    ClassroomAttachDetailResponse selectDetailById(Serializable id);

}

