package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.train.logistics.entity.DormitoryStat;
import cn.qbs.wa.train.logistics.mapper.DormitoryStatMapper;
import cn.qbs.wa.train.logistics.service.platform.DormitoryStatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 宿舍房型统计(DormitoryStat)表服务实现类
 *
 * @author makejava
 * @since 2023-06-05 10:59:42
 */
@Slf4j
@Service("dormitoryStatService")
public class DormitoryStatServiceImpl extends ServiceImpl<DormitoryStatMapper, DormitoryStat> implements DormitoryStatService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteByIds(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return Boolean.FALSE;
        }
        return this.removeByIds(idList);
    }

}

