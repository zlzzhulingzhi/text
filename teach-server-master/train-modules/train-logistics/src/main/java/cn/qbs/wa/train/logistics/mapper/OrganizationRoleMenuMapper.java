package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.OrganizationRoleMenu;
import cn.qbs.wa.train.logistics.pojo.organizationrolemenu.OrganizationRoleMenuDetailResponse;
import cn.qbs.wa.train.logistics.pojo.organizationrolemenu.OrganizationRoleMenuPageRequest;
import cn.qbs.wa.train.logistics.pojo.organizationrolemenu.OrganizationRoleMenuPageResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【角色菜单关联关系】(OrganizationRoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-12 08:58:31
 */
public interface OrganizationRoleMenuMapper extends BaseMapper<OrganizationRoleMenu> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<OrganizationRoleMenu> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<OrganizationRoleMenu> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<OrganizationRoleMenu> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<OrganizationRoleMenu> entities);

    IPage<OrganizationRoleMenuPageResponse> page(@Param("page") IPage<?> page, @Param("params") OrganizationRoleMenuPageRequest params);

    OrganizationRoleMenuDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<OrganizationRoleMenu> listByAdmin(Long orgId);

    @InterceptorIgnore(tenantLine = "true")
    void deleteByAdminIds(List<Long> idList);
}

