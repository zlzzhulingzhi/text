package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.DormitoryStatImport;
import cn.qbs.wa.train.logistics.excel.DormitoryDailyStatData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 宿舍房型导入报表数据(DormitoryStatImport)表服务接口
 *
 * @author makejava
 * @since 2023-06-05 10:59:42
 */
public interface DormitoryStatImportService extends IService<DormitoryStatImport> {

    Boolean importData(List<DormitoryDailyStatData> records);
}

