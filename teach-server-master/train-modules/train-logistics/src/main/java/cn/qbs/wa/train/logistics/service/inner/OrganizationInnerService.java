package cn.qbs.wa.train.logistics.service.inner;

import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.pojo.organization.*;
import cn.qbs.wa.train.logistics.pojo.organization.inner.OrganizationInnerAddRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;


/**
 * 组织机构(Organization)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
public interface OrganizationInnerService extends IService<Organization> {
    /**
     * 培训中心新增机构后教务中心同步增加
     */
    boolean init(OrganizationInnerAddRequest params);

    boolean adminUpdate(OrganizationUpdateRequest params);

    boolean update(OrganizationUpdateRequest params);

}

