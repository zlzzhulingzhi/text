package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.DormitoryStat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 宿舍房型统计(DormitoryStat)表服务接口
 *
 * @author makejava
 * @since 2023-06-05 10:59:42
 */
public interface DormitoryStatService extends IService<DormitoryStat> {

    Boolean deleteByIds(List<Long> idList);
}

