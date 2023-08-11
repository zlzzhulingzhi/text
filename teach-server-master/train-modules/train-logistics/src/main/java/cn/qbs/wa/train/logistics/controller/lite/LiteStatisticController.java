package cn.qbs.wa.train.logistics.controller.lite;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.chart.ChartItem;
import cn.qbs.wa.teach.common.core.domain.chart.LineOrBarChart;
import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.TreeDictDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.TreeDictResultDTO;
import cn.qbs.wa.train.logistics.mapper.LiteStatisticMapper;
import cn.qbs.wa.train.logistics.pojo.stat.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api(tags = "小程序-平台统计")
@RestController
@RequestMapping("/lite/stat")
public class LiteStatisticController {

    @Resource
    private LiteStatisticMapper liteStatisticMapper;

    @Resource
    private RemoteTrainDictService remoteTrainDictService;

    @ApiOperation("数据总览")
    @GetMapping("/overview")
    public R<Map<String, String>> overview() {
        if (!isPlatformUser()) {
            return R.ok(Collections.emptyMap());
        }
        int stuNum = liteStatisticMapper.totalStudentNum();
        int orgNum = liteStatisticMapper.totalOrgNum();
        Map<String, String> map = new HashMap<>(4);
        map.put("studentNum", String.valueOf(stuNum));
        map.put("orgNum", String.valueOf(orgNum));
        return R.ok(map);
    }

    @ApiOperation("培训班")
    @GetMapping("/attend-class")
    public R<Map<String, Object>> attendClass(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (!isPlatformUser()) {
            return R.ok(Collections.emptyMap());
        }
        if (startDate == null) {
            startDate = endDate = LocalDate.now();
        }
        List<AttendClassResponse> list = liteStatisticMapper.attendClass(startDate, endDate);
        for (AttendClassResponse clazz : list) {
            // 查询各班级学员人数
            clazz.setStudentNum(liteStatisticMapper.countClassStudent(clazz.getClazzId()));
        }
        Integer totalStuNum = liteStatisticMapper.totalClassStudent(startDate, endDate);
        Integer totalClassNum = liteStatisticMapper.totalAttendClass(startDate, endDate);
        Map<String, Object> map = new HashMap<>(4);
        map.put("top", list);
        map.put("totalStudentNum", totalStuNum);
        map.put("totalClassNum", totalClassNum);
        return R.ok(map);
    }

    @ApiOperation("培训班-分页")
    @PostMapping("/attend-class/page")
    public R<IPage<AttendClassResponse>> attendClassPage(@RequestBody AttendClassPageRequest params) {
        if (!isPlatformUser()) {
            return R.ok(params.createMpPage());
        }
        if (params.getStartDate() == null) {
            LocalDate now = LocalDate.now();
            params.setStartDate(now);
            params.setEndDate(now);
        }
        IPage<AttendClassResponse> page = liteStatisticMapper.attendClassPage(params.createMpPage(), params.getStartDate(), params.getEndDate());
        for (AttendClassResponse clazz : page.getRecords()) {
            // 查询各班级学员人数
            clazz.setStudentNum(liteStatisticMapper.countClassStudent(clazz.getClazzId()));
        }
        return R.ok(page);
    }

    @ApiOperation("宿舍占用数量/空闲数量")
    @GetMapping("/dorm-usage-rate")
    public R<Map<String, Object>> dormUsageRate(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (!isPlatformUser()) {
            return R.ok(Collections.emptyMap());
        }
        if (startDate == null) {
            startDate = endDate = LocalDate.now();
        }
        HashMap<String, Object> map = new HashMap<>();
        // 查询统计所有宿舍类型房型数量
        List<GroupDormitoryDTO> totals = liteStatisticMapper.groupDormitoryTotal();
        if (!totals.isEmpty()) {
            // 查询时间段内已使用的宿舍类型房型数量
            List<GroupDormitoryDTO> useList = liteStatisticMapper.groupDormitoryUsage(startDate, endDate);
            if (!useList.isEmpty()) {
                for (GroupDormitoryDTO usage : useList) {
                    for (GroupDormitoryDTO total : totals) {
                        if (total.getBuilding().equals(usage.getBuilding()) && total.getRoomType().equals(usage.getRoomType())) {
                            total.setUsedNum(usage.getUsedNum());
                            break;
                        }
                    }
                }
            }
            // 远程获取宿舍类型字典值
            Map<String, TreeDictResultDTO> dictMap = getDictMap("dormitory");
            // 根据 building 分组，字典值排序
            Map<String, List<GroupDormitoryDTO>> groups = totals.stream().collect(Collectors.groupingBy(GroupDormitoryDTO::getBuilding,
                    Collectors.collectingAndThen(
                            Collectors.toList(),
                            l -> l.stream()
                                    .peek(e -> Optional.ofNullable(dictMap.get(e.getRoomType())).ifPresent(o -> {
                                        e.setRoomTypeName(o.getDictValue());
                                        e.setSort(o.getSort());
                                    }))
                                    .sorted(Comparator.comparingInt(GroupDormitoryDTO::getSort))
                                    .collect(Collectors.toList())
                    )));
            // 构造柱状图格式
            groups.forEach((k, l) -> {
                // 横坐标值
                List<String> xAxis = l.stream().map(GroupDormitoryDTO::getRoomType).collect(Collectors.toList());
                // 横坐标对应的纵坐标的值
                List<Number> usingList = new ArrayList<>(l.size());
                List<Number> freeList = new ArrayList<>(l.size());
                for (GroupDormitoryDTO dynamicVO : l) {
                    Integer using = Optional.ofNullable(dynamicVO.getUsedNum()).orElse(0);
                    usingList.add(using);
                    freeList.add(dynamicVO.getRoomNum() - using);
                }
                List<ChartItem> chartItems = new ArrayList<>(2);
                chartItems.add(new ChartItem("using", "占用数量", usingList));
                chartItems.add(new ChartItem("free", "空闲数量", freeList));
                LineOrBarChart barChart = new LineOrBarChart();
                barChart.setXAxis(xAxis);
                barChart.setChartItems(chartItems);
                map.put(k, barChart);
            });
        }
        return R.ok(map);
    }

