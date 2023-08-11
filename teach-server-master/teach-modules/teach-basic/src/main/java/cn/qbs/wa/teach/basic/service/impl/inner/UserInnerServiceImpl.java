package cn.qbs.wa.teach.basic.service.impl.inner;

import cn.qbs.wa.teach.basic.entity.User;
import cn.qbs.wa.teach.basic.mapper.UserMapper;
import cn.qbs.wa.teach.basic.pojo.user.*;
import cn.qbs.wa.teach.basic.service.inner.UserInnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@Slf4j
@Service("userInnerService")
public class UserInnerServiceImpl extends ServiceImpl<UserMapper, User> implements UserInnerService {

    @Autowired
    UserInnerService userInnerService;

    @Override
    public User saveUserOrg(UserOrgAddRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setId(request.getUserId());
        User use=userInnerService.getById(request.getUserId());
        if(use!=null){
            updateById(user);
            return user;
        }
        save(user);
        return user;
    }
}

