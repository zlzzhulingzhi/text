package cn.qbs.wa.teach.organization.mapper;

import cn.qbs.wa.teach.organization.entity.OrganizationMenu;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.OrganizationMenuDetailResponse;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.OrganizationMenuPageRequest;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.OrganizationMenuPageResponse;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.OrganizationMenuVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【角色菜单关联关系】(OrganizationMenu)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-09 20:26:18
 */
public interface OrganizationMenuMapper extends BaseMapper<OrganizationMenu> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<OrganizationMenu> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<OrganizationMenu> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<OrganizationMenu> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<OrganizationMenu> entities);

    IPage<OrganizationMenuPageResponse> page(@Param("page") IPage<?> page, @Param("params") OrganizationMenuPageRequest params);

    OrganizationMenuDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<OrganizationMenuVO> getMenuByRoleIdList(List<Long> roleIdList);

    @InterceptorIgnore(tenantLine = "true")
    List<OrganizationMenu> adminListByOrgId(Serializable id);
}

