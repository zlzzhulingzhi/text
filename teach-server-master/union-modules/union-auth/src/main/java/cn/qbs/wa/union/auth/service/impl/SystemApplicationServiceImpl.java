package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.SystemApplication;
import cn.qbs.wa.union.auth.mapper.SystemApplicationMapper;
import cn.qbs.wa.union.auth.service.SystemApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【系统应用】(SystemApplication)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:00
 */
@Slf4j
@Service("systemApplicationService")
public class SystemApplicationServiceImpl extends ServiceImpl<SystemApplicationMapper, SystemApplication> implements SystemApplicationService {

}

