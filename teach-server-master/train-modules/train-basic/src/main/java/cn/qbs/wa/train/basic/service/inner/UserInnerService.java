package cn.qbs.wa.train.basic.service.inner;

import cn.qbs.wa.train.basic.entity.User;
import cn.qbs.wa.train.basic.pojo.user.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface UserInnerService extends IService<User> {

    User saveUserOrg(UserOrgAddRequest request);

    User updateUserOrg(UserOrgUpdateRequest request);

}

