package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.pojo.stat.AttendClassResponse;
import cn.qbs.wa.train.logistics.pojo.stat.GroupClassroomDTO;
import cn.qbs.wa.train.logistics.pojo.stat.GroupDormitoryDTO;
import cn.qbs.wa.train.logistics.pojo.stat.GroupDormitoryFreeDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@InterceptorIgnore(tenantLine = "true")
public interface LiteStatisticMapper {

    int totalStudentNum();

    int totalOrgNum();

    List<AttendClassResponse> attendClass(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Integer totalAttendClass(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Integer totalClassStudent(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    IPage<AttendClassResponse> attendClassPage(Page<?> mpPage, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Integer countClassStudent(@Param("clazzId") Long clazzId);

    List<GroupDormitoryDTO> groupDormitoryTotal();

    List<GroupDormitoryDTO> groupDormitoryUsage(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<GroupDormitoryDTO> groupDormitoryTotalV2();

    List<GroupDormitoryFreeDTO> groupDormitoryUsageV2(@Param("day") LocalDate day);

    List<GroupClassroomDTO> groupClassroomTotal();

    List<GroupClassroomDTO> groupClassroomUsage(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
