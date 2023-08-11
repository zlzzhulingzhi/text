package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.SystemEmployeeUser;
import cn.qbs.wa.union.auth.mapper.SystemEmployeeUserMapper;
import cn.qbs.wa.union.auth.service.SystemEmployeeUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 职工用户表(SystemEmployeeUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:03
 */
@Slf4j
@Service("systemEmployeeUserService")
public class SystemEmployeeUserServiceImpl extends ServiceImpl<SystemEmployeeUserMapper, SystemEmployeeUser> implements SystemEmployeeUserService {

}

