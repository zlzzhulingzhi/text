package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.UniApplication;
import cn.qbs.wa.union.auth.mapper.UniApplicationMapper;
import cn.qbs.wa.union.auth.service.UniApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 统一应用展示(UniApplication)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
@Slf4j
@Service("uniApplicationService")
public class UniApplicationServiceImpl extends ServiceImpl<UniApplicationMapper, UniApplication> implements UniApplicationService {

}

