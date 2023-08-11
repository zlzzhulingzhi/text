package cn.qbs.wa.teach.basic.service.impl;

import cn.qbs.wa.teach.basic.entity.UserInfo;
import cn.qbs.wa.teach.basic.mapper.UserInfoMapper;
import cn.qbs.wa.teach.basic.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (UserInfo)表服务实现类
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@Slf4j
@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Override
    public Boolean checkFirstInfo(Long userId) {
        UserInfo userInfo = getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            UserInfo userInfoAdd = new UserInfo();
            userInfoAdd.setUserId(userId);
            save(userInfoAdd);
            return true;
        }
        return false;
    }
}

