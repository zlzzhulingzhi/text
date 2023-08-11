package cn.qbs.wa.teach.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.qbs.wa.teach.basic.entity.UserInfo;

/**
 * (UserInfo)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface UserInfoService extends IService<UserInfo> {

    Boolean checkFirstInfo(Long userid);
}

