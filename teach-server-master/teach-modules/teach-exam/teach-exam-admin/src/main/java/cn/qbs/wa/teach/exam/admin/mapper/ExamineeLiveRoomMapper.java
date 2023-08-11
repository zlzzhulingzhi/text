package cn.qbs.wa.teach.exam.admin.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom.ExamineeLiveRoomDetailResponse;
import cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom.ExamineeLiveRoomPageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom.ExamineeLiveRoomPageResponse;
import cn.qbs.wa.teach.exam.common.entity.ExamineeLiveRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 考生直播房间(ExamineeLiveRoom)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-04 11:54:09
 */
public interface ExamineeLiveRoomMapper extends BaseMapper<ExamineeLiveRoom> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<ExamineeLiveRoom> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<ExamineeLiveRoom> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<ExamineeLiveRoom> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<ExamineeLiveRoom> entities);

    List<ExamineeLiveRoomPageResponse> page( ExamineeLiveRoomPageRequest params);

    List<ExamineeLiveRoomPageResponse> selectDetailList(ExamineeLiveRoomPageRequest params);
    
}

