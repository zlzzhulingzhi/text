package cn.qbs.wa.train.logistics.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DormitoryDailyStatData {

    @ExcelProperty("日期")
    private LocalDate day;

    @ExcelProperty("描述")
    private String descr;

    @ExcelProperty("总量")
    private String totals;

    @ExcelProperty("总量含超预留")
    private String exceedTotals;

    @ExcelProperty("非确认")
    private String unconfirmed;

    @ExcelProperty("出租率")
    private String occupancyRate;

    List<DormitoryDailyDetail> dailyList;

}