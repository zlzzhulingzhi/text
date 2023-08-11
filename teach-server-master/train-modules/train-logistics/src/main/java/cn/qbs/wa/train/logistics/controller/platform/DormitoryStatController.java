package cn.qbs.wa.train.logistics.controller.platform;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.constants.RedisCacheKey;
import cn.qbs.wa.train.logistics.entity.DormitoryStat;
import cn.qbs.wa.train.logistics.entity.DormitoryStatDaily;
import cn.qbs.wa.train.logistics.entity.DormitoryStatImport;
import cn.qbs.wa.train.logistics.excel.DormitoryDailyDetail;
import cn.qbs.wa.train.logistics.excel.DormitoryDailyExcelListener;
import cn.qbs.wa.train.logistics.excel.DormitoryDailyStatData;
import cn.qbs.wa.train.logistics.pojo.dormitorystat.DormitoryStatAddRequest;
import cn.qbs.wa.train.logistics.pojo.dormitorystat.DormitoryStatDailyPageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitorystat.DormitoryStatPageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitorystat.DormitoryStatUpdateRequest;
import cn.qbs.wa.train.logistics.service.platform.DormitoryStatDailyService;
import cn.qbs.wa.train.logistics.service.platform.DormitoryStatImportService;
import cn.qbs.wa.train.logistics.service.platform.DormitoryStatService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 宿舍房型统计(DormitoryStat)表控制层
 *
 * @author makejava
 * @since 2023-06-05 11:06:16
 */
@Api(tags = "房态信息")
@RestController
@RequestMapping("/dormitoryStat")
public class DormitoryStatController {

    @Resource
    private DormitoryStatService dormitoryStatService;

    @Resource
    private DormitoryStatDailyService dormitoryStatDailyService;

    @Resource
    private DormitoryStatImportService dormitoryStatImportService;

    @Resource
    private RedisService redisService;

    /**
     * 新增宿舍房型统计
     */
    @PostMapping("/add")
    @RequiresPermissions("Management:Dormitory:Stat")
    public R<Boolean> add(@RequestBody @Validated DormitoryStatAddRequest params) {
        DormitoryStat dormitoryStat = BeanUtil.copyProperties(params, DormitoryStat.class);
        return R.ok(this.dormitoryStatService.save(dormitoryStat));
    }

    /**
     * 分页查询宿舍房型统计
     */
    @PostMapping("/page")
    @RequiresPermissions("Management:Dormitory:Stat")
    public R<IPage<DormitoryStat>> page(@RequestBody DormitoryStatPageRequest params) {
        Page<DormitoryStat> page = params.createMpPage();
        LambdaQueryWrapper<DormitoryStat> queryWrapper = Wrappers.<DormitoryStat>lambdaQuery()
                .eq(StrUtil.isNotBlank(params.getBuilding()), DormitoryStat::getBuilding, params.getBuilding())
                .like(StrUtil.isNotBlank(params.getRoomTypeName()), DormitoryStat::getRoomTypeName, params.getRoomTypeName())
                .eq(params.getEnabled() != null, DormitoryStat::getEnabled, params.getEnabled())
                .orderByAsc(DormitoryStat::getBuilding, DormitoryStat::getSort, DormitoryStat::getId);
        return R.ok(this.dormitoryStatService.page(page, queryWrapper));

    }

    /**
     * 查询宿舍房型统计详情
     */
    @PostMapping("/detail")
    @RequiresPermissions("Management:Dormitory:Stat")
    public R<DormitoryStat> detail(@RequestBody @Validated IdRequest request) {
        return R.ok(this.dormitoryStatService.getById(request.getId()));
    }

