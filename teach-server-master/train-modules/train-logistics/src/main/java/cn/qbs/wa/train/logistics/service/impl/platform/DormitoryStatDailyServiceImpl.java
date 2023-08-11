package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.train.logistics.entity.DormitoryStatDaily;
import cn.qbs.wa.train.logistics.mapper.DormitoryStatDailyMapper;
import cn.qbs.wa.train.logistics.service.platform.DormitoryStatDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 宿舍房型每日剩余数量(DormitoryStatDaily)表服务实现类
 *
 * @author makejava
 * @since 2023-06-05 10:59:42
 */
@Slf4j
@Service("dormitoryStatDailyService")
public class DormitoryStatDailyServiceImpl extends ServiceImpl<DormitoryStatDailyMapper, DormitoryStatDaily> implements DormitoryStatDailyService {
    
}

