package cn.qbs.wa.train.logistics.service.impl.lite;

import cn.qbs.wa.train.logistics.entity.*;
import cn.qbs.wa.train.logistics.mapper.ClassroomScheduleMapper;
import cn.qbs.wa.train.logistics.mapper.ClazzStudentMapper;
import cn.qbs.wa.train.logistics.mapper.DormitoryScheduleMapper;
import cn.qbs.wa.train.logistics.mapper.LiteLogisticsMapper;
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
import cn.qbs.wa.train.logistics.service.lite.LiteLogisticsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("liteLogisticsService")
public class LiteLogisticsServiceImpl implements LiteLogisticsService {

    @Resource
    private LiteLogisticsMapper liteLogisticsMapper;

    @Resource
    private ClassroomScheduleMapper classroomScheduleMapper;

    @Resource
    private DormitoryScheduleMapper dormitoryScheduleMapper;

    @Resource
    private ClazzStudentMapper clazzStudentMapper;

    @Override
    public IPage<LiteMatterReportResponse> pageMatterReport(LiteMatterReportPageRequest params) {
        return this.liteLogisticsMapper.pageMatterReport(params.createMpPage(), params);
    }

    @Override
    public List<Classroom> listMatterReportClassroom(Long orgId) {
        return liteLogisticsMapper.listMatterReportClassroom(orgId);
    }

    @Override
    public IPage<LiteClassroomResponse> pageClassroom(LiteClassroomPageRequest params) {
        // 根据条件分页查询教室
        return liteLogisticsMapper.pageClassroom(params.createMpPage(), params);
    }

    @Override
    public List<Organization> listMatterReportOrg() {
        return liteLogisticsMapper.listMatterReportOrg();
    }

    @Override
    public IPage<LiteClassroomResponse> pageClassroomWithDate(LiteClassroomPageRequest params) {
        // 根据条件分页查询教室
        Page<LiteClassroomResponse> page = params.createMpPage();
        liteLogisticsMapper.pageClassroomWithDate(page, params);
        List<LiteClassroomResponse> records = page.getRecords();
        for (LiteClassroomResponse record : records) {
            // 查询后续预定情况
            LambdaQueryWrapper<ClassroomSchedule> queryWrapper = Wrappers.<ClassroomSchedule>lambdaQuery()
                    .eq(ClassroomSchedule::getClassroomId, record.getClassroomId())
                    .between(ClassroomSchedule::getUseDate, params.getUseDate().plusDays(1), params.getUseDate().plusMonths(1));
            Long count = classroomScheduleMapper.selectCount(queryWrapper);
            if (count != null && count > 0L) {
                record.setScheduleAfter(1);
            } else {
                record.setScheduleAfter(0);
            }
        }
        return page;
    }

    @Override
    public List<LiteClassroomScheduleResponse> listClassroomSchedule(LiteClassroomScheduleRequest params) {
        return liteLogisticsMapper.listClassroomSchedule(params);
    }

    @Override
    public IPage<LiteDormitoryResponse> pageDormitory(LiteDormitoryPageRequest params) {
        // 根据条件分页查询教室
        Page<LiteDormitoryResponse> page = params.createMpPage();
        liteLogisticsMapper.pageDormitory(page, params);
        List<LiteDormitoryResponse> records = page.getRecords();
        for (LiteDormitoryResponse record : records) {
            // 查询后续预定情况
            LambdaQueryWrapper<DormitorySchedule> queryWrapper = Wrappers.<DormitorySchedule>lambdaQuery()
                    .eq(DormitorySchedule::getDormitoryId, record.getDormitoryId())
                    .between(DormitorySchedule::getUseDate, params.getUseDate().plusDays(1), params.getUseDate().plusMonths(1));
            Long count = dormitoryScheduleMapper.selectCount(queryWrapper);
            if (count != null && count > 0L) {
                record.setScheduleAfter(1);
            } else {
                record.setScheduleAfter(0);
            }
        }
        return page;
    }

    @Override
    public List<LiteDormitoryScheduleResponse> listDormitorySchedule(LiteDormitoryScheduleRequest params) {
        return liteLogisticsMapper.listDormitorySchedule(params);
    }

    @Override
    public IPage<LiteDormitoryResponse> pageDormitoryV2(LiteDormitoryPageRequest params) {
        // 根据条件分页查询宿舍
        Page<LiteDormitoryResponse> page = params.createMpPage();
        liteLogisticsMapper.pageDormitory(page, params);
        return page;
    }

    @Override
    public LiteDormitoryStatResponse statDormitoryInUse(LiteDormitoryStatRequest params) {
        return liteLogisticsMapper.statDormitoryInUse(params);
    }

    @Override
    public IPage<LiteClazzResponse> pageClazz(LiteClazzPageRequest params) {
        IPage<LiteClazzResponse> page = liteLogisticsMapper.pageClazz(params.createMpPage(), params);
        // 分别查询班级下的学员人数
        for (LiteClazzResponse record : page.getRecords()) {
            Long count = clazzStudentMapper.selectCount(Wrappers.<ClazzStudent>lambdaQuery().eq(ClazzStudent::getClazzId, record.getId()));
            record.setStudentNum(Math.toIntExact(count));
        }
        return page;
    }

    @Override
    public List<LiteOrganizationListResponse> listOrg() {
        return liteLogisticsMapper.listOrg();
    }
}
