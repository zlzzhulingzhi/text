package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.SystemUser;
import cn.qbs.wa.union.auth.mapper.SystemUserMapper;
import cn.qbs.wa.union.auth.service.SystemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 平台系统子用户表(SystemUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:07
 */
@Slf4j
@Service("systemUserService")
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService {

}

