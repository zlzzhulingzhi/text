package cn.qbs.wa.train.logistics.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.logistics.constants.RedisCacheKey;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class DormitoryDailyExcelListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 每隔多少条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 30;

    /**
     * 数据记录
     */
    private final List<DormitoryDailyStatData> records = new ArrayList<>(BATCH_COUNT);

    /**
     * 表头信息
     */
    private Map<Integer, String> headMap;

    /**
     * 这个每一行数据解析都会被调用
     *
     * @param data    一行数据
     * @param context 当前解析上下文
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        if (data.containsValue("日期")) {
            // 获取到表头数据
            this.headMap = data;
        } else {
            if (CollUtil.isNotEmpty(headMap)) {
                // 每一行的数据的存储对象
                DormitoryDailyStatData tmp = new DormitoryDailyStatData();
                tmp.setDailyList(new ArrayList<>(12));
                Integer rowIndex = context.readRowHolder().getRowIndex();
                for (Map.Entry<Integer, String> entry : data.entrySet()) {
                    // 当前列对应的表头名称
                    String val = headMap.get(entry.getKey());
                    if (StrUtil.isBlank(val)) {
                        continue;
                    }
                    switch (val.trim()) {
                        case "日期":
                            try {
                                tmp.setDay(LocalDate.parse(entry.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                            } catch (DateTimeParseException e) {
                                throw new DateTimeParseException(
                                        StrUtil.format("第{}行，日期列格式不正确，请输入正确的日期，格式为: yyyy-MM-dd", rowIndex + 1),
                                        e.getParsedString(), e.getErrorIndex());
                            }
                            break;
                        case "描述":
                            tmp.setDescr(entry.getValue());
                            break;
                        case "总量":
                            tmp.setTotals(entry.getValue());
                            break;
                        case "总量含超预留":
                            tmp.setExceedTotals(entry.getValue());
                            break;
                        case "非确认":
                            tmp.setUnconfirmed(entry.getValue());
                            break;
                        case "出租率":
                            tmp.setOccupancyRate(entry.getValue());
                            break;
                        default:
                            try {
                                DormitoryDailyDetail dormitoryStatDaily = new DormitoryDailyDetail(val, Integer.valueOf(entry.getValue()));
                                tmp.getDailyList().add(dormitoryStatDaily);
                            } catch (NumberFormatException e) {
                                throw new NumberFormatException(StrUtil.format("第{}行，{}列为非数字类型，请输入正确的数字", rowIndex + 1, val));
                            }
                    }
                }
                records.add(tmp);
            }
        }

    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context 当前解析上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // TODO
    }

    public List<DormitoryDailyStatData> getRecords() {
        return records;
    }

    /**
     * 统一异常处理入口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(Exception e, AnalysisContext context) throws Exception {
        // 自定义异常
        if (e instanceof DateTimeParseException || e instanceof NumberFormatException) {
            throw new ServiceException(e.getMessage());
        } else {
            log.error("第{}行，解析出错：{}", context.readRowHolder().getRowIndex() + 1, e.getMessage(), e);
            throw new ServiceException(StrUtil.format("第{}行，解析出错"));
        }
    }

}