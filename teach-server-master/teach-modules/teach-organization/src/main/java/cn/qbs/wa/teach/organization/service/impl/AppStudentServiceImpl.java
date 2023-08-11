package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.organization.entity.AppOauth;
import cn.qbs.wa.teach.organization.entity.Student;
import cn.qbs.wa.teach.organization.enums.AccountTypeEnum;
import cn.qbs.wa.teach.organization.enums.SocialPlatformEnum;
import cn.qbs.wa.teach.organization.pojo.student.app.AppSocialBindingRequest;
import cn.qbs.wa.teach.organization.service.AppOauthService;
import cn.qbs.wa.teach.organization.service.AppStudentService;
import cn.qbs.wa.teach.organization.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AppStudentServiceImpl implements AppStudentService {

    @Resource
    private AppOauthService appOauthService;

    @Resource
    private StudentService studentService;

    @Override
    public Boolean bindingSocial(AppSocialBindingRequest request) {
        String uid = request.getUnionId();
        Long count = appOauthService.lambdaQuery().eq(AppOauth::getUid, uid).count();
        if (count != null && count > 0) {
            throw new ServiceException("该微信已绑定其他学员账号");
        }
        Long userId = SecurityContextHolder.getUserId();
        AppOauth one = appOauthService.lambdaQuery().eq(AppOauth::getUserId, userId).eq(AppOauth::getPlatform, SocialPlatformEnum.WEIXIN.getName()).one();
        AppOauth appOauth = new AppOauth();
        if (one != null) {
            if (uid.equals(one.getUid())) {
                return Boolean.TRUE;
            }
            appOauth.setId(one.getId());
        } else {
            appOauth.setUserId(userId);
            appOauth.setAccountType(AccountTypeEnum.STUDENT.getName());
            appOauth.setPlatform(SocialPlatformEnum.WEIXIN.getName());
        }
        appOauth.setUid(uid);
        appOauthService.saveOrUpdate(appOauth);
        if (!StrUtil.isAllBlank(request.getHeadImgUrl(), request.getNickName(), request.getRealName())) {
            // 更新学员信息
            Student student = new Student();
            student.setId(SecurityContextHolder.getStudentId());
            student.setHeadImgUrl(StrUtil.trimToNull(request.getHeadImgUrl()));
            student.setRealName(StrUtil.trimToNull(request.getRealName()));
            student.setNickName(StrUtil.trimToNull(request.getNickName()));
            student.setSex(request.getSex());
            studentService.updateById(student);
        }
        return Boolean.TRUE;
    }
}
