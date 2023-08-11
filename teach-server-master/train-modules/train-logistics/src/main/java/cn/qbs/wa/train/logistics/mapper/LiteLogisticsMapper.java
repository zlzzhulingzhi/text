package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.pojo.classroom.LiteClassroomPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroom.LiteClassroomResponse;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.LiteClassroomScheduleRequest;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.LiteClassroomScheduleResponse;
import cn.qbs.wa.train.logistics.pojo.clazz.LiteClazzPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazz.LiteClazzResponse;
import cn.qbs.wa.train.logistics.pojo.dormitory.LiteDormitoryPageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitory.LiteDormitoryResponse;
import cn.qbs.wa.train.logistics.pojo.dormitory.LiteDormitoryStatRequest;
import cn.qbs.wa.train.logistics.pojo.dormitory.LiteDormitoryStatResponse;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.LiteDormitoryScheduleRequest;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.LiteDormitoryScheduleResponse;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportPageRequest;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportResponse;
import cn.qbs.wa.train.logistics.pojo.organization.LiteOrganizationListResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiteLogisticsMapper {

    IPage<LiteMatterReportResponse> pageMatterReport(Page<?> page, @Param("params") LiteMatterReportPageRequest params);

    IPage<LiteClassroomResponse> pageClassroom(Page<?> page, @Param("params") LiteClassroomPageRequest params);

    List<Classroom> listMatterReportClassroom(Long orgId);

    List<Organization> listMatterReportOrg();

    IPage<LiteClassroomResponse> pageClassroomWithDate(Page<LiteClassroomResponse> page, @Param("params") LiteClassroomPageRequest params);

    List<LiteClassroomScheduleResponse> listClassroomSchedule(@Param("params") LiteClassroomScheduleRequest params);

    IPage<LiteDormitoryResponse> pageDormitory(Page<LiteDormitoryResponse> page, @Param("params") LiteDormitoryPageRequest params);

    List<LiteDormitoryScheduleResponse> listDormitorySchedule(@Param("params") LiteDormitoryScheduleRequest params);

    LiteDormitoryStatResponse statDormitoryInUse(@Param("params") LiteDormitoryStatRequest params);

    IPage<LiteClazzResponse> pageClazz(Page<?> page, @Param("params") LiteClazzPageRequest params);
    List<LiteOrganizationListResponse> listOrg();

}
