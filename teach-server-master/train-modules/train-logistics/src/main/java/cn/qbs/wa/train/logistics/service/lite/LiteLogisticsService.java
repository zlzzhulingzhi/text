package cn.qbs.wa.train.logistics.service.lite;

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

import java.util.List;

public interface LiteLogisticsService {

    IPage<LiteMatterReportResponse>  pageMatterReport(LiteMatterReportPageRequest params);

    List<Classroom> listMatterReportClassroom(Long orgId);

    List<Organization> listMatterReportOrg();

    IPage<LiteClassroomResponse> pageClassroom(LiteClassroomPageRequest params);

    IPage<LiteClassroomResponse> pageClassroomWithDate(LiteClassroomPageRequest params);

    List<LiteClassroomScheduleResponse> listClassroomSchedule(LiteClassroomScheduleRequest params);

    IPage<LiteDormitoryResponse> pageDormitory(LiteDormitoryPageRequest params);

    List<LiteDormitoryScheduleResponse> listDormitorySchedule(LiteDormitoryScheduleRequest params);

    IPage<LiteDormitoryResponse> pageDormitoryV2(LiteDormitoryPageRequest params);

    LiteDormitoryStatResponse statDormitoryInUse(LiteDormitoryStatRequest params);

    IPage<LiteClazzResponse> pageClazz(LiteClazzPageRequest params);
    List<LiteOrganizationListResponse> listOrg();

}
