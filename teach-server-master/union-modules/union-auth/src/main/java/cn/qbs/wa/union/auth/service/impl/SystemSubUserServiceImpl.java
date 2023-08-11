package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.SystemSubUser;
import cn.qbs.wa.union.auth.mapper.SystemSubUserMapper;
import cn.qbs.wa.union.auth.service.SystemSubUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 子用户表(SystemSubUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:06
 */
@Slf4j
@Service("systemSubUserService")
public class SystemSubUserServiceImpl extends ServiceImpl<SystemSubUserMapper, SystemSubUser> implements SystemSubUserService {



}