    /**
     * 修改宿舍房型统计
     */
    @PostMapping("/update")
    @RequiresPermissions("Management:Dormitory:Stat")
    public R<Boolean> update(@RequestBody @Validated DormitoryStatUpdateRequest params) {
        DormitoryStat dormitoryStat = BeanUtil.copyProperties(params, DormitoryStat.class);
        DormitoryStat stat = this.dormitoryStatService.getById(params.getId());
        if (stat == null) {
            return R.fail("数据不存在");
        }
        // 维修数量
        Integer maintNum = Optional.ofNullable(params.getMaintNum()).orElseGet(() -> Optional.ofNullable(stat.getMaintNum()).orElse(0));
        // 当日剩余
        DormitoryStatDaily today = dormitoryStatDailyService.lambdaQuery()
                .select(DormitoryStatDaily::getFree)
                .eq(DormitoryStatDaily::getRoomTypeCode, stat.getRoomTypeCode())
                .eq(DormitoryStatDaily::getDay, LocalDate.now())
                .one();
        // 判断是否 当日剩余 + 维修 > 总数量
        Integer roomNum = Optional.ofNullable(params.getRoomNum()).orElse(stat.getRoomNum());
        if (today != null) {
            if (maintNum + today.getFree() > roomNum) {
                return R.fail("房间的当日剩余可住数量与维修数量之和不能大于总数");
            }
        } else {
            if (maintNum > roomNum) {
                return R.fail("房间的维修数量不能大于总数");
            }
        }
        return R.ok(this.dormitoryStatService.updateById(dormitoryStat));
    }

    /**
     * 删除宿舍房型统计
     */
    @PostMapping("/delete")
    @RequiresPermissions("Management:Dormitory:Stat")
    public R<Boolean> delete(@RequestBody @Validated IdListRequest request) {
        return R.ok(this.dormitoryStatService.deleteByIds(request.getIdList()));
    }

    @ApiOperation(value = "导入预览")
    @PostMapping("/import/preview")
    @RequiresPermissions("Management:Dormitory:Stat")
    public R<?> importPreview(MultipartFile file) throws IOException {
        DormitoryDailyExcelListener excelListener = new DormitoryDailyExcelListener();
        EasyExcel.read(file.getInputStream(), excelListener).ignoreEmptyRow(Boolean.TRUE).sheet().doRead();
        List<DormitoryDailyStatData> records = excelListener.getRecords();
        String batchNo = IdUtil.simpleUUID();
        redisService.setCacheObject(RedisCacheKey.PREFIX + "import:" + batchNo, JSON.toJSONString(records), 30L, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>(4);
        map.put("batchNo", batchNo);
        map.put("records", records);
        return R.ok(map);
    }

    @ApiOperation(value = "导入数据")
    @PostMapping("/import/exec/{batchNo}")
    @RequiresPermissions("Management:Dormitory:Stat")
    public R<Void> importExec(@PathVariable String batchNo) {
        String cacheKey = RedisCacheKey.PREFIX + "import:" + batchNo;
        if (redisService.hasKey(cacheKey)) {
            String jsonStr = redisService.getCacheObject(cacheKey);
            List<DormitoryDailyStatData> records = JSONArray.parseArray(jsonStr, DormitoryDailyStatData.class);
            dormitoryStatImportService.importData(records);
            redisService.deleteObject(cacheKey);
        } else {
            return R.fail("导入批次号不正确 或 页面停留时间过长，需要重新导入数据");
        }
        return R.ok();
    }


    @ApiOperation(value = "导入数据-分页")
    @PostMapping("/import/page")
    @RequiresPermissions("Management:Dormitory:Stat")
    public R<IPage<DormitoryDailyStatData>> importPage(@RequestBody DormitoryStatDailyPageRequest params) {
        LambdaQueryWrapper<DormitoryStatImport> queryWrapper = Wrappers.<DormitoryStatImport>lambdaQuery()
                .between(params.getStartDate() != null && params.getEndDate() != null, DormitoryStatImport::getDay, params.getStartDate(), params.getEndDate())
                .orderByDesc(DormitoryStatImport::getDay);

        IPage<DormitoryStatImport> page = dormitoryStatImportService.page(params.createMpPage(), queryWrapper);
        IPage<DormitoryDailyStatData> resultPage = page.convert(r -> {
            DormitoryDailyStatData dailyStatData = BeanUtil.copyProperties(r, DormitoryDailyStatData.class);
            List<DormitoryStatDaily> dailyList = dormitoryStatDailyService.lambdaQuery().eq(DormitoryStatDaily::getDormitoryStatImportId, r.getId()).list();
            if (CollUtil.isNotEmpty(dailyList)) {
                dailyStatData.setDailyList(dailyList.stream().map(d -> new DormitoryDailyDetail(d.getRoomTypeCode(), d.getFree())).collect(Collectors.toList()));
            }
            return dailyStatData;
        });
        return R.ok(resultPage);
    }
}

