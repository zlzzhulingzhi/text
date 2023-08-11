package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemUser;
import cn.qbs.wa.union.admin.pojo.systemuser.InnerSystemUserAddRequest;
import com.baomidou.mybatisplus.extension.service.IService;

public interface InnerSystemUserService extends IService<SystemUser> {

    SystemUser innerAdd(InnerSystemUserAddRequest params);
}
