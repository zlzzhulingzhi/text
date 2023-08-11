package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.DormitoryAttach;
import cn.qbs.wa.train.logistics.pojo.dormitoryattach.DormitoryAttachDetailResponse;
import cn.qbs.wa.train.logistics.pojo.dormitoryattach.DormitoryAttachPageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitoryattach.DormitoryAttachPageResponse;

/**
 * 宿舍附件(DormitoryAttach)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-13 09:36:14
 */
public interface DormitoryAttachMapper extends BaseMapper<DormitoryAttach> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DormitoryAttach> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DormitoryAttach> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DormitoryAttach> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<DormitoryAttach> entities);

    IPage<DormitoryAttachPageResponse> page(@Param("page") IPage<?> page, @Param("params") DormitoryAttachPageRequest params);

    DormitoryAttachDetailResponse selectDetailById(Serializable id);

}

