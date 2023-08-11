package cn.qbs.wa.train.logistics.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/12 14:38
 */
@Data
public class ClassroomExcelData {

    // 真实姓名 手机号码 性别 基础数据类.这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "教室单元")
    @ExcelProperty(value = "*教室单元")
    private String building;

    @ApiModelProperty(value = "楼层")
    @ExcelProperty(value = "*楼层")
    private String floor;

    @ApiModelProperty(value = "教室编号")
    @ExcelProperty(value = "*房间号")
    private String roomNo;

    @ApiModelProperty(value = "面积")
    @ExcelProperty(value = "*面积")
    private BigDecimal area;

    @ApiModelProperty(value = "座位数")
    @ExcelProperty(value = "*座位数")
    private Integer seats;

    @ApiModelProperty(value = "类别")
    @ExcelProperty(value = "类别")
    private String roomType;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注")
    private String remark;
}
