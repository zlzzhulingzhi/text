package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.UniApplicationClient;
import cn.qbs.wa.union.auth.mapper.UniApplicationClientMapper;
import cn.qbs.wa.union.auth.service.UniApplicationClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 统一应用客户端(UniApplicationClient)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@Slf4j
@Service("uniApplicationClientService")
public class UniApplicationClientServiceImpl extends ServiceImpl<UniApplicationClientMapper, UniApplicationClient> implements UniApplicationClientService {

}

