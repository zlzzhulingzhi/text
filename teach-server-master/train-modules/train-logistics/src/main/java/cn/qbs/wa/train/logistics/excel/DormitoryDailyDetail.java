package cn.qbs.wa.train.logistics.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DormitoryDailyDetail {

    @ExcelProperty("房型编号")
    private String roomTypeCode;

    @ExcelProperty("剩余房数")
    private Integer free;

}