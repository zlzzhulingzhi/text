package cn.qbs.wa.train.screen.excel;

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
public class ScreenStatDataDynamicExcelData {

    // 标题 内容 发布时间 基础数据类.这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "数据名称")
    @ExcelProperty(value = "*数据名称")
    private String dataName;

    @ApiModelProperty(value = "已使用数量")
    @ExcelProperty(value = "*已使用数量")
    private Integer usingNum;

    @ApiModelProperty(value = "空闲数量")
    @ExcelProperty(value = "*空闲数量")
    private Integer freeNum;


}
