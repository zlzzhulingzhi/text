package cn.qbs.wa.teach.basic.service.inner;

import cn.qbs.wa.teach.basic.entity.User;
import cn.qbs.wa.teach.basic.pojo.user.*;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface UserInnerService extends IService<User> {

    User saveUserOrg(UserOrgAddRequest request);

}

