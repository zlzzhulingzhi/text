package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.train.logistics.entity.DormitoryStatDaily;
import cn.qbs.wa.train.logistics.entity.DormitoryStatImport;
import cn.qbs.wa.train.logistics.excel.DormitoryDailyStatData;
import cn.qbs.wa.train.logistics.mapper.DormitoryStatImportMapper;
import cn.qbs.wa.train.logistics.service.platform.DormitoryStatDailyService;
import cn.qbs.wa.train.logistics.service.platform.DormitoryStatImportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 宿舍房型导入报表数据(DormitoryStatImport)表服务实现类
 *
 * @author makejava
 * @since 2023-06-05 10:59:42
 */
@Slf4j
@Service("dormitoryStatImportService")
public class DormitoryStatImportServiceImpl extends ServiceImpl<DormitoryStatImportMapper, DormitoryStatImport> implements DormitoryStatImportService {

    @Resource
    private DormitoryStatDailyService dormitoryStatDailyService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean importData(List<DormitoryDailyStatData> records) {
        List<DormitoryStatDaily> list = new ArrayList<>(150);
        // 防止重复导入，需要先将旧数据删除，再新增
        List<LocalDate> collect = records.stream().map(DormitoryDailyStatData::getDay).distinct().collect(Collectors.toList());
        List<DormitoryStatImport> oldImports = this.lambdaQuery().select(DormitoryStatImport::getId).in(DormitoryStatImport::getDay, collect).list();
        if (CollUtil.isNotEmpty(oldImports)) {
            List<Long> ids = oldImports.stream().map(DormitoryStatImport::getId).collect(Collectors.toList());
            dormitoryStatDailyService.lambdaUpdate().in(DormitoryStatDaily::getDormitoryStatImportId, ids).remove();
            this.removeBatchByIds(ids);
        }
        for (DormitoryDailyStatData record : records) {
            DormitoryStatImport dormitoryStatImport = BeanUtil.copyProperties(record, DormitoryStatImport.class);
            this.save(dormitoryStatImport);
            if (CollUtil.isNotEmpty(record.getDailyList())) {
                List<DormitoryStatDaily> dailyList = record.getDailyList().stream().map(d -> {
                    DormitoryStatDaily statDaily = new DormitoryStatDaily();
                    statDaily.setDormitoryStatImportId(dormitoryStatImport.getId());
                    statDaily.setDay(dormitoryStatImport.getDay());
                    statDaily.setFree(d.getFree());
                    statDaily.setRoomTypeCode(d.getRoomTypeCode());
                    return statDaily;
                }).collect(Collectors.toList());
                list.addAll(dailyList);
            }
        }
        if (!list.isEmpty()) {
            dormitoryStatDailyService.saveBatch(list);
        }
        return Boolean.TRUE;
    }
}

