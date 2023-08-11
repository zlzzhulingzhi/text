package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.train.logistics.entity.Student;
import cn.qbs.wa.train.logistics.pojo.student.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 学员(Student)表数据库访问层
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
public interface StudentMapper extends BaseMapper<Student> {

    @InterceptorIgnore(tenantLine = "true")
    IPage<StudentPageResponse> page(@Param("page") IPage<?> page, @Param("params") StudentPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<StudentPageResponse> pageByGroup(Page<?> page, @Param("params") StudentPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<StudentPageResponse> pageWithStaff(IPage<?> page, @Param("params") StudentPageStaffRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<StudentPageResponse> pageNoTenant(@Param("page") IPage<?> page, @Param("params") StudentPageRequest params);

    List<StudentListResponse> list(@Param("params") StudentListRequest params);

    StudentDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    StudentDetailResponse selectDetailByIdNoTenant(IdOrgRequest request);

    @InterceptorIgnore(tenantLine = "true")
    LoginInfoResponse getLoginInfo(@Param("orgId") Long orgId, @Param("account") String account);

    @InterceptorIgnore(tenantLine = "true")
    Student verifyPhone(@Param("orgId") Long orgId, @Param("phone") String phone);

    @InterceptorIgnore(tenantLine = "true")
    SocialLoginInfoResponse getPhoneBindingInfo(@Param("orgId") Long orgId, @Param("account") String account);

    @InterceptorIgnore(tenantLine = "true")
    IPage<StudentPageResponse> pageWithPure(Page<?> page, @Param("params") StudentPageStaffRequest params);

    StudentDetailResponse selectDetailsById(Serializable id);

    /**
     * 统计未选择组织的学员数量
     * */
    Long countNotDeptNum();

    @InterceptorIgnore(tenantLine = "true")
    IPage<StudentPageResponse> pageByGroupIdList(Page<?> page, StudentPageRequest params);
}

