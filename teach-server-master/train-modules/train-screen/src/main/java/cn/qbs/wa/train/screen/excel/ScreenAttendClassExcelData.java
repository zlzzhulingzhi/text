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
public class ScreenAttendClassExcelData {

    // 标题 内容 发布时间 基础数据类.这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "机构名称")
    @ExcelProperty(value = "*机构名称")
    private String orgName;

    @ApiModelProperty(value = "班级名称")
    @ExcelProperty(value = "*班级名称")
    private String clazzName;

    @ApiModelProperty(value = "学员人数")
    @ExcelProperty(value = "*学员人数")
    private Integer studentNum;

    @ApiModelProperty(value = "教室")
    @ExcelProperty(value = "*教室")
    private String classroom;

}
