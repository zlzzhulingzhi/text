package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.UniApplicationUser;
import cn.qbs.wa.union.auth.mapper.UniApplicationUserMapper;
import cn.qbs.wa.union.auth.service.UniApplicationUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【系统应用-用户】(UniApplicationUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
@Slf4j
@Service("uniApplicationUserService")
public class UniApplicationUserServiceImpl extends ServiceImpl<UniApplicationUserMapper, UniApplicationUser> implements UniApplicationUserService {


}

