package cn.qbs.wa.train.logistics.service.manage;

import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.pojo.dept.TreeDeptResponse;
import cn.qbs.wa.train.logistics.pojo.organization.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 组织机构(Organization)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
public interface OrganizationManageService extends IService<Organization> {

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    OrganizationDetailResponse detail(Serializable id);

}

