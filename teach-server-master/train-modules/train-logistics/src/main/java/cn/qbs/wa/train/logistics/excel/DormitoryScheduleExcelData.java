package cn.qbs.wa.train.logistics.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/12 14:38
 */
@Data
public class DormitoryScheduleExcelData {

    // 真实姓名 手机号码 性别 基础数据类.这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "机构名称")
    @ExcelProperty(value = "*机构名称")
    private String orgName;

    @ApiModelProperty(value = "宿舍单元")
    @ExcelProperty(value = "*宿舍单元")
    private String building;

    @ApiModelProperty(value = "楼层")
    @ExcelProperty(value = "*楼层")
    private String floor;

    @ApiModelProperty(value = "房号")
    @ExcelProperty(value = "*房号")
    private String roomNo;

    @ApiModelProperty(value = "房型")
    @ExcelProperty(value = "房型")
    private String roomType;

    @ApiModelProperty(value = "使用日期")
    @ExcelProperty(value = "*使用日期")
    private String useDate;
}
