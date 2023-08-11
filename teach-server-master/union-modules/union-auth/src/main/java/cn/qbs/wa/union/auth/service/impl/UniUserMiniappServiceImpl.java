package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.UniUserMiniapp;
import cn.qbs.wa.union.auth.mapper.UniUserMiniappMapper;
import cn.qbs.wa.union.auth.service.UniUserMiniappService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 统一用户绑定小程序(UniUserMiniapp)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-21 08:35:17
 */
@Slf4j
@Service("uniUserMiniappService")
public class UniUserMiniappServiceImpl extends ServiceImpl<UniUserMiniappMapper, UniUserMiniapp> implements UniUserMiniappService {
    
}

