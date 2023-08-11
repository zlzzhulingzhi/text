package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomPriceResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.qbs.wa.train.logistics.entity.ClassroomValuation;

/**
 * 教室设备-计价项目(ClassroomValuation)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-24 10:52:43
 */
public interface ClassroomValuationMapper extends BaseMapper<ClassroomValuation> {

    ClassroomPriceResponse getClassroomPrice();

}

