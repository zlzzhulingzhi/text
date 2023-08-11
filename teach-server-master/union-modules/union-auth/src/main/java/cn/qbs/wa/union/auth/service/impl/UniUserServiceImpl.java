package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.UniUser;
import cn.qbs.wa.union.auth.mapper.UniUserMapper;
import cn.qbs.wa.union.auth.service.UniUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 统一用户表(UniUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
@Slf4j
@Service("uniUserService")
public class UniUserServiceImpl extends ServiceImpl<UniUserMapper, UniUser> implements UniUserService {



}

