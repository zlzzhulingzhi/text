package cn.qbs.wa.teach.organization.service.impl;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.organization.entity.AppOauth;
import cn.qbs.wa.teach.organization.enums.AccountTypeEnum;
import cn.qbs.wa.teach.organization.mapper.AppOauthMapper;
import cn.qbs.wa.teach.organization.pojo.appoauth.AppOAuthUserRequest;
import cn.qbs.wa.teach.organization.pojo.student.LoginInfoResponse;
import cn.qbs.wa.teach.organization.service.AppOauthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户第三方应用登录绑定表(AppOauth)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-02 19:09:45
 */
@Slf4j
@Service("appOauthService")
public class AppOauthServiceImpl extends ServiceImpl<AppOauthMapper, AppOauth> implements AppOauthService {

    @Override
    public LoginInfoResponse getLoginInfo(AppOAuthUserRequest oAuthUserRequest) {
        String accountType = oAuthUserRequest.getAccountType();
        if (AccountTypeEnum.STUDENT.getName().equals(accountType)) {
            return this.baseMapper.getStudentLoginInfo(oAuthUserRequest);
        } else if (AccountTypeEnum.EMPLOYEE.getName().equals(accountType)) {
            return this.baseMapper.getEmployeeLoginInfo(oAuthUserRequest);
        }
        throw new ServiceException("不支持[" + accountType + "]账号类型查询");
    }
}

