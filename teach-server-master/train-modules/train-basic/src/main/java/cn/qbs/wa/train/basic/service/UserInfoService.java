package cn.qbs.wa.train.basic.service;

import cn.qbs.wa.train.basic.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (UserInfo)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface UserInfoService extends IService<UserInfo> {

    Boolean checkFirstInfo(Long userid);
}