    @ApiOperation("宿舍占用数量/空闲数量-V2")
    @GetMapping("/dorm-usage-rate/v2")
    public R<Map<String, List<GroupDormitoryDTO>>> dormUsageRateV2(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
        if (!isPlatformUser()) {
            return R.ok(Collections.emptyMap());
        }
        if (day == null) {
            day = LocalDate.now();
        }
        Map<String, List<GroupDormitoryDTO>> map = new HashMap<>();
        // 查询统计所有宿舍类型房型数量
        List<GroupDormitoryDTO> totals = liteStatisticMapper.groupDormitoryTotalV2();
        if (!totals.isEmpty()) {
            // 查询时间段内已使用的宿舍类型房型数量
            List<GroupDormitoryFreeDTO> useList = liteStatisticMapper.groupDormitoryUsageV2(day);
            if (!useList.isEmpty()) {
                Map<String, Integer> roomTypeMap = useList.stream().collect(Collectors.toMap(GroupDormitoryFreeDTO::getRoomTypeCode, GroupDormitoryFreeDTO::getFreeNum, (a, b) -> a));
                for (GroupDormitoryDTO total : totals) {
                    if (roomTypeMap.containsKey(total.getRoomType())) {
                        Integer freeNum = roomTypeMap.get(total.getRoomType());
                        total.setUsedNum(total.getRoomNum() - total.getMaintNum() - freeNum);
                    }
                    if (total.getUsedNum() == null || total.getUsedNum() < 0) {
                        total.setUsedNum(0);
                    }
                }
            }
            // 根据 building 分组，字典值排序
            map = totals.stream().collect(Collectors.groupingBy(GroupDormitoryDTO::getBuilding,
                    Collectors.collectingAndThen(
                            Collectors.toList(),
                            l -> l.stream().sorted(Comparator.comparingInt(GroupDormitoryDTO::getSort)).collect(Collectors.toList())
                    )));
        }
        return R.ok(map);
    }

    @ApiOperation("教室占用数量/空闲数量")
    @GetMapping("/classroom-usage-rate")
    public R<List<GroupClassroomDTO>> classroomUsageRate(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (!isPlatformUser()) {
            return R.ok(Collections.emptyList());
        }
        if (startDate == null) {
            startDate = endDate = LocalDate.now();
        }
        // 查询统计所有教室类型数量
        List<GroupClassroomDTO> totals = liteStatisticMapper.groupClassroomTotal();
        if (!totals.isEmpty()) {
            // 查询时间段内已使用的教室类型数量
            List<GroupClassroomDTO> useList = liteStatisticMapper.groupClassroomUsage(startDate, endDate);
            if (!useList.isEmpty()) {
                for (GroupClassroomDTO usage : useList) {
                    for (GroupClassroomDTO total : totals) {
                        if (total.getRoomType().equals(usage.getRoomType())) {
                            total.setUsedNum(usage.getUsedNum());
                            break;
                        }
                    }
                }
            }
            // 远程获取宿舍类型字典值
            Map<String, TreeDictResultDTO> dictMap = getDictMap("classroom");
            totals = totals.stream()
                    .peek(e -> {
                        if (e.getUsedNum() == null) {
                            e.setUsedNum(0);
                        }
                        Optional.ofNullable(dictMap.get(e.getRoomType())).ifPresent(o -> {
                            e.setRoomType(o.getDictValue());
                            e.setSort(o.getSort());
                        });
                    })
                    .sorted(Comparator.comparingInt(GroupClassroomDTO::getSort))
                    .collect(Collectors.toList());
        }
        return R.ok(totals);
    }

    @NotNull
    private Map<String, TreeDictResultDTO> getDictMap(String dictCode) {
        Map<String, TreeDictResultDTO> dictMap = Collections.emptyMap();
        R<List<TreeDictResultDTO>> r = remoteTrainDictService.treeList(new TreeDictDTO(dictCode));
        if (CollUtil.isNotEmpty(r.getRemoteData())) {
            List<TreeDictResultDTO> children = r.getData().get(0).getChildren();
            dictMap = children.stream().collect(Collectors.toMap(TreeDictResultDTO::getDictKey, Function.identity(), (a, b) -> a));
        }
        return dictMap;
    }

    private boolean isPlatformUser() {
        return UserConstants.USER_PLATFORM.equals(SecurityContextHolder.getUserType());
    }
}
